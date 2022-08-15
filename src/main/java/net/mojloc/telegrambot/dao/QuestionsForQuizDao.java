package net.mojloc.telegrambot.dao;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.entities.QuestionsForQuiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("questionsForQuizDao")
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionsForQuizDao {
    QuestionsForQuizRepository repository;

    @Autowired
    public QuestionsForQuizDao(QuestionsForQuizRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public QuestionsForQuiz finByID(int id) {
        QuestionsForQuiz question = repository.findById(id).get();

        return question;
    }

    @Transactional(readOnly = true)
    public List<QuestionsForQuiz> getAllQuestionsList() {
        return repository.LoadAllQuestionsToList();
    }
}
