package net.mojloc.telegrambot.Dao;

import net.mojloc.telegrambot.entities.QuestionsForQuiz;
import net.mojloc.telegrambot.entities.QuizResults;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsForQuizRepository extends CrudRepository<QuestionsForQuiz, Integer> {

    @Query(value = "select * from questionsforquiz", nativeQuery = true)
    List<QuestionsForQuiz> LoadAllQuestionsToList();
}