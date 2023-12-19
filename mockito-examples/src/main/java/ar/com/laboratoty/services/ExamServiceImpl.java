package ar.com.laboratoty.services;

import ar.com.laboratoty.models.Exam;
import ar.com.laboratoty.repositories.ExamRepository;
import ar.com.laboratoty.repositories.QuestionsRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    private ExamRepository examRepository;
    private QuestionsRepository questionsRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionsRepository questionsRepository) {
        this.examRepository = examRepository;
        this.questionsRepository = questionsRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return examRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();
    }

    @Override
    public Exam findExamByNameWithQuestions(String name) {
        Optional<Exam> optionalExam =  examRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();
        Exam exam = null;
        if(optionalExam.isPresent()){
            exam = optionalExam.orElseThrow();
            List<String> questions = questionsRepository.findQuestionsByExamId(optionalExam.get().getId());
            exam.setQuestions(questions);
        }
        return exam;
    }

    @Override
    public Exam save(Exam exam) {
        if(!exam.getQuestions().isEmpty()){
            questionsRepository.saveQuestions(exam.getQuestions());
        }
        return examRepository.save(exam);
    }
}
