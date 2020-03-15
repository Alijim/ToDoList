package com.example.todolist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private ArrayList<String> myDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView.
        myRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize the ArrayList that will contain the data.
        myDataList = new ArrayList<>();

        myDataList.add("aaaaaaa");
        myDataList.add("bbbbbbb");
        myDataList.add("ccccccc");

    }


}
