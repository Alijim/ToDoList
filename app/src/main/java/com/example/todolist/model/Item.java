package com.example.todolist.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Item {
    private Integer id;
    private String title;
    private Date deadline;
    private List<Task> listTasks;
    private String image;
    private String background_color;
    private Integer imageRessource;



    public Item(){};

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
        this.imageRessource = image;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public Integer getImageRessource() {
        return imageRessource;
    }

    public void setImageRessource(Integer imageRessource) {
        this.imageRessource = imageRessource;
    }
}
