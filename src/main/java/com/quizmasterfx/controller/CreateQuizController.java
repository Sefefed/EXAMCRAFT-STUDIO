package com.quizmasterfx.controller;

import com.quizmasterfx.data.DataManager;
import com.quizmasterfx.model.Choice;
import com.quizmasterfx.model.Question;
import com.quizmasterfx.model.QuestionType;
import com.quizmasterfx.model.Quiz;
import com.quizmasterfx.utils.DialogHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CreateQuizController {
    @FXML private TextField titleField;
    @FXML private PasswordField passwordField;
    @FXML private TextField timeLimitField;
    @FXML private TextArea descriptionArea;

    @FXML private TextArea questionTextArea;
    @FXML private ChoiceBox<QuestionType> typeChoice;

    // MCQ choices
    @FXML private TextField choiceA;
    @FXML private TextField choiceB;
    @FXML private TextField choiceC;
    @FXML private TextField choiceD;
    @FXML private CheckBox correctA;
    @FXML private CheckBox correctB;
    @FXML private CheckBox correctC;
    @FXML private CheckBox correctD;

    // Short answer
    @FXML private TextField shortAnswerField;

    @FXML private ListView<String> questionsListView;

    private final List<Question> stagedQuestions = new ArrayList<>();

    @FXML
    public void initialize() {
        typeChoice.setItems(FXCollections.observableArrayList(QuestionType.values()));
        typeChoice.setValue(QuestionType.MULTIPLE_CHOICE);
        updateFieldsForType(QuestionType.MULTIPLE_CHOICE);
        typeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> updateFieldsForType(newV));
    }

    private void updateFieldsForType(QuestionType type) {
        boolean mc = type == QuestionType.MULTIPLE_CHOICE;
        boolean sa = type == QuestionType.SHORT_ANSWER;
        choiceA.setDisable(!mc); choiceB.setDisable(!mc); choiceC.setDisable(!mc); choiceD.setDisable(!mc);
        correctA.setDisable(!mc); correctB.setDisable(!mc); correctC.setDisable(!mc); correctD.setDisable(!mc);
        shortAnswerField.setDisable(!sa);
    }

    @FXML
    private void onAddQuestion() {
        String qtext = questionTextArea.getText().trim();
        if (qtext.isEmpty()) { DialogHelper.error("Invalid", "Question text cannot be empty"); return; }
        QuestionType type = typeChoice.getValue();
        Question q;
        if (type == QuestionType.MULTIPLE_CHOICE) {
            List<Choice> choices = new ArrayList<>();
            if (!choiceA.getText().isBlank()) choices.add(new Choice(choiceA.getText().trim(), correctA.isSelected()));
            if (!choiceB.getText().isBlank()) choices.add(new Choice(choiceB.getText().trim(), correctB.isSelected()));
            if (!choiceC.getText().isBlank()) choices.add(new Choice(choiceC.getText().trim(), correctC.isSelected()));
            if (!choiceD.getText().isBlank()) choices.add(new Choice(choiceD.getText().trim(), correctD.isSelected()));
            if (choices.size() < 2) { DialogHelper.error("Invalid", "Provide at least two choices"); return; }
            boolean anyCorrect = choices.stream().anyMatch(Choice::isCorrect);
            if (!anyCorrect) { DialogHelper.error("Invalid", "Mark at least one correct choice"); return; }
            q = new Question(qtext, choices);
        } else if (type == QuestionType.TRUE_FALSE) {
            // default to True as correct if shortAnswerField contains "true" - provide a simple toggle by text
            boolean trueIsCorrect = "true".equalsIgnoreCase(shortAnswerField.getText().trim());
            q = new Question(qtext, trueIsCorrect);
        } else { // SHORT_ANSWER
            String ans = shortAnswerField.getText().trim();
            if (ans.isEmpty()) { DialogHelper.error("Invalid", "Provide the correct short answer"); return; }
            q = new Question(qtext, ans);
        }
        stagedQuestions.add(q);
        questionsListView.getItems().add(formatQuestionSummary(q));
        clearQuestionEntryFields();
    }

    private String formatQuestionSummary(Question q) {
        return q.getType() + ": " + (q.getText().length() > 80 ? q.getText().substring(0, 77) + "..." : q.getText());
    }

    private void clearQuestionEntryFields() {
        questionTextArea.clear();
        choiceA.clear(); choiceB.clear(); choiceC.clear(); choiceD.clear();
        correctA.setSelected(false); correctB.setSelected(false); correctC.setSelected(false); correctD.setSelected(false);
        shortAnswerField.clear();
    }

    @FXML
    private void onSaveQuiz() {
        String title = titleField.getText().trim();
        String pwd = passwordField.getText().trim();
        int time = 0;
        try { time = Integer.parseInt(timeLimitField.getText().trim()); } catch (Exception e) {}
        if (title.isEmpty() || pwd.isEmpty() || time <= 0) { DialogHelper.error("Invalid", "Title, password and time limit are required (time > 0)"); return; }
        if (stagedQuestions.isEmpty()) { DialogHelper.error("Empty Quiz", "Add at least one question"); return; }
        Quiz quiz = new Quiz(title, pwd, time);
        quiz.setDescription(descriptionArea.getText().trim());
        stagedQuestions.forEach(quiz::addQuestion);
        DataManager.saveQuiz(quiz);
        DialogHelper.info("Saved", "Quiz '" + title + "' saved with " + stagedQuestions.size() + " questions");
        // close window
        Stage s = (Stage) titleField.getScene().getWindow();
        s.close();
    }

    @FXML
    private void onCancel() {
        Stage s = (Stage) titleField.getScene().getWindow();
        s.close();
    }
}
