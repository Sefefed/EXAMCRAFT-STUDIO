package com.quizmasterfx.controller;

import com.quizmasterfx.SceneRouter;
import com.quizmasterfx.data.DataManager;
import com.quizmasterfx.model.Choice;
import com.quizmasterfx.model.Question;
import com.quizmasterfx.model.QuestionType;
import com.quizmasterfx.model.QuizResult;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class QuizController {
    @FXML private Label quizTitleLabel;
    @FXML private Label timerLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Label questionText;
    @FXML private VBox answersBox;
    @FXML private Button prevBtn;
    @FXML private Button flagBtn;
    @FXML private Button nextBtn;
    @FXML private FlowPane bubbleContainer;

    private com.quizmasterfx.model.Quiz quiz;
    private int currentIndex = 0;
    private final List<Boolean> answered = new ArrayList<>();
    private final List<String> shortAnswers = new ArrayList<>();
    private final List<Integer> selectedChoiceIndex = new ArrayList<>();

    private int remainingSeconds;
    private Timeline timer;

    @FXML
    public void initialize() {
        this.quiz = QuizContext.quiz;
        if (quiz == null) {
            SceneRouter.switchTo("/view/StudentLogin.fxml");
            return;
        }
        quizTitleLabel.setText("QUIZ: " + quiz.getTitle());
        remainingSeconds = quiz.getTimeLimitMinutes() * 60;
        int n = quiz.getQuestions().size();
        for (int i = 0; i < n; i++) { answered.add(false); selectedChoiceIndex.add(-1); shortAnswers.add(""); }
        setupBubbles();
        renderQuestion();
        startTimer();
    }

    private void setupBubbles() {
        bubbleContainer.getChildren().clear();
        bubbleContainer.setHgap(8);
        bubbleContainer.setVgap(8);
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            int idx = i;
            Circle c = new Circle(12);
            c.setFill(Color.LIGHTGRAY);
            c.setOnMouseEntered(e -> c.setScaleX(1.2));
            c.setOnMouseExited(e -> c.setScaleX(1.0));
            c.setOnMouseClicked(e -> {
                currentIndex = idx;
                renderQuestion();
            });
            bubbleContainer.getChildren().add(c);
        }
        updateBubbles();
    }

    private void updateBubbles() {
        for (int i = 0; i < bubbleContainer.getChildren().size(); i++) {
            Circle c = (Circle) bubbleContainer.getChildren().get(i);
            if (i == currentIndex) {
                c.setFill(Color.DODGERBLUE);
            } else if (answered.get(i)) {
                c.setFill(Color.DARKSEAGREEN);
            } else {
                c.setFill(Color.LIGHTGRAY);
            }
        }
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            remainingSeconds--;
            updateTimerLabel();
            if (remainingSeconds <= 0) {
                timer.stop();
                submitQuiz();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        updateTimerLabel();
    }

    private void updateTimerLabel() {
        int m = remainingSeconds / 60;
        int s = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", m, s));
        if (remainingSeconds <= 60) {
            timerLabel.setStyle("-fx-text-fill: #ff3b30; -fx-font-weight: 700;");
        } else {
            timerLabel.setStyle("");
        }
    }

    private void renderQuestion() {
        Question q = quiz.getQuestions().get(currentIndex);
        questionText.setText((currentIndex + 1) + ". " + q.getText());
        answersBox.getChildren().clear();
        answersBox.setSpacing(10);
        answersBox.setPadding(new Insets(10));

        switch (q.getType()) {
            case MULTIPLE_CHOICE -> renderMultipleChoice(q);
            case TRUE_FALSE -> renderMultipleChoice(q);
            case SHORT_ANSWER -> renderShortAnswer(q);
        }
        updateProgress();
        updateBubbles();
        prevBtn.setDisable(currentIndex == 0);
        nextBtn.setText(currentIndex == quiz.getQuestions().size() - 1 ? "Submit" : "Next →");
        prevBtn.setOnAction(e -> { if (currentIndex > 0) { currentIndex--; renderQuestion(); } });
        nextBtn.setOnAction(e -> {
            if (currentIndex < quiz.getQuestions().size() - 1) {
                currentIndex++;
                renderQuestion();
            } else {
                submitQuiz();
            }
        });
    }

    private void renderMultipleChoice(Question q) {
        ToggleGroup group = new ToggleGroup();
        for (int i = 0; i < q.getChoices().size(); i++) {
            Choice choice = q.getChoices().get(i);
            RadioButton rb = new RadioButton(choice.getText());
            rb.setToggleGroup(group);
            int idx = i;
            rb.setSelected(selectedChoiceIndex.get(currentIndex) == idx);
            rb.setOnAction(e -> {
                selectedChoiceIndex.set(currentIndex, idx);
                answered.set(currentIndex, true);
                updateBubbles();
                updateProgress();
            });
            answersBox.getChildren().add(rb);
        }
    }

    private void renderShortAnswer(Question q) {
        TextField tf = new TextField();
        tf.setPromptText("Type your answer...");
        tf.setText(shortAnswers.get(currentIndex));
        tf.textProperty().addListener((obs, old, val) -> {
            shortAnswers.set(currentIndex, val);
            answered.set(currentIndex, val != null && !val.isBlank());
            updateBubbles();
            updateProgress();
        });
        answersBox.getChildren().add(tf);
    }

    private void updateProgress() {
        long count = answered.stream().filter(b -> b).count();
        double progress = quiz.getQuestions().isEmpty() ? 0 : (double) count / quiz.getQuestions().size();
        progressBar.setProgress(progress);
    }

    private void submitQuiz() {
        if (timer != null) timer.stop();
        int correct = 0;
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question q = quiz.getQuestions().get(i);
            if (q.getType() == QuestionType.SHORT_ANSWER) {
                String ans = shortAnswers.get(i);
                if (ans != null && q.getShortAnswerCorrect() != null && ans.trim().equalsIgnoreCase(q.getShortAnswerCorrect().trim())) {
                    correct++;
                }
            } else {
                int sel = selectedChoiceIndex.get(i);
                if (sel >= 0 && q.getChoices().get(sel).isCorrect()) correct++;
            }
        }
        int total = quiz.getQuestions().size();
        int timeSpent = quiz.getTimeLimitMinutes() * 60 - remainingSeconds;
        String timeTaken = String.format("%02d:%02d", timeSpent / 60, timeSpent % 60);
        DataManager.addResult(new QuizResult(quiz.getTitle(), QuizContext.studentName, QuizContext.studentId, correct, total, timeTaken));
        ResultsContext.correct = correct;
        ResultsContext.total = total;
        ResultsContext.timeTaken = timeTaken;
        ResultsContext.quizTitle = quiz.getTitle();
        SceneRouter.switchTo("/view/ResultsScreen.fxml");
    }
}

class ResultsContext {
    static String quizTitle;
    static int correct;
    static int total;
    static String timeTaken;
}
