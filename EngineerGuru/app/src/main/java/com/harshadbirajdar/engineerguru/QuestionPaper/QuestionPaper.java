package com.harshadbirajdar.engineerguru.QuestionPaper;

public class QuestionPaper {
    String month;
    String pattern;
    String pdf;
    int year;
    String ans;


    public QuestionPaper() {
    }

    public QuestionPaper(String month, String pattern, String pdf, int year, String ans) {
        this.month = month;
        this.pattern = pattern;
        this.pdf = pdf;
        this.year = year;
        this.ans = ans;
    }

    public String getMonth() {
        return month;
    }

    public String getPattern() {
        return pattern;
    }

    public String getPdf() {
        return pdf;
    }

    public int getYear() {
        return year;
    }

    public String getAns() {
        return ans;
    }
}