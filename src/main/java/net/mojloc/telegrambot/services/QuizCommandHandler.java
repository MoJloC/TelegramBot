package net.mojloc.telegrambot.services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.mojloc.telegrambot.Dao.QuestionsForQuizDao;
import net.mojloc.telegrambot.Dao.QuizResultsDao;
import net.mojloc.telegrambot.entities.QuizResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("quizCommandHandler")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Slf4j
public class QuizCommandHandler extends CommandHandler{
    final MessageParser messageParser;
    final QuizResultsDao quizResultsDao;
    final QuestionsForQuizDao questionsForQuizDao;

    final String command = Commands.QUIZ.getCommand();

    static Map<Long, QuizHandler> cacheForQuizHandlers = new HashMap<>();

    @Autowired
    public QuizCommandHandler(MessageParser messageParser, QuizResultsDao quizResultsDao
                              , QuestionsForQuizDao questionsForQuizDao) {
        this.messageParser = messageParser;
        this.quizResultsDao = quizResultsDao;
        this.questionsForQuizDao = questionsForQuizDao;
    }

    static void removingFromCacheAfterQuizEnding (Long userId) {
        cacheForQuizHandlers.remove(userId);
    }

    private QuizHandler setQuizHandler(Message incomingMessage) {
        Long currentUserId = incomingMessage.getFrom().getId();
        QuizResults currentUserQuizResults = new QuizResults();
        currentUserQuizResults.setUserId(currentUserId);
        currentUserQuizResults.setUserFirstName(incomingMessage.getFrom().getFirstName());
        currentUserQuizResults.setUserLastName(incomingMessage.getFrom().getLastName());
        currentUserQuizResults.setUserNickname(incomingMessage.getFrom().getUserName());

        QuizHandler currentUserQuizHandler = new QuizHandler(quizResultsDao, questionsForQuizDao, currentUserQuizResults);

        cacheForQuizHandlers.put(currentUserId, currentUserQuizHandler);

        return currentUserQuizHandler;
    }

    private QuizHandler getQuizHandler(Long userId) {
        return cacheForQuizHandlers.get(userId);
    }

    @Override
    public BotApiMethod<?> commandHandler(Message incomingMessage, String updateId) {
        strokeCount = 1;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(incomingMessage.getChatId());
        String responseMessage = "";
        List<String> attributes = messageParser.searchForCommandAttributes(incomingMessage.getText(), command, 1);

        if (attributes.size()==0) {
            if (cacheForQuizHandlers.containsKey(incomingMessage.getFrom().getId())) {
                responseMessage = "Вы уже принимаете участие в викторине. Участие более чем в одной викторине одновременно"
                        + " не допускается";
                sendMessage.setText(responseMessage);

                log.info("Update ID " + updateId + ": message text contains the command /quiz from user "
                        + incomingMessage.getFrom().getFirstName()
                        + " " + incomingMessage.getFrom().getLastName()
                        + " (user_id: " + incomingMessage.getFrom().getId() + ") "
                        + ". But user always has active quiz. A regular response has been sent");

                return sendMessage;
            }

            QuizHandler currentUserQuizHandler = setQuizHandler(incomingMessage);

            log.info("Update ID " + updateId + ": message text contains the command /quiz from user "
                    + incomingMessage.getFrom().getFirstName()
                    + " " + incomingMessage.getFrom().getLastName()
                    + " (user_id: " + incomingMessage.getFrom().getId() + ").");

            return currentUserQuizHandler.handleQuiz(sendMessage, updateId);
        }

        String commandAttribute = attributes.get(0);

        if (commandAttribute.equals("help")) {
            responseMessage = "Для начала игры наберите: /quiz\n"
                              + "Для просмотра информации по дополнительным возможностям наберите: /quiz help\n"
                              + "Для просмотра всех своих результатов наберите: /quiz 0\n"
                              + "Для просмотра лучших результатов по всем игрокам наберите: /quiz x, где x - любая цифра, кроме 0\n";
            sendMessage.setText(responseMessage);

            log.info("Update ID " + updateId + ": message text contains the command /quiz with attribute \"help\" from user "
                    + incomingMessage.getFrom().getFirstName()
                    + " " + incomingMessage.getFrom().getLastName()
                    + " (user_id: " + incomingMessage.getFrom().getId() + "). A regular response has been sent");
            
            return sendMessage;
        }

        try {
            int numericCommandAttribute = Integer.parseInt(commandAttribute.replaceAll("\\D+",""));

            if (numericCommandAttribute==0) {
                List<QuizResults> resultsList = quizResultsDao.findByUserId(incomingMessage.getFrom().getId());

                if(resultsList.size()==0) {
                    responseMessage = "Записи результатов Вашего участия в викторине в базе данных не найдены. "
                                      + "Давайте поиграем и они там обязательно появятся!";
                    sendMessage.setText(responseMessage);

                    log.info("Update ID " + updateId + ": message text contains the command /quiz with attribute \"0\" from user "
                            + incomingMessage.getFrom().getFirstName()
                            + " " + incomingMessage.getFrom().getLastName()
                            + " (user_id: " + incomingMessage.getFrom().getId() + "). No entries found. A regular response has been sent");

                    return sendMessage;
                }

                responseMessage = QuizResults.ListOfQuizResultsToString(resultsList);
                sendMessage.setText(responseMessage);

                log.info("Update ID " + updateId + ": message text contains the command /quiz with attribute \"0\" from user "
                        + incomingMessage.getFrom().getFirstName()
                        + " " + incomingMessage.getFrom().getLastName()
                        + " (user_id: " + incomingMessage.getFrom().getId() + "). A regular response has been sent");

                return sendMessage;
            } else {
                List<QuizResults> resultsList = quizResultsDao.findTopResults(numericCommandAttribute);
                responseMessage = QuizResults.ListOfQuizResultsToString(resultsList);
                sendMessage.setText(responseMessage);

                log.info("Update ID " + updateId + ": message text contains the command /quiz with attribute \""
                        + numericCommandAttribute
                        +"\" from user "
                        + incomingMessage.getFrom().getFirstName()
                        + " " + incomingMessage.getFrom().getLastName()
                        + " (user_id: " + incomingMessage.getFrom().getId() + "). A regular response has been sent");

                return sendMessage;
            }

        } catch (NumberFormatException e) {
            responseMessage = "Вы ввели не поддерживаемый вариант аттрибута команды /quiz.\n"
                               + "Для просмотра всех своих результатов наберите: /quiz 0\n"
                               + "Для просмотра лучших результатов по всем игрокам наберите: /quiz x, где x - любая цифра, кроме 0\n";
            sendMessage.setText(responseMessage);

            log.info("Update ID " + updateId + ": message text contains the command /quiz with illegal attribute from user "
                    + incomingMessage.getFrom().getFirstName()
                    + " " + incomingMessage.getFrom().getLastName()
                    + " (user_id: " + incomingMessage.getFrom().getId() + "). A regular response has been sent");

            return sendMessage;
        }
    }

