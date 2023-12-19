package ar.com.laboratoty.models;

import java.util.ArrayList;
import java.util.List;

public class Exam {

    private Long id;
    private String name;
    private List<String> questions;

    public Exam(Long id, String name) {
        this.id = id;
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public Exam(Long id, String name, List<String> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
