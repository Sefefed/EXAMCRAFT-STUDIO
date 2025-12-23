package com.quizmasterfx.model;

import java.time.LocalDateTime;

public class QuizResult {
    private String quizTitle;
    private String studentName;
    private String studentId;
    private int correct;
    private int total;
    private String timeTaken; // mm:ss
    private LocalDateTime dateTime = LocalDateTime.now();

    public QuizResult() {}

    public QuizResult(String quizTitle, String studentName, String studentId, int correct, int total, String timeTaken) {
        this.quizTitle = quizTitle;
        this.studentName = studentName;
        this.studentId = studentId;
        this.correct = correct;
        this.total = total;
        this.timeTaken = timeTaken;
    }

    public String getQuizTitle() { return quizTitle; }
    public String getStudentName() { return studentName; }
    public String getStudentId() { return studentId; }
    public int getCorrect() { return correct; }
    public int getTotal() { return total; }
    public String getTimeTaken() { return timeTaken; }
    public LocalDateTime getDateTime() { return dateTime; }
}
