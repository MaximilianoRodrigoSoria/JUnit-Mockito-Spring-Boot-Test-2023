package ar.com.laboratoty.services;

import ar.com.laboratoty.models.Exam;

import java.util.Optional;

public interface ExamService {
    Optional<Exam> findExamByName(String name);
    Exam findExamByNameWithQuestions(String name);

    Exam save(Exam exam);


}
