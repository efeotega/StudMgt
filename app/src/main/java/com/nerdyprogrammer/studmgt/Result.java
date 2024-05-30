package com.nerdyprogrammer.studmgt;

import java.io.Serializable;

public class Result implements Serializable {
    private String id;
    private String name;
    private int score;
    private String grade;
    private String key;

    public Result() {
        // Default constructor required for calls to DataSnapshot.getValue(Result.class)
    }

    public Result(String id, String name, int score, String grade,String key) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.grade = grade;
        this.key=key;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

