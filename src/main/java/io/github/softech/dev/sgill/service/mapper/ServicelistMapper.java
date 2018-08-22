package io.github.softech.dev.sgill.service.mapper;

import io.github.softech.dev.sgill.domain.*;
import io.github.softech.dev.sgill.service.dto.ServicelistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Servicelist and its DTO ServicelistDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServicelistMapper extends EntityMapper<ServicelistDTO, Servicelist> {


    @Mapping(target = "customer", ignore = true)
    Servicelist toEntity(ServicelistDTO servicelistDTO);

    default Servicelist fromId(Long id) {
        if (id == null) {
            return null;
        }
        Servicelist servicelist = new Servicelist();
        servicelist.setId(id);
        return servicelist;
    }
}
