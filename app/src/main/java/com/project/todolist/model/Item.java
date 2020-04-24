package com.project.todolist.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Item {
    private Integer id;
    private String title;
    private Integer deadline;
    private List<Task> listTasks;
    private List<Tag> listTags;
    private String image;
    private String background_color;


    public List<Tag> getListTags() {
        return listTags;
    }

    public void setListTags(List<Tag> listTags) {
        this.listTags = listTags;
    }

    public Item(){};

    public Item(Integer id, String title, Integer deadline, List<Task> listTasks, List<Tag> listTags, String image, String background_color) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.listTasks = listTasks;
        this.listTags = listTags;
        this.image = image;
        this.background_color = background_color;
    }
    public Item(Integer id, String title, List<Task> listTasks, List<Tag> listTags, String image, String background_color) {
        this.id = id;
        this.title = title;
        this.listTasks = listTasks;
        this.listTags = listTags;
        this.image = image;
        this.background_color = background_color;
    }

    public Item(Integer id, String title, List<Task> listTasks, String image, String background_color) {
        this.id = id;
        this.title = title;
        this.listTasks = listTasks;
        this.image = image;
        this.background_color = background_color;
    }

    public Item(Integer id, String title, List<Task> listTasks, String background_color) {
        this.id = id;
        this.title = title;
        this.listTasks = listTasks;
        this.background_color = background_color;
    }

    public Item(String title, List<Task> listTasks, Integer image) {
        this.title = title;
        this.listTasks = listTasks;
    }

    public Integer getId() {
        return id;
    }

    public List<Task> getListTasks() {
        return listTasks;
    }

    public void setListTasks(List<Task> listTasks) {
        this.listTasks = listTasks;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDeadline() {
        return deadline;
    }

//    public String displayDeadline() {
//
//    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }


}
