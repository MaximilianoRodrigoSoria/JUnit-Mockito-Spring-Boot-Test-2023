package ar.com.laboratoty.repositories;

import ar.com.laboratoty.models.Exam;

import java.util.List;

public interface ExamRepository {
    List<Exam> findAll();

    Exam save(Exam exam);
}
