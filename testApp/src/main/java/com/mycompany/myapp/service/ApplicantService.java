package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Applicant;
import com.mycompany.myapp.repository.ApplicantRepository;
import com.mycompany.myapp.service.dto.ApplicantDTO;
import com.mycompany.myapp.service.mapper.ApplicantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Applicant}.
 */
@Service
@Transactional
public class ApplicantService {

    private final Logger log = LoggerFactory.getLogger(ApplicantService.class);

    private final ApplicantRepository applicantRepository;

    private final ApplicantMapper applicantMapper;

    public ApplicantService(ApplicantRepository applicantRepository, ApplicantMapper applicantMapper) {
        this.applicantRepository = applicantRepository;
        this.applicantMapper = applicantMapper;
    }

    /**
     * Save a applicant.
     *
     * @param applicantDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicantDTO save(ApplicantDTO applicantDTO) {
        log.debug("Request to save Applicant : {}", applicantDTO);
        Applicant applicant = applicantMapper.toEntity(applicantDTO);
        applicant = applicantRepository.save(applicant);
        return applicantMapper.toDto(applicant);
    }

    /**
     * Update a applicant.
     *
     * @param applicantDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicantDTO update(ApplicantDTO applicantDTO) {
        log.debug("Request to update Applicant : {}", applicantDTO);
        Applicant applicant = applicantMapper.toEntity(applicantDTO);
        applicant = applicantRepository.save(applicant);
        return applicantMapper.toDto(applicant);
    }

    /**
     * Partially update a applicant.
     *
     * @param applicantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicantDTO> partialUpdate(ApplicantDTO applicantDTO) {
        log.debug("Request to partially update Applicant : {}", applicantDTO);

        return applicantRepository
            .findById(applicantDTO.getId())
            .map(existingApplicant -> {
                applicantMapper.partialUpdate(existingApplicant, applicantDTO);

                return existingApplicant;
            })
            .map(applicantRepository::save)
            .map(applicantMapper::toDto);
    }

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Applicants");
        return applicantRepository.findAll(pageable).map(applicantMapper::toDto);
    }

    /**
     * Get one applicant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicantDTO> findOne(Long id) {
        log.debug("Request to get Applicant : {}", id);
        return applicantRepository.findById(id).map(applicantMapper::toDto);
    }

    /**
     * Delete the applicant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Applicant : {}", id);
        applicantRepository.deleteById(id);
    }
}
