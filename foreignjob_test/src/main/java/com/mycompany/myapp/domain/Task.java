package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Task entity.\n@author The JHipster team.
 */
@Entity
@Table(name = "task")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "task")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "marks")
    private Long marks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "task" }, allowSetters = true)
    private Set<Questionaire> questionaires = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tasks")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "tasks", "applicants", "company" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Task title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Task description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return this.duration;
    }

    public Task duration(Long duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getMarks() {
        return this.marks;
    }

    public Task marks(Long marks) {
        this.setMarks(marks);
        return this;
    }

    public void setMarks(Long marks) {
        this.marks = marks;
    }

    public Set<Questionaire> getQuestionaires() {
        return this.questionaires;
    }

    public void setQuestionaires(Set<Questionaire> questionaires) {
        if (this.questionaires != null) {
            this.questionaires.forEach(i -> i.setTask(null));
        }
        if (questionaires != null) {
            questionaires.forEach(i -> i.setTask(this));
        }
        this.questionaires = questionaires;
    }

    public Task questionaires(Set<Questionaire> questionaires) {
        this.setQuestionaires(questionaires);
        return this;
    }

    public Task addQuestionaire(Questionaire questionaire) {
        this.questionaires.add(questionaire);
        questionaire.setTask(this);
        return this;
    }

    public Task removeQuestionaire(Questionaire questionaire) {
        this.questionaires.remove(questionaire);
        questionaire.setTask(null);
        return this;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.removeTask(this));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.addTask(this));
        }
        this.jobs = jobs;
    }

    public Task jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Task addJob(Job job) {
        this.jobs.add(job);
        job.getTasks().add(this);
        return this;
    }

    public Task removeJob(Job job) {
        this.jobs.remove(job);
        job.getTasks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", duration=" + getDuration() +
            ", marks=" + getMarks() +
            "}";
    }
}
