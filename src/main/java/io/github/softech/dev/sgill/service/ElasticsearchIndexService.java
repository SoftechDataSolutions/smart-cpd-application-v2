package io.github.softech.dev.sgill.service;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.softech.dev.sgill.domain.*;
import io.github.softech.dev.sgill.repository.*;
import io.github.softech.dev.sgill.repository.search.*;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.indices.*;
import org.elasticsearch.index.IndexShardAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToMany;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElasticsearchIndexService {

    private static final Lock reindexLock = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private CartRepository cartRepository;

    @Inject
    private CartSearchRepository cartSearchRepository;

    @Inject
    private CertificateRepository certificateRepository;

    @Inject
    private CertificateSearchRepository certificateSearchRepository;

    @Inject
    private ChoiceRepository choiceRepository;

    @Inject
    private ChoiceSearchRepository choiceSearchRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private CourseRepository courseRepository;

    @Inject
    private CourseSearchRepository courseSearchRepository;

    @Inject
    private CourseCartBridgeRepository courseCartBridgeRepository;

    @Inject
    private CourseCartBridgeSearchRepository courseCartBridgeSearchRepository;

    @Inject
    private CourseHistoryRepository courseHistoryRepository;

    @Inject
    private CourseHistorySearchRepository courseHistorySearchRepository;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerSearchRepository customerSearchRepository;

    @Inject
    private OrdersRepository ordersRepository;

    @Inject
    private OrdersSearchRepository ordersSearchRepository;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private QuestionSearchRepository questionSearchRepository;

    @Inject
    private QuestionHistoryRepository questionHistoryRepository;

    @Inject
    private QuestionHistorySearchRepository questionHistorySearchRepository;

    @Inject
    private QuizRepository quizRepository;

    @Inject
    private QuizSearchRepository quizSearchRepository;

    @Inject
    private QuizHistoryRepository quizHistoryRepository;

    @Inject
    private QuizHistorySearchRepository quizHistorySearchRepository;

    @Inject
    private SectionRepository sectionRepository;

    @Inject
    private SectionSearchRepository sectionSearchRepository;

    @Inject
    private SectionHistoryRepository sectionHistoryRepository;

    @Inject
    private SectionHistorySearchRepository sectionHistorySearchRepository;

    @Inject
    private TimeCourseLogRepository timeCourseLogRepository;

    @Inject
    private TimeCourseLogSearchRepository timeCourseLogSearchRepository;

    @Inject
    private TopicRepository topicRepository;

    @Inject
    private TopicSearchRepository topicSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        if (reindexLock.tryLock()) {
            try {
                reindexForClass(Cart.class, cartRepository, cartSearchRepository);
                reindexForClass(Certificate.class, certificateRepository, certificateSearchRepository);
                reindexForClass(Choice.class, choiceRepository, choiceSearchRepository);
                reindexForClass(Company.class, companyRepository, companySearchRepository);
                reindexForClass(Course.class, courseRepository, courseSearchRepository);
                reindexForClass(CourseCartBridge.class, courseCartBridgeRepository, courseCartBridgeSearchRepository);
                reindexForClass(CourseHistory.class, courseHistoryRepository, courseHistorySearchRepository);
                reindexForClass(Customer.class, customerRepository, customerSearchRepository);
                reindexForClass(Orders.class, ordersRepository, ordersSearchRepository);
                reindexForClass(Question.class, questionRepository, questionSearchRepository);
                reindexForClass(QuestionHistory.class, questionHistoryRepository, questionHistorySearchRepository);
                reindexForClass(Quiz.class, quizRepository, quizSearchRepository);
                reindexForClass(QuizHistory.class, quizHistoryRepository, quizHistorySearchRepository);
                reindexForClass(Section.class, sectionRepository, sectionSearchRepository);
                reindexForClass(SectionHistory.class, sectionHistoryRepository, sectionHistorySearchRepository);
                reindexForClass(TimeCourseLog.class, timeCourseLogRepository, timeCourseLogSearchRepository);
                reindexForClass(Topic.class, topicRepository, topicSearchRepository);
                reindexForClass(User.class, userRepository, userSearchRepository);

                log.info("Elasticsearch: Successfully performed reindexing");
            } finally {
                reindexLock.unlock();
            }
        } else {
            log.info("Elasticsearch: concurrent reindexing attempt");
        }
    }

    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexShardAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            // if a JHipster entity field is the owner side of a many-to-many relationship, it should be loaded manually
            List<Method> relationshipGetters = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getType().equals(Set.class))
                .filter(field -> field.getAnnotation(ManyToMany.class) != null)
                .filter(field -> field.getAnnotation(ManyToMany.class).mappedBy().isEmpty())
                .filter(field -> field.getAnnotation(JsonIgnore.class) == null)
                .map(field -> {
                    try {
                        return new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
                    } catch (IntrospectionException e) {
                        log.error("Error retrieving getter for class {}, field {}. Field will NOT be indexed",
                            entityClass.getSimpleName(), field.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            int size = 100;
            for (int i = 0; i <= jpaRepository.count() / size; i++) {
                Pageable page = new PageRequest(i, size);
                log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
                Page<T> results = jpaRepository.findAll(page);
                results.map(result -> {
                    // if there are any relationships to load, do it now
                    relationshipGetters.forEach(method -> {
                        try {
                            // eagerly load the relationship set
                            ((Set) method.invoke(result)).size();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    return result;
                });
                elasticsearchRepository.saveAll(results.getContent());
            }
        }
        log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
    }
}
