package com.example.todolist.model;

public class Tag {
    private Integer id;
    private String wording;


    public Tag(Integer id, String wording) {
        this.id = id;
        this.wording = wording;
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
}
