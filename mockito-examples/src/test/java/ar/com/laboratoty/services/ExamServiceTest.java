package ar.com.laboratoty.services;

import ar.com.laboratoty.models.Exam;
import ar.com.laboratoty.repositories.ExamRepositoryImpl;
import ar.com.laboratoty.repositories.ExamRepository;
import ar.com.laboratoty.repositories.QuestionsRepository;
import ar.com.laboratoty.repositories.QuestionsRepositoryImpl;
import ar.com.laboratoty.utils.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    @Mock
    ExamRepository repository;
    @Mock
    QuestionsRepository questionsRepository;

//    @Mock
//    ExamRepository repository2;
//    @Mock
//    QuestionsRepository questionsRepository2;
    @InjectMocks
ExamServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;
    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        /*this.repository = mock(IExamRepository.class);
        this.questionsRepository = mock(IQuestionsRepository.class);
        this.service = new ExamService(repository, questionsRepository);*/
    }
    @Test
    void testNumberInvocations4() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        service.findExamByNameWithQuestions("Quimica");

        verify(questionsRepository).findQuestionsByExamId(5L);
        verify(questionsRepository, times(1)).findQuestionsByExamId(5L);
        verify(questionsRepository, atLeast(1)).findQuestionsByExamId(5L);
        verify(questionsRepository, atLeastOnce()).findQuestionsByExamId(5L);
        verify(questionsRepository, atMost(1)).findQuestionsByExamId(5L);
        verify(questionsRepository, atMostOnce()).findQuestionsByExamId(5L);
    }

    @Test
    void testNumberInvocations2() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        service.findExamByNameWithQuestions("Quimica");

