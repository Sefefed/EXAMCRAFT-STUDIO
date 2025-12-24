package com.quizmasterfx.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quizmasterfx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
        private static final ObservableList<Quiz> quizzes = FXCollections.observableArrayList();
        private static final ObservableList<QuizResult> results = FXCollections.observableArrayList();
        private static final Path QUIZ_FILE = Paths.get("quizzes.json");
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        static {
                // Try to load persisted quizzes; if none present, create a sample quiz
                if (!loadFromDisk()) {
                        Quiz sampleQuiz = new Quiz("Java Basics", "JAVA2024", 30);
                        sampleQuiz.setDescription("Fundamentals of Java and OOP");
                        sampleQuiz.addQuestion(new Question(
                                        "What is polymorphism?",
                                        List.of(
                                                        new Choice("Hiding data", false),
                                                        new Choice("Multiple forms", true),
                                                        new Choice("Data binding", false),
                                                        new Choice("Creating instances", false)
                                        )
                        ));
                        sampleQuiz.addQuestion(new Question("Encapsulation means?",
                                        List.of(
                                                        new Choice("Hiding data within a class", true),
                                                        new Choice("Using multiple inheritance", false),
                                                        new Choice("Overloading methods", false),
                                                        new Choice("Dynamic dispatch", false)
                                        )));
                        sampleQuiz.addQuestion(new Question("Java is platform independent.", true));
                        sampleQuiz.addQuestion(new Question("Short answer: keyword for inheritance?", "extends"));
                        quizzes.add(sampleQuiz);
                        saveToDisk();
                }

                // Ensure quizzes are persisted on JVM shutdown as well
                Runtime.getRuntime().addShutdownHook(new Thread(DataManager::saveToDisk));
        }

        public static ObservableList<Quiz> getQuizzes() { return quizzes; }
        public static ObservableList<QuizResult> getResults() { return results; }

        public static void saveQuiz(Quiz quiz) {
                quizzes.add(quiz);
                saveToDisk();
        }

        public static Quiz findQuizByPassword(String password) {
                return quizzes.stream()
                                .filter(q -> q.getPassword() != null && q.getPassword().equals(password))
                                .findFirst().orElse(null);
        }

        public static void addResult(QuizResult result) {
                results.add(result);
                saveToDisk();
        }

        private static boolean loadFromDisk() {
                try {
                        if (Files.exists(QUIZ_FILE)) {
                                try (Reader r = Files.newBufferedReader(QUIZ_FILE, StandardCharsets.UTF_8)) {
                                        Type listType = new TypeToken<List<Quiz>>(){}.getType();
                                        List<Quiz> list = GSON.fromJson(r, listType);
                                        if (list != null && !list.isEmpty()) {
                                                quizzes.addAll(list);
                                                return true;
                                        }
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return false;
        }

        private static void saveToDisk() {
                try (Writer w = Files.newBufferedWriter(QUIZ_FILE, StandardCharsets.UTF_8)) {
                        // write a plain ArrayList to avoid serializing ObservableList internals
                        GSON.toJson(new ArrayList<>(quizzes), w);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
