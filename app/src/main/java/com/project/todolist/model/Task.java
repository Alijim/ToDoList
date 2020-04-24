package com.project.todolist.model;

public class Task {
    private Integer id;
    private String wording;
    private Boolean done;

    public Task(Integer id, String wording, Boolean done) {
        this.id = id;
        this.wording = wording;
        this.done = done;
    }

    public Task(String wording, Boolean done) {
        this.wording = wording;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
