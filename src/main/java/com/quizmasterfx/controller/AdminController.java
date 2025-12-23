package com.quizmasterfx.controller;

import com.quizmasterfx.SceneRouter;
import com.quizmasterfx.data.DataManager;
import com.quizmasterfx.model.Quiz;
import com.quizmasterfx.model.QuizResult;
import com.quizmasterfx.utils.DialogHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminController {
    @FXML private ListView<String> activeQuizzesList;
    @FXML private Button createQuizBtn;
    @FXML private Button studentPortalBtn;
    @FXML private Button toggleThemeBtn;
    @FXML private Label quizCountLabel;
    @FXML private TableView<QuizResult> resultsTable;
    @FXML private TableColumn<QuizResult, String> studentCol;
    @FXML private TableColumn<QuizResult, String> quizCol;
    @FXML private TableColumn<QuizResult, Integer> correctCol;
    @FXML private TableColumn<QuizResult, Integer> totalCol;
    @FXML private TableColumn<QuizResult, String> timeCol;

    @FXML
    public void initialize() {
        refreshActiveQuizzes();
        setupResultsTable();

        createQuizBtn.setOnAction(e -> onCreateQuiz());
        studentPortalBtn.setOnAction(e -> SceneRouter.switchTo("/view/StudentLogin.fxml"));
        toggleThemeBtn.setOnAction(e -> com.quizmasterfx.utils.StyleManager.toggleTheme());
    }

    private void setupResultsTable() {
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        quizCol.setCellValueFactory(new PropertyValueFactory<>("quizTitle"));
        correctCol.setCellValueFactory(new PropertyValueFactory<>("correct"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeTaken"));
        resultsTable.setItems(DataManager.getResults());
    }

    private void refreshActiveQuizzes() {
        ObservableList<Quiz> quizzes = DataManager.getQuizzes();
        activeQuizzesList.getItems().clear();
        for (Quiz q : quizzes) {
            activeQuizzesList.getItems().add(q.getTitle() + " - " + q.getTimeLimitMinutes() + " min");
        }
        quizCountLabel.setText("Active Quizzes: " + quizzes.size());
    }

    private void onCreateQuiz() {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/CreateQuiz.fxml"));
            javafx.stage.Stage dialog = new javafx.stage.Stage();
            dialog.initOwner(createQuizBtn.getScene().getWindow());
            dialog.setTitle("Create New Quiz");
            dialog.setScene(new javafx.scene.Scene(root));
            dialog.showAndWait();
            // after dialog closes, refresh list
            refreshActiveQuizzes();
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogHelper.error("Error", "Unable to open Create Quiz dialog: " + ex.getMessage());
        }
    }
}
