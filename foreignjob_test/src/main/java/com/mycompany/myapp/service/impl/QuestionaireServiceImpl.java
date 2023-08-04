package com.mycompany.myapp.service.impl;

import static org.springframework.data.elasticsearch.client.elc.QueryBuilders.*;

import com.mycompany.myapp.domain.Questionaire;
import com.mycompany.myapp.repository.QuestionaireRepository;
import com.mycompany.myapp.repository.search.QuestionaireSearchRepository;
import com.mycompany.myapp.service.QuestionaireService;
import com.mycompany.myapp.service.dto.QuestionaireDTO;
import com.mycompany.myapp.service.mapper.QuestionaireMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Questionaire}.
 */
@Service
@Transactional
public class QuestionaireServiceImpl implements QuestionaireService {

    private final Logger log = LoggerFactory.getLogger(QuestionaireServiceImpl.class);

    private final QuestionaireRepository questionaireRepository;

    private final QuestionaireMapper questionaireMapper;

    private final QuestionaireSearchRepository questionaireSearchRepository;

    public QuestionaireServiceImpl(
        QuestionaireRepository questionaireRepository,
        QuestionaireMapper questionaireMapper,
        QuestionaireSearchRepository questionaireSearchRepository
    ) {
        this.questionaireRepository = questionaireRepository;
        this.questionaireMapper = questionaireMapper;
        this.questionaireSearchRepository = questionaireSearchRepository;
    }

    @Override
    public QuestionaireDTO save(QuestionaireDTO questionaireDTO) {
        log.debug("Request to save Questionaire : {}", questionaireDTO);
        Questionaire questionaire = questionaireMapper.toEntity(questionaireDTO);
        questionaire = questionaireRepository.save(questionaire);
        QuestionaireDTO result = questionaireMapper.toDto(questionaire);
        questionaireSearchRepository.index(questionaire);
        return result;
    }

    @Override
    public QuestionaireDTO update(QuestionaireDTO questionaireDTO) {
        log.debug("Request to update Questionaire : {}", questionaireDTO);
        Questionaire questionaire = questionaireMapper.toEntity(questionaireDTO);
        questionaire = questionaireRepository.save(questionaire);
        QuestionaireDTO result = questionaireMapper.toDto(questionaire);
        questionaireSearchRepository.index(questionaire);
        return result;
    }

    @Override
    public Optional<QuestionaireDTO> partialUpdate(QuestionaireDTO questionaireDTO) {
        log.debug("Request to partially update Questionaire : {}", questionaireDTO);

        return questionaireRepository
            .findById(questionaireDTO.getId())
            .map(existingQuestionaire -> {
                questionaireMapper.partialUpdate(existingQuestionaire, questionaireDTO);

                return existingQuestionaire;
            })
            .map(questionaireRepository::save)
            .map(savedQuestionaire -> {
                questionaireSearchRepository.index(savedQuestionaire);
                return savedQuestionaire;
            })
            .map(questionaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionaireDTO> findAll() {
        log.debug("Request to get all Questionaires");
        return questionaireRepository.findAll().stream().map(questionaireMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionaireDTO> findOne(Long id) {
        log.debug("Request to get Questionaire : {}", id);
        return questionaireRepository.findById(id).map(questionaireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Questionaire : {}", id);
        questionaireRepository.deleteById(id);
        questionaireSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionaireDTO> search(String query) {
        log.debug("Request to search Questionaires for query {}", query);
        try {
            return StreamSupport
                .stream(questionaireSearchRepository.search(query).spliterator(), false)
                .map(questionaireMapper::toDto)
                .toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
