package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Questionaire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionaireDTO implements Serializable {

    private Long id;

    private String questTitle;

    private String options;

    private String correctOption;

    private TaskDTO task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestTitle() {
        return questTitle;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionaireDTO)) {
            return false;
        }

        QuestionaireDTO questionaireDTO = (QuestionaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionaireDTO{" +
            "id=" + getId() +
            ", questTitle='" + getQuestTitle() + "'" +
            ", options='" + getOptions() + "'" +
            ", correctOption='" + getCorrectOption() + "'" +
            ", task=" + getTask() +
            "}";
    }
}
