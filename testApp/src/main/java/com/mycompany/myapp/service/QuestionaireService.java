package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Questionaire;
import com.mycompany.myapp.repository.QuestionaireRepository;
import com.mycompany.myapp.service.dto.QuestionaireDTO;
import com.mycompany.myapp.service.mapper.QuestionaireMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Questionaire}.
 */
@Service
@Transactional
public class QuestionaireService {

    private final Logger log = LoggerFactory.getLogger(QuestionaireService.class);

    private final QuestionaireRepository questionaireRepository;

    private final QuestionaireMapper questionaireMapper;

    public QuestionaireService(QuestionaireRepository questionaireRepository, QuestionaireMapper questionaireMapper) {
        this.questionaireRepository = questionaireRepository;
        this.questionaireMapper = questionaireMapper;
    }

    /**
     * Save a questionaire.
     *
     * @param questionaireDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionaireDTO save(QuestionaireDTO questionaireDTO) {
        log.debug("Request to save Questionaire : {}", questionaireDTO);
        Questionaire questionaire = questionaireMapper.toEntity(questionaireDTO);
        questionaire = questionaireRepository.save(questionaire);
        return questionaireMapper.toDto(questionaire);
    }

    /**
     * Update a questionaire.
     *
     * @param questionaireDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionaireDTO update(QuestionaireDTO questionaireDTO) {
        log.debug("Request to update Questionaire : {}", questionaireDTO);
        Questionaire questionaire = questionaireMapper.toEntity(questionaireDTO);
        questionaire = questionaireRepository.save(questionaire);
        return questionaireMapper.toDto(questionaire);
    }

    /**
     * Partially update a questionaire.
     *
     * @param questionaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionaireDTO> partialUpdate(QuestionaireDTO questionaireDTO) {
        log.debug("Request to partially update Questionaire : {}", questionaireDTO);

        return questionaireRepository
            .findById(questionaireDTO.getId())
            .map(existingQuestionaire -> {
                questionaireMapper.partialUpdate(existingQuestionaire, questionaireDTO);

                return existingQuestionaire;
            })
            .map(questionaireRepository::save)
            .map(questionaireMapper::toDto);
    }

    /**
     * Get all the questionaires.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionaireDTO> findAll() {
        log.debug("Request to get all Questionaires");
        return questionaireRepository.findAll().stream().map(questionaireMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one questionaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionaireDTO> findOne(Long id) {
        log.debug("Request to get Questionaire : {}", id);
        return questionaireRepository.findById(id).map(questionaireMapper::toDto);
    }

    /**
     * Delete the questionaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Questionaire : {}", id);
        questionaireRepository.deleteById(id);
    }
}
