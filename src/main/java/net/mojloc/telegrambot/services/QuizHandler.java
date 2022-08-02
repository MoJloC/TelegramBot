package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.Dao.QuestionsForQuizDao;
import net.mojloc.telegrambot.Dao.QuizResultsDao;
import net.mojloc.telegrambot.entities.QuestionsForQuiz;
import net.mojloc.telegrambot.entities.QuizResults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class QuizHandler {
    final QuizResultsDao quizResultsDao;
    final QuestionsForQuizDao questionsForQuizDao;
    final QuizResults currentUserQuizResults;
    final Long userId;
    List<QuestionsForQuiz> questionsForQuiz;
    int quizScore;
    int currentNumberOfQuestion;

    public QuizHandler(QuizResultsDao quizResultsDao, QuestionsForQuizDao questionsForQuizDao
                       , QuizResults currentUserQuizResults) {
        this.quizResultsDao = quizResultsDao;
        this.questionsForQuizDao = questionsForQuizDao;
        this.currentUserQuizResults = currentUserQuizResults;
        userId = currentUserQuizResults.getUserId();
        prepareQuestionsList();
    }

    public int getCurrentNumberOfQuestion() {
        return currentNumberOfQuestion;
    }

    public BotApiMethod<?> handleQuiz(SendMessage sendMessage, String updateId) {
        quizScore = 0;
        log.info("Update ID " + updateId + ": starting new Quiz for user with Id " + userId);

        return startQuiz(sendMessage, updateId);
    }

    public BotApiMethod<?> nextQuizQuestion(String chatId, String updateId) {
        quizScore++;

        if (questionsForQuiz.size() == 0) {
            log.warn("Update ID " + updateId + ": user with Id " + userId + " successfully completed Quiz. You should to"
                     + " think about adding some more interesting questions to the Quiz DB!");

            return finishQuiz(chatId, updateId, 1);
        }

        SendMessage nextQuizQuestion = new SendMessage();
        nextQuizQuestion.setChatId(chatId);
        QuestionsForQuiz currentQuestion = questionsForQuiz.remove(0);
        InlineKeyboardMarkup keyboardForCurrentQuestion = prepareInlineKeyboard(currentQuestion);
        nextQuizQuestion.setText(currentQuestion.getQuestion());
        nextQuizQuestion.setReplyMarkup(keyboardForCurrentQuestion);

        log.info("Update ID " + updateId + ": next question has been prepared for transmission to user with Id "
                + userId);

        return nextQuizQuestion;
    }

    private void prepareQuestionsList() {
        questionsForQuiz = questionsForQuizDao.getAllQuestionsList();
        Collections.shuffle(questionsForQuiz);
    }

    private BotApiMethod<?> startQuiz(SendMessage sendMessage, String updateId) {
        if (questionsForQuiz.size() == 0) {
            log.warn("Update ID " + updateId + ": user with Id " + userId + " has no way to start Quiz: "
                     + "List questionsForQuiz is empty!!!");
            sendMessage.setText("Мя... Что-то пошло совсем не так и я не могу нацарапать ни один вопрос. Мы обязательно "
                                + "с этим разберёмся в ближайшее время!");
        } else {
            QuestionsForQuiz currentQuestion = questionsForQuiz.remove(0);
            InlineKeyboardMarkup keyboardForCurrentQuestion = prepareInlineKeyboard(currentQuestion);
            sendMessage.setText(currentQuestion.getQuestion());
            sendMessage.setReplyMarkup(keyboardForCurrentQuestion);

            log.info("Update ID " + updateId + ": first question has been prepared for transmission to user with Id "
                     + userId);
        }

        return sendMessage;
    }

    BotApiMethod<?> finishQuiz(String chatId, String updateId, int typeOfFinish) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        String[] currentDateAndTime = LocalDateTime.now().format(formatter).split(" ");
        currentUserQuizResults.setDate(currentDateAndTime[0]);
        currentUserQuizResults.setTime(currentDateAndTime[1]);
        currentUserQuizResults.setScore(quizScore);
        quizResultsDao.saveResult(currentUserQuizResults);

        log.info("Update ID " + updateId + ": quiz results of user with Id " + userId + " is saved to the database.");

        SendMessage messageAboutEndOfTheQuiz = new SendMessage();
        messageAboutEndOfTheQuiz.setChatId(chatId);

        switch (typeOfFinish) {
            case (0):
                messageAboutEndOfTheQuiz.setText("Вы отменили Викторину. Ваш текущий результат в " + quizScore
                                                 + " сохранён в таблице результатов");
                log.info("Update ID " + updateId + ": user with Id " + userId + " canceled the Quiz. A regular notification"
                         + "has been sent.");
                break;
            case (1):
                messageAboutEndOfTheQuiz.setText("Вы правильно ответили на все вопросы Викторины! Ваш замуррррчательный "
                                                 + "результат в " + quizScore + " баллов я сохранила в таблице результатов."
                                                 + "Поздравляем и обязательно добавим ещё интересных вопросов в виторину!");
                log.info("Update ID " + updateId + ": user with Id " + userId + " successfully completed the Quiz. A "
                         + "regular notification has been sent.");
                break;
            case (2):
                messageAboutEndOfTheQuiz.setText("К сожалению, Вы дали неправильный ответ. Ваш текущий результат в "
                        + quizScore + " сохранён в таблице результатов. Жду Вас вновь на Викторине!");
                log.info("Update ID " + updateId + ": user with Id " + userId + " sent wrong answer to the question. A "
                         + "regular notification has been sent.");
                break;
        }

        QuizCommandHandler.removingFromCacheAfterQuizEnding(userId);

        return messageAboutEndOfTheQuiz;
    }

    private InlineKeyboardMarkup prepareInlineKeyboard(QuestionsForQuiz currentQuestion) {
        currentNumberOfQuestion++;
        InlineKeyboardMarkup keyboardWithQuestions = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowTwo = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowThree = new ArrayList<>();
        String rightAnswer = currentQuestion.getRanswer();
        String generalDataText = "Quiz " + userId;
        List<String> answers = currentQuestion.getListWithAnswers();

        for (int i = 0; i <answers.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(answers.get(i));
            button.setCallbackData(answers.get(i).equals(rightAnswer) ? generalDataText + " Right " + currentNumberOfQuestion
                                                                      : generalDataText + " Wrong " + currentNumberOfQuestion);

            if (i < 2) {
                inlineKeyboardButtonsRowOne.add(button);
            } else {
                inlineKeyboardButtonsRowTwo.add(button);
            }
        }

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Завершить викторину");
        button.setCallbackData(generalDataText + " Finish");
        inlineKeyboardButtonsRowThree.add(button);

        keyboardWithQuestions.setKeyboard(List.of(inlineKeyboardButtonsRowOne, inlineKeyboardButtonsRowTwo
                                                  , inlineKeyboardButtonsRowThree));

        return keyboardWithQuestions;
    }
}
