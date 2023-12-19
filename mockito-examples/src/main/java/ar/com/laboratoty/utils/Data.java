package ar.com.laboratoty.utils;

import ar.com.laboratoty.models.Exam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Data {

    public final  static List<String>  QUESTIONS = Arrays.asList(
            "Question 1",
            "Question 2",
            "Question 3",
            "Question 4"
            );
    public final  static List<Exam>  EXAMS = Arrays.asList(
            new Exam(1L,"Matematicas"),
            new Exam(2L,"Fisica"),
            new Exam(3L,"Sociales"),
            new Exam(4L,"Ed Fisica"),
            new Exam(5L,"Quimica"),
            new Exam(6L,"Plastica"),
            new Exam(7L,"Geografia")
    );

    public final  static List<Exam>  EXAMS_ID_NULL = Arrays.asList(
            new Exam(null,"Matematicas"),
            new Exam(null,"Fisica"),
            new Exam(null,"Sociales"),
            new Exam(null,"Ed Fisica"),
            new Exam(null,"Quimica"),
            new Exam(null,"Plastica"),
            new Exam(null,"Geografia")
    );

    public final static List<Exam> EXAM_EMPTY = Collections.emptyList();

    public final static Exam EXAM = new  Exam(null,"Matematicas", QUESTIONS);

}
