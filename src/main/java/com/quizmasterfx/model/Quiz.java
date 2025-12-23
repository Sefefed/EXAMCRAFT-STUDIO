package com.quizmasterfx.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String title;
    private String description;
    private String password;
    private int timeLimitMinutes; // minutes
    private boolean shuffleQuestions = true;
    private boolean showResultsImmediately = true;
    private boolean allowReviewAfterSubmit = true;
    private boolean requireStudentId = false;

    private List<Question> questions = new ArrayList<>();

    public Quiz() {}

    public Quiz(String title, String password, int timeLimitMinutes) {
        this.title = title;
        this.password = password;
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(int timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }
    public boolean isShuffleQuestions() { return shuffleQuestions; }
    public void setShuffleQuestions(boolean shuffleQuestions) { this.shuffleQuestions = shuffleQuestions; }
    public boolean isShowResultsImmediately() { return showResultsImmediately; }
    public void setShowResultsImmediately(boolean showResultsImmediately) { this.showResultsImmediately = showResultsImmediately; }
    public boolean isAllowReviewAfterSubmit() { return allowReviewAfterSubmit; }
    public void setAllowReviewAfterSubmit(boolean allowReviewAfterSubmit) { this.allowReviewAfterSubmit = allowReviewAfterSubmit; }
    public boolean isRequireStudentId() { return requireStudentId; }
    public void setRequireStudentId(boolean requireStudentId) { this.requireStudentId = requireStudentId; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public void addQuestion(Question q) { this.questions.add(q); }
}