//        verify(preguntaRepository).findPreguntasPorExamenId(5L); falla
//        verify(questionsRepository, times(2)).findQuestionsByExamId(5L);
//        verify(questionsRepository, atLeast(2)).findQuestionsByExamId(5L);
//        verify(questionsRepository, atLeastOnce()).findQuestionsByExamId(5L);
//        verify(questionsRepository, atMost(20)).findQuestionsByExamId(5L);
//        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L); falla
    }

    @Test
    void testNumberInvocations3() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        service.findExamByNameWithQuestions("Matemáticas");

        verify(questionsRepository, never()).findQuestionsByExamId(5L);
        verifyNoInteractions(questionsRepository);

        verify(repository).findAll();
        verify(repository, times(1)).findAll();
        verify(repository, atLeast(1)).findAll();
        verify(repository, atLeastOnce()).findAll();
        verify(repository, atMost(10)).findAll();
        verify(repository, atMostOnce()).findAll();
    }


    @Test
    @DisplayName("TEST findByName WHEN find exam THEN status OK")
    void findExamByNameTest() {
        //Assemble
        when(repository.findAll()).thenReturn(Data.EXAMS);

        //Act
       Optional<Exam> exam = service.findExamByName("Matematicas");

       //Assert
        assertAll(
            ()->assertTrue(exam.isPresent()),
            ()->assertEquals(1L,exam.orElseThrow().getId()),
            ()->assertEquals("Matematicas",exam.orElseThrow().getName())
        );
    }

    @Test
    @DisplayName("TEST findByName WHEN empty list THEN status OK")
    void findExamByNameEmptyListTest() {
        //Assemble
        List<Exam> datos = Collections.emptyList();
        when(repository.findAll()).thenReturn(datos);

        //Act
        Optional<Exam> exam = service.findExamByName("Matematicas");

        //Assert
        assertFalse(exam.isPresent());
    }

    @Test
    @DisplayName("TEST findQuestions WHEN find exam THEN status OK")
    void findExamWithQuestions() {
        //Assemble
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionsRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        //Act
        Exam exam = service.findExamByNameWithQuestions("Matematicas");

        //Assert
        assertAll(
                ()->assertNotNull(exam),
                ()->assertEquals(1L,exam.getId()),
                ()->assertEquals("Matematicas",exam.getName()),
                ()->assertNotNull(exam.getQuestions()),
                ()->assertEquals(4, exam.getQuestions().size()),
                ()->assertTrue(exam.getQuestions().contains("Question 1"))
        );
    }
    @Test
    @DisplayName("TEST findQuestions WHEN find exam THEN status OK")
    void findExamWithQuestionsVerify() {
        //Assemble
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionsRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        //Act
        Exam exam = service.findExamByNameWithQuestions("Matematicas");

        //Assert
        assertAll(
                ()->assertNotNull(exam),
                ()->assertEquals(1L,exam.getId()),
                ()->assertEquals("Matematicas",exam.getName()),
                ()->assertNotNull(exam.getQuestions()),
                ()->assertEquals(4, exam.getQuestions().size()),
                ()->assertTrue(exam.getQuestions().contains("Question 1"))
        );

        verify(repository).findAll();
        verify(questionsRepository).findQuestionsByExamId(anyLong());
    }

    @Test
    @DisplayName("TEST findExamWithQuest WHEN not exist THEN status OK")
    void findNotExistExamVerify() {
        //Assemble
        when(repository.findAll()).thenReturn(Data.EXAM_EMPTY);
        //when(questionsRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        //Act
        Exam exam = service.findExamByNameWithQuestions("Matematicas");

        //Assert
        assertNull(exam);

        verify(repository).findAll();

    }

    @Test
    @DisplayName("TEST findExamWithQuest WHEN not exist THEN status OK")
    void testInsertExam() {
        //Assemble
        when(repository.save(any(Exam.class))).then(new Answer<Exam>(){
            Long secuence = 8L;
            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(secuence++);
                return exam;
            }
        });

        //Act
        Exam exam = service.save(Data.EXAM);

        //Assert
        assertNotNull(exam);

        verify(repository).save(any());
        verify(questionsRepository).saveQuestions(any());

    }

    @Test
    void TestManagementException() {

        //Assemble
        when(repository.findAll()).thenReturn(Data.EXAMS_ID_NULL);
        when(questionsRepository.findQuestionsByExamId(isNull())).thenThrow(IllegalArgumentException.class);

        //ACT
        var exception= assertThrows(IllegalArgumentException.class, () -> {service.findExamByNameWithQuestions("Matematicas");});

        //Assert
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repository).findAll();
        verify(questionsRepository).findQuestionsByExamId(isNull());
    }


    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionsRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Matematicas");


        verify(repository).findAll();
        verify(questionsRepository).findQuestionsByExamId(argThat(new MyArgMatchers()));

    }

    @Test
    void testArgumentMatchers2() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionsRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Matematicas");


        verify(repository).findAll();
        verify(questionsRepository).findQuestionsByExamId(argThat(arg -> arg.equals(1L)));

    }

    @Test
    void testCaptureArgumentMatchers() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionsRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findExamByNameWithQuestions("Matematicas");
        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        verify(repository).findAll();
        verify(questionsRepository).findQuestionsByExamId(captor.capture());

        assertEquals(1L, captor.getValue());

    }


    @Test
    void testDoThrow() {
        doThrow(IllegalArgumentException.class).when(questionsRepository).saveQuestions(anyList());
        var exam = assertThrows(IllegalArgumentException.class, ()-> service.save(Data.EXAM));
        assertEquals(IllegalArgumentException.class, exam.getClass());
    }

    @Test
    void testDOAnswer() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        //when(questionsRepository.findQuestionsByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 5 ?Data.QUESTIONS: null;
        }).when(questionsRepository).findQuestionsByExamId(anyLong());

        var exam = service.findExamByNameWithQuestions("Quimica");

        assertEquals(5L, exam.getId());
        assertEquals("Quimica", exam.getName());

        verify(questionsRepository).findQuestionsByExamId(anyLong());
    }

    @Test
    void testDoCallRealMethod() {
        //when(repository.findAll()).thenReturn(Data.EXAMS);
        //doCallRealMethod().when(questionsRepository2).findQuestionsByExamId(anyLong());

        Exam exam = service.findExamByNameWithQuestions("Matematicas");
        //assertEquals(1L, exam.getId());
        //assertEquals("Matematicas", exam.getName());
    }

    @Test
    void testSpy(){
        ExamRepository repository = spy(ExamRepositoryImpl.class);
        QuestionsRepository questionsRepository = spy(QuestionsRepositoryImpl.class);
        ExamService examService = new ExamServiceImpl(repository,questionsRepository);

        Exam exam = examService.findExamByNameWithQuestions("Matematicas");


        List<String> questions = Arrays.asList("Question 1");
        //doReturn(questions).when(questionsRepository).findQuestionsByExamId(anyLong());


        assertEquals(1L, exam.getId());
        assertEquals("Matematicas",exam.getName());
        assertEquals(4,exam.getQuestions().size());


    }

    public static class  MyArgMatchers implements ArgumentMatcher<Long>{
        private Long argument;
        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument!=null && argument>0;
        }

        @Override
        public String toString(){
            return  "Esto es un error personalizado";
        }
    }
}
