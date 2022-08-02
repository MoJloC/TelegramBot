package net.mojloc.telegrambot.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="quizresults")
@NoArgsConstructor
public class QuizResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    int id;

    @Column(name = "user_id")
    Long userId;

    @Column(name= "user_first_name")
    String userFirstName="";

    @Column(name= "user_last_name")
    String userLastName="";

    @Column(name= "user_nickname")
    String userNickname="";

    @Column(name= "date")
    String date;

    @Column(name= "time")
    String time;

    @Column(name= "score")
    int score;

    public static String ListOfQuizResultsToString(List<QuizResults> ListOfQuizResults) {
        StringBuilder stringBuilder = new StringBuilder();

        for (QuizResults quizResults : ListOfQuizResults) {
            stringBuilder.append(quizResults.toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Score: " + getScore() + " - " + getUserFirstName() + " " + getUserLastName()
                + " with nickname (" + getUserNickname() + ") at date/time: " + getDate() + " " + getTime();
    }

}
