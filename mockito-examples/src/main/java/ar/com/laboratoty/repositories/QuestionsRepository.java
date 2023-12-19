package ar.com.laboratoty.repositories;

import java.util.List;
import java.util.Optional;

public interface QuestionsRepository {

    List<String> findQuestionsByExamId(Long id);
    Optional<String> findQuestionsById(Long id);

    void saveQuestions(List<String> questions);
}
