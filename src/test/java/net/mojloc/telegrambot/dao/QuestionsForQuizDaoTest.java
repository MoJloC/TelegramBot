package net.mojloc.telegrambot.dao;

import net.mojloc.telegrambot.entities.QuestionsForQuiz;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionsForQuizDaoTest {

    @Autowired
    private QuestionsForQuizDao questionsForQuizDao;

    @Test
    public void testQuestionsForQuizDao(){
        QuestionsForQuiz question = questionsForQuizDao.finByID(2);
        System.out.println(question.toString());
        assertEquals(2, question.getId());
    }
}