package com.quizmasterfx.controller;

import com.quizmasterfx.SceneRouter;
import com.quizmasterfx.data.DataManager;
import com.quizmasterfx.model.Quiz;
import com.quizmasterfx.utils.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class StudentController {
    @FXML private TextField passwordField;
    @FXML private TextField nameField;
    @FXML private TextField idField;
    @FXML private Button accessBtn;
    @FXML private Button backBtn;
    @FXML private ListView<String> availableList;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        DataManager.getQuizzes().forEach(q -> availableList.getItems().add(q.getTitle() + " (" + q.getTimeLimitMinutes() + " min)"));
        accessBtn.setOnAction(e -> accessQuiz());
        backBtn.setOnAction(e -> SceneRouter.switchTo("/view/AdminDashboard.fxml"));
    }

    private void accessQuiz() {
        String pw = passwordField.getText();
        String name = nameField.getText();
        String sid = idField.getText();
        if (name == null || name.isBlank()) { DialogHelper.warn("Missing", "Please enter your name."); return; }
        if (sid == null || sid.isBlank()) { DialogHelper.warn("Missing", "Please enter your student ID."); return; }
        Quiz quiz = DataManager.findQuizByPassword(pw);
        if (quiz == null) {
            statusLabel.setText("No quiz found for password.");
            return;
        }
        // Prevent the same student (by studentId) from taking the same quiz twice
        boolean alreadyTaken = DataManager.getResults().stream()
                .anyMatch(r -> r.getQuizTitle().equals(quiz.getTitle()) && sid.equals(r.getStudentId()));
        if (alreadyTaken) {
            DialogHelper.warn("Already Taken", "Student ID '" + sid + "' has already taken this quiz.");
            return;
        }
        // Store ephemeral context via static holder (simple demo approach)
        QuizContext.set(quiz, name, sid);
        SceneRouter.switchTo("/view/QuizTaking.fxml");
    }
}

class QuizContext {
    static com.quizmasterfx.model.Quiz quiz;
    static String studentName;
    static String studentId;
    static void set(com.quizmasterfx.model.Quiz q, String name, String id) { quiz = q; studentName = name; studentId = id; }
}
