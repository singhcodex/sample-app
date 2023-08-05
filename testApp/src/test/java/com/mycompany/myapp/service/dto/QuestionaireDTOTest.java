package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionaireDTO.class);
        QuestionaireDTO questionaireDTO1 = new QuestionaireDTO();
        questionaireDTO1.setId(1L);
        QuestionaireDTO questionaireDTO2 = new QuestionaireDTO();
        assertThat(questionaireDTO1).isNotEqualTo(questionaireDTO2);
        questionaireDTO2.setId(questionaireDTO1.getId());
        assertThat(questionaireDTO1).isEqualTo(questionaireDTO2);
        questionaireDTO2.setId(2L);
        assertThat(questionaireDTO1).isNotEqualTo(questionaireDTO2);
        questionaireDTO1.setId(null);
        assertThat(questionaireDTO1).isNotEqualTo(questionaireDTO2);
    }
}
