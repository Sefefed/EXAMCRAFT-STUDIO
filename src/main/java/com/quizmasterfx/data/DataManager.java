package com.quizmasterfx.data;

import com.quizmasterfx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class DataManager {
    private static final ObservableList<Quiz> quizzes = FXCollections.observableArrayList();
    private static final ObservableList<QuizResult> results = FXCollections.observableArrayList();

    static {
        // Pre-populate sample data for demo
        Quiz sampleQuiz = new Quiz("Java Basics", "JAVA2024", 30);
        sampleQuiz.setDescription("Fundamentals of Java and OOP");
        sampleQuiz.addQuestion(new Question(
                "What is polymorphism?",
                Arrays.asList(
                        new Choice("Hiding data", false),
                        new Choice("Multiple forms", true),
                        new Choice("Data binding", false),
                        new Choice("Creating instances", false)
                )
        ));
        sampleQuiz.addQuestion(new Question("Encapsulation means?",
                Arrays.asList(
                        new Choice("Hiding data within a class", true),
                        new Choice("Using multiple inheritance", false),
                        new Choice("Overloading methods", false),
                        new Choice("Dynamic dispatch", false)
                )));
        sampleQuiz.addQuestion(new Question("Java is platform independent.", true));
        sampleQuiz.addQuestion(new Question("Short answer: keyword for inheritance?", "extends"));
        quizzes.add(sampleQuiz);
    }

    public static ObservableList<Quiz> getQuizzes() { return quizzes; }
    public static ObservableList<QuizResult> getResults() { return results; }

    public static void saveQuiz(Quiz quiz) { quizzes.add(quiz); }

    public static Quiz findQuizByPassword(String password) {
        return quizzes.stream()
                .filter(q -> q.getPassword() != null && q.getPassword().equals(password))
                .findFirst().orElse(null);
    }

    public static void addResult(QuizResult result) {
        results.add(result);
    }
}
