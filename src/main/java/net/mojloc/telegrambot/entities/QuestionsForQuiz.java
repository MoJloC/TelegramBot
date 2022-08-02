package net.mojloc.telegrambot.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="questionsforquiz")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionsForQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    int id;

    @Column(name = "question")
    String question;

    @Column(name = "ranswer")
    String ranswer;

    @Column(name = "wanswer1")
    String wanswer1;

    @Column(name = "wanswer2")
    String wanswer2;

    @Column(name = "wanswer3")
    String wanswer3;

    @Override
    public String toString() {
        return "Вопрос: " + getQuestion() + "\n" + "Варианты ответов:\n"
                + "А. " + getRanswer() + "\n"
                + "Б. " + getWanswer1() + "\n"
                + "В. " + getWanswer2() + "\n"
                + "Г. " + getWanswer3() + "\n";
    }

    public List<String> getListWithAnswers() {
        List<String> answers = new ArrayList<>();
        answers.add(getRanswer());
        answers.add(getWanswer1());
        answers.add(getWanswer2());
        answers.add(getWanswer3());
        Collections.shuffle(answers);

        return answers;
    }
}
