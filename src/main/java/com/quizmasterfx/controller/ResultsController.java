package com.quizmasterfx.controller;

import com.quizmasterfx.SceneRouter;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Random;

public class ResultsController {
    @FXML private Label titleLabel;
    @FXML private Label scoreLabel;
    @FXML private Label timeLabel;
    @FXML private Button reviewBtn;
    @FXML private Button backBtn;
    @FXML private AnchorPane confettiPane;

    @FXML
    public void initialize() {
        titleLabel.setText("🎉 QUIZ COMPLETED!");
        scoreLabel.setText(ResultsContext.correct + "/" + ResultsContext.total + " (" + (int) (ResultsContext.correct * 100.0 / Math.max(1, ResultsContext.total)) + "%)");
        timeLabel.setText("Time Taken: " + ResultsContext.timeTaken);
        backBtn.setOnAction(e -> SceneRouter.switchTo("/view/AdminDashboard.fxml"));
        reviewBtn.setOnAction(e -> SceneRouter.switchTo("/view/StudentLogin.fxml")); // Stub
        celebrate();
    }

    private void celebrate() {
        Random rnd = new Random();
        for (int i = 0; i < 50; i++) {
            Circle confetti = new Circle(4 + rnd.nextInt(4));
            confetti.setFill(randomColor(rnd));
            confetti.setLayoutX(20 + rnd.nextDouble() * Math.max(20, confettiPane.getWidth() - 40));
            confetti.setLayoutY(-10);
            confettiPane.getChildren().add(confetti);
            TranslateTransition fall = new TranslateTransition(Duration.seconds(1.5 + rnd.nextDouble() * 1.2), confetti);
            fall.setByY(500 + rnd.nextInt(150));
            fall.setDelay(Duration.millis(rnd.nextInt(500)));
            fall.play();
        }
    }

    private Color randomColor(Random rnd) {
        Color[] palette = new Color[]{ Color.CORNFLOWERBLUE, Color.HOTPINK, Color.GOLD, Color.MEDIUMSEAGREEN, Color.ORANGE, Color.MEDIUMPURPLE };
        return palette[rnd.nextInt(palette.length)];
    }
}
