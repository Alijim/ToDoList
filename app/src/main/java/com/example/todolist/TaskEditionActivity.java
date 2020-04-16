package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import database.FeedReaderContract;
import database.FeedReaderDbHelper;

public class TaskEditionActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private ListView mTaskListView;
    //private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);
        mHelper = new FeedReaderDbHelper(this);

        Bundle extras = getIntent().getExtras();

        String txt = extras.getString("nom");


        List items = new ArrayList<String>(mHelper.getTasksFromItem(txt));


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this,R.layout.item_todo, R.id.task_title, items);

        ListView lv = (ListView) findViewById(R.id.taskListView);
        lv.setAdapter(itemsAdapter);
//        myText.setText(i.toString());
//        txt.setText(l.toString());
    }



}
