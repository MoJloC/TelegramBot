package net.mojloc.telegrambot.dao;

import net.mojloc.telegrambot.entities.QuizResults;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizResultsRepository extends CrudRepository<QuizResults, Integer> {

    List<QuizResults> findAllByUserId(Long userId);

    @Query(value = "select * from quizresults order by score desc limit :quantityOfResults", nativeQuery = true)
    List<QuizResults> findTopScoreResults(int quantityOfResults);
}
