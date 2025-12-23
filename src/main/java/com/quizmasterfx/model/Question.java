package com.quizmasterfx.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String text;
    private QuestionType type = QuestionType.MULTIPLE_CHOICE;
    private List<Choice> choices = new ArrayList<>();
    private String shortAnswerCorrect;

    public Question() {}

    public Question(String text, List<Choice> choices) {
        this.text = text;
        this.type = QuestionType.MULTIPLE_CHOICE;
        this.choices = choices;
    }

    public Question(String text, boolean trueIsCorrect) {
        this.text = text;
        this.type = QuestionType.TRUE_FALSE;
        this.choices = List.of(new Choice("True", trueIsCorrect), new Choice("False", !trueIsCorrect));
    }

    public Question(String text, String shortAnswerCorrect) {
        this.text = text;
        this.type = QuestionType.SHORT_ANSWER;
        this.shortAnswerCorrect = shortAnswerCorrect;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public QuestionType getType() { return type; }
    public void setType(QuestionType type) { this.type = type; }
    public List<Choice> getChoices() { return choices; }
    public void setChoices(List<Choice> choices) { this.choices = choices; }
    public String getShortAnswerCorrect() { return shortAnswerCorrect; }
    public void setShortAnswerCorrect(String shortAnswerCorrect) { this.shortAnswerCorrect = shortAnswerCorrect; }
}
