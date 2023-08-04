package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Questionaire;
import com.mycompany.myapp.domain.Task;
import com.mycompany.myapp.service.dto.QuestionaireDTO;
import com.mycompany.myapp.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Questionaire} and its DTO {@link QuestionaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionaireMapper extends EntityMapper<QuestionaireDTO, Questionaire> {
    @Mapping(target = "task", source = "task", qualifiedByName = "taskId")
    QuestionaireDTO toDto(Questionaire s);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);
}
