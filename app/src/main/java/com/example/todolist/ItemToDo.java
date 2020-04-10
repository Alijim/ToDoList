package com.example.todolist;

import java.util.ArrayList;
import java.util.List;

public class ItemToDo {

    private String title;
    private ArrayList<String> listItem;
    private int imageResource;

    public ItemToDo(){};

    public ItemToDo(String title, ArrayList<String> listItem, int imageResource) {
        this.title = title;
        this.listItem = listItem;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getListItem() {
        return listItem;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setListItem(ArrayList<String> listItem) {
        this.listItem = listItem;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
