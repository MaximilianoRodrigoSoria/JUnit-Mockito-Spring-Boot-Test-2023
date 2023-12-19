package ar.com.laboratoty.repositories;

import ar.com.laboratoty.utils.Data;

import java.util.List;
import java.util.Optional;

public class QuestionsRepositoryImpl implements QuestionsRepository {
    @Override
    public List<String> findQuestionsByExamId(Long id) {
        System.out.println("Llamando a ".concat(this.getClass().getCanonicalName()));
        return Data.QUESTIONS;
    }

    @Override
    public Optional<String> findQuestionsById(Long id) {
        return Optional.of("Question 1");
    }

    @Override
    public void saveQuestions(List<String> questions) {

    }
}
