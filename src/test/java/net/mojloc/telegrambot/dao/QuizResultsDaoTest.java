package net.mojloc.telegrambot.dao;

import net.mojloc.telegrambot.entities.QuizResults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuizResultsDaoTest {

    @Autowired
    QuizResultsDao quizResultsDao;

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<QuizResults> results = quizResultsDao.findByUserId(userId);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindTopResults() {
        int quantity = 3;
        List<QuizResults> results = quizResultsDao.findTopResults(quantity);
        System.out.println(Arrays.toString(results.toArray()));
        assertEquals(3, results.size());
    }
}