package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import database.FeedReaderContract;
import database.FeedReaderDbHelper;

public class TaskEditionActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private ListView mTaskListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);

        mHelper = new FeedReaderDbHelper(this);

        mHelper.deleteAllData();

        mHelper.insertFakeData();
        ListView myList = findViewById(R.id.taskListView);

        TextView myText = findViewById(R.id.titleDisplay);
        TextView txt = findViewById(R.id.itemsDisplay);

        Integer i = mHelper.getAnyID("Tags", "wording", "ECOLE");

        myText.setText(i.toString());
        txt.setText(mHelper.readAllTasks());
    }



}
