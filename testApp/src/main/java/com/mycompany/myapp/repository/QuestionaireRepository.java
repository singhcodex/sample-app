package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Questionaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Questionaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionaireRepository extends JpaRepository<Questionaire, Long> {}
