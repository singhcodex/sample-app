package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QuestionaireRepository;
import com.mycompany.myapp.service.QuestionaireService;
import com.mycompany.myapp.service.dto.QuestionaireDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Questionaire}.
 */
@RestController
@RequestMapping("/api")
public class QuestionaireResource {

    private final Logger log = LoggerFactory.getLogger(QuestionaireResource.class);

    private static final String ENTITY_NAME = "questionaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionaireService questionaireService;

    private final QuestionaireRepository questionaireRepository;

    public QuestionaireResource(QuestionaireService questionaireService, QuestionaireRepository questionaireRepository) {
        this.questionaireService = questionaireService;
        this.questionaireRepository = questionaireRepository;
    }

    /**
     * {@code POST  /questionaires} : Create a new questionaire.
     *
     * @param questionaireDTO the questionaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionaireDTO, or with status {@code 400 (Bad Request)} if the questionaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questionaires")
    public ResponseEntity<QuestionaireDTO> createQuestionaire(@RequestBody QuestionaireDTO questionaireDTO) throws URISyntaxException {
        log.debug("REST request to save Questionaire : {}", questionaireDTO);
        if (questionaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionaireDTO result = questionaireService.save(questionaireDTO);
        return ResponseEntity
            .created(new URI("/api/questionaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questionaires/:id} : Updates an existing questionaire.
     *
     * @param id the id of the questionaireDTO to save.
     * @param questionaireDTO the questionaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionaireDTO,
     * or with status {@code 400 (Bad Request)} if the questionaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questionaires/{id}")
    public ResponseEntity<QuestionaireDTO> updateQuestionaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionaireDTO questionaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Questionaire : {}, {}", id, questionaireDTO);
        if (questionaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionaireDTO result = questionaireService.update(questionaireDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questionaires/:id} : Partial updates given fields of an existing questionaire, field will ignore if it is null
     *
     * @param id the id of the questionaireDTO to save.
     * @param questionaireDTO the questionaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionaireDTO,
     * or with status {@code 400 (Bad Request)} if the questionaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questionaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionaireDTO> partialUpdateQuestionaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionaireDTO questionaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Questionaire partially : {}, {}", id, questionaireDTO);
        if (questionaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionaireDTO> result = questionaireService.partialUpdate(questionaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questionaires} : get all the questionaires.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionaires in body.
     */
    @GetMapping("/questionaires")
    public List<QuestionaireDTO> getAllQuestionaires() {
        log.debug("REST request to get all Questionaires");
        return questionaireService.findAll();
    }

    /**
     * {@code GET  /questionaires/:id} : get the "id" questionaire.
     *
     * @param id the id of the questionaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questionaires/{id}")
    public ResponseEntity<QuestionaireDTO> getQuestionaire(@PathVariable Long id) {
        log.debug("REST request to get Questionaire : {}", id);
        Optional<QuestionaireDTO> questionaireDTO = questionaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionaireDTO);
    }

    /**
     * {@code DELETE  /questionaires/:id} : delete the "id" questionaire.
     *
     * @param id the id of the questionaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questionaires/{id}")
    public ResponseEntity<Void> deleteQuestionaire(@PathVariable Long id) {
        log.debug("REST request to delete Questionaire : {}", id);
        questionaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
