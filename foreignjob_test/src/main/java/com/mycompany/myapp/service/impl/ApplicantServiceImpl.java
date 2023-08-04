package com.mycompany.myapp.service.impl;

import static org.springframework.data.elasticsearch.client.elc.QueryBuilders.*;

import com.mycompany.myapp.domain.Applicant;
import com.mycompany.myapp.repository.ApplicantRepository;
import com.mycompany.myapp.repository.search.ApplicantSearchRepository;
import com.mycompany.myapp.service.ApplicantService;
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
public class ApplicantServiceImpl implements ApplicantService {

    private final Logger log = LoggerFactory.getLogger(ApplicantServiceImpl.class);

    private final ApplicantRepository applicantRepository;

    private final ApplicantMapper applicantMapper;

    private final ApplicantSearchRepository applicantSearchRepository;

    public ApplicantServiceImpl(
        ApplicantRepository applicantRepository,
        ApplicantMapper applicantMapper,
        ApplicantSearchRepository applicantSearchRepository
    ) {
        this.applicantRepository = applicantRepository;
        this.applicantMapper = applicantMapper;
        this.applicantSearchRepository = applicantSearchRepository;
    }

    @Override
    public ApplicantDTO save(ApplicantDTO applicantDTO) {
        log.debug("Request to save Applicant : {}", applicantDTO);
        Applicant applicant = applicantMapper.toEntity(applicantDTO);
        applicant = applicantRepository.save(applicant);
        ApplicantDTO result = applicantMapper.toDto(applicant);
        applicantSearchRepository.index(applicant);
        return result;
    }

    @Override
    public ApplicantDTO update(ApplicantDTO applicantDTO) {
        log.debug("Request to update Applicant : {}", applicantDTO);
        Applicant applicant = applicantMapper.toEntity(applicantDTO);
        applicant = applicantRepository.save(applicant);
        ApplicantDTO result = applicantMapper.toDto(applicant);
        applicantSearchRepository.index(applicant);
        return result;
    }

    @Override
    public Optional<ApplicantDTO> partialUpdate(ApplicantDTO applicantDTO) {
        log.debug("Request to partially update Applicant : {}", applicantDTO);

        return applicantRepository
            .findById(applicantDTO.getId())
            .map(existingApplicant -> {
                applicantMapper.partialUpdate(existingApplicant, applicantDTO);

                return existingApplicant;
            })
            .map(applicantRepository::save)
            .map(savedApplicant -> {
                applicantSearchRepository.index(savedApplicant);
                return savedApplicant;
            })
            .map(applicantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Applicants");
        return applicantRepository.findAll(pageable).map(applicantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicantDTO> findOne(Long id) {
        log.debug("Request to get Applicant : {}", id);
        return applicantRepository.findById(id).map(applicantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Applicant : {}", id);
        applicantRepository.deleteById(id);
        applicantSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Applicants for query {}", query);
        return applicantSearchRepository.search(query, pageable).map(applicantMapper::toDto);
    }
}
