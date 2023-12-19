package ar.com.laboratoty.repositories;

import ar.com.laboratoty.models.Exam;
import ar.com.laboratoty.utils.Data;

import java.util.Arrays;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {


    @Override
    public List<Exam> findAll() {
        System.out.println("Llamando a ".concat(this.getClass().getCanonicalName()));
        return Arrays.asList(
                new Exam(1L,"Matematicas"),
                new Exam(1L,"Fisica"),
                new Exam(1L,"Sociales"),
                new Exam(1L,"Ed Fisica"),
                new Exam(1L,"Quimica"),
                new Exam(1L,"Plastica"),
                new Exam(1L,"Geografia")
                );
    }

    @Override
    public Exam save(Exam exam) {
        return Data.EXAM;
    }
}
