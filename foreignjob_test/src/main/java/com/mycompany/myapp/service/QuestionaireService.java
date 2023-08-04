package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.QuestionaireDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Questionaire}.
 */
public interface QuestionaireService {
    /**
     * Save a questionaire.
     *
     * @param questionaireDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionaireDTO save(QuestionaireDTO questionaireDTO);

    /**
     * Updates a questionaire.
     *
     * @param questionaireDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionaireDTO update(QuestionaireDTO questionaireDTO);

    /**
     * Partially updates a questionaire.
     *
     * @param questionaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionaireDTO> partialUpdate(QuestionaireDTO questionaireDTO);

    /**
     * Get all the questionaires.
     *
     * @return the list of entities.
     */
    List<QuestionaireDTO> findAll();

    /**
     * Get the "id" questionaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionaireDTO> findOne(Long id);

    /**
     * Delete the "id" questionaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the questionaire corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<QuestionaireDTO> search(String query);
}
