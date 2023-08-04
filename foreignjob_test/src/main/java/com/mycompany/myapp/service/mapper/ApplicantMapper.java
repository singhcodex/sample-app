package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Applicant;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.ApplicantDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Applicant} and its DTO {@link ApplicantDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicantMapper extends EntityMapper<ApplicantDTO, Applicant> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ApplicantDTO toDto(Applicant s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
