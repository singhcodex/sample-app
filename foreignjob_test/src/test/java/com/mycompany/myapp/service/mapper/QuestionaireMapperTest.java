package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionaireMapperTest {

    private QuestionaireMapper questionaireMapper;

    @BeforeEach
    public void setUp() {
        questionaireMapper = new QuestionaireMapperImpl();
    }
}
