package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Applicant;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.Job;
import com.mycompany.myapp.domain.Task;
import com.mycompany.myapp.service.dto.ApplicantDTO;
import com.mycompany.myapp.service.dto.CompanyDTO;
import com.mycompany.myapp.service.dto.JobDTO;
import com.mycompany.myapp.service.dto.TaskDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobMapper extends EntityMapper<JobDTO, Job> {
    @Mapping(target = "tasks", source = "tasks", qualifiedByName = "taskTitleSet")
    @Mapping(target = "applicants", source = "applicants", qualifiedByName = "applicantEmailSet")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    JobDTO toDto(Job s);

    @Mapping(target = "removeTask", ignore = true)
    @Mapping(target = "removeApplicant", ignore = true)
    Job toEntity(JobDTO jobDTO);

    @Named("taskTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    TaskDTO toDtoTaskTitle(Task task);

    @Named("taskTitleSet")
    default Set<TaskDTO> toDtoTaskTitleSet(Set<Task> task) {
        return task.stream().map(this::toDtoTaskTitle).collect(Collectors.toSet());
    }

    @Named("applicantEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ApplicantDTO toDtoApplicantEmail(Applicant applicant);

    @Named("applicantEmailSet")
    default Set<ApplicantDTO> toDtoApplicantEmailSet(Set<Applicant> applicant) {
        return applicant.stream().map(this::toDtoApplicantEmail).collect(Collectors.toSet());
    }

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
