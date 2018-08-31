package io.github.softech.dev.sgill.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Bookmarks.
 */
@Entity
@Table(name = "bookmarks")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bookmarks")
public class Bookmarks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bookmark", nullable = false)
    private String bookmark;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Section section;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookmark() {
        return bookmark;
    }

    public Bookmarks bookmark(String bookmark) {
        this.bookmark = bookmark;
        return this;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getText() {
        return text;
    }

    public Bookmarks text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Section getSection() {
        return section;
    }

    public Bookmarks section(Section section) {
        this.section = section;
        return this;
    }

    public void setSection(Section section) {
        this.section = section;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bookmarks bookmarks = (Bookmarks) o;
        if (bookmarks.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bookmarks.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bookmarks{" +
            "id=" + getId() +
            ", bookmark='" + getBookmark() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
