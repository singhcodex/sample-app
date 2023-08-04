package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Questionaire.
 */
@Entity
@Table(name = "questionaire")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "questionaire")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Questionaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quest_title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String questTitle;

    @Column(name = "options")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String options;

    @Column(name = "correct_option")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String correctOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questionaires", "jobs" }, allowSetters = true)
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Questionaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestTitle() {
        return this.questTitle;
    }

    public Questionaire questTitle(String questTitle) {
        this.setQuestTitle(questTitle);
        return this;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

    public String getOptions() {
        return this.options;
    }

    public Questionaire options(String options) {
        this.setOptions(options);
        return this;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return this.correctOption;
    }

    public Questionaire correctOption(String correctOption) {
        this.setCorrectOption(correctOption);
        return this;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Questionaire task(Task task) {
        this.setTask(task);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questionaire)) {
            return false;
        }
        return id != null && id.equals(((Questionaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questionaire{" +
            "id=" + getId() +
            ", questTitle='" + getQuestTitle() + "'" +
            ", options='" + getOptions() + "'" +
            ", correctOption='" + getCorrectOption() + "'" +
            "}";
    }
}
