package com.quizmasterfx.model;

public class Choice {
    private String text;
    private boolean correct;

    public Choice() {}

    public Choice(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    @Override
    public String toString() {
        return text;
    }
}