    public BotApiMethod<?> callbackQueryHandler(CallbackQuery callbackQuery, String updateId) {
        Long userId = callbackQuery.getFrom().getId();
        String chatId = String.valueOf(callbackQuery.getMessage().getChatId());
        QuizHandler currentUserQuizHandler = getQuizHandler(userId);

        if (currentUserQuizHandler==null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Мяф... Вы ещё не принимаете участия в Викторине. Наберите, пожалуйста, /quiz для старта ");

            log.info("Update ID " + updateId + ": user with Id " + userId + " try to use old messages of closed Quiz. "
                    + "A regular response has been sent.");

            return sendMessage;
        }

        String[] callbackQueryDataArray = callbackQuery.getData().split(" ");
        String answerStatus = callbackQueryDataArray[2];
        int numberOfQuestionInAnswer = Integer.parseInt(callbackQueryDataArray[3]);
        int numberOfCurrentQuestion = currentUserQuizHandler.getCurrentNumberOfQuestion();

        if (answerStatus.equals("Right")) {
            if (!(numberOfQuestionInAnswer==numberOfCurrentQuestion)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Вы отправили ответ на один из прошлых вопросов. Отправьте ответ на текущий, пожалуйста!");

                log.info("Update ID " + updateId + ": user with Id " + userId + " sent an answer to one of the previous "
                        + "questions. A regular response has been sent");

                return sendMessage;
            }

            log.info("Update ID " + updateId + ": the answer from the user with Id " + userId + " for Quiz question "
                     + "is Right");
            return currentUserQuizHandler.nextQuizQuestion(chatId, updateId);
        } else if (answerStatus.equals("Wrong")) {
            if (!(numberOfQuestionInAnswer==numberOfCurrentQuestion)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Вы отправили ответ на один из прошлых вопросов. Отправьте ответ на текущий, пожалуйста!");

                log.info("Update ID " + updateId + ": user with Id " + userId + " sent an answer to one of the previous "
                        + "questions. A regular response has been sent");

                return sendMessage;
            }

            log.info("Update ID " + updateId + ": the answer from the user with Id " + userId + " for Quiz question "
                    + "is Wrong");
            return currentUserQuizHandler.finishQuiz(chatId, updateId, 2);
        } else if (answerStatus.equals("Finish")) {
            log.info("Update ID " + updateId + ": the answer from the user with Id " + userId + " for Quiz question "
                    + "contains option Finish");
            return currentUserQuizHandler.finishQuiz(chatId, updateId, 0);
        } else {
         SendMessage sendMessage = new SendMessage();
         sendMessage.setChatId(chatId);
         sendMessage.setText("Мя... Что-то пошло совсем не так. Мы обязательно с этим разберёмся в ближайшее время!");

         log.warn("Update ID " + updateId + ": can't handle CallbackQuery data at callbackQueryHandler. You should fix it!");

         return sendMessage;
        }
    }
}
