package net.mojloc.telegrambot.Dao;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.entities.QuizResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("quizResultsDao")
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizResultsDao {
    QuizResultsRepository repository;

    @Autowired
    public QuizResultsDao(QuizResultsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<QuizResults> findByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<QuizResults> findTopResults(int quantity) {
        return repository.findTopScoreResults(quantity);
    }

    @Transactional
    public QuizResults saveResult(QuizResults quizResults) {
        return repository.save(quizResults);
    }
}
