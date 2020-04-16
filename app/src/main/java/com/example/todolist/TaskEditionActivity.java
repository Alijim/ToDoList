package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private List items;
    private String item;
    private ArrayAdapter<String> itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);
        mHelper = new FeedReaderDbHelper(this);

        Bundle extras = getIntent().getExtras();

        String txt = extras.getString("nom");
        this.item = txt;
        items = new ArrayList<String>(mHelper.getTasksFromItem(txt));

        TextView txtV = findViewById(R.id.titleDisplay);
        txtV.setText(item);

        itemsAdapter = new ArrayAdapter<String>(this,R.layout.item_todo, R.id.task_title, items);

        ListView lv = findViewById(R.id.taskListView);
        lv.setAdapter(itemsAdapter);

    }


    public void insertTaskIntoItem(View view) {
        EditText edtTask = findViewById(R.id.txtTask);

        mHelper.insertTask(item, edtTask.getText().toString());


        items.add(edtTask.getText().toString());
        itemsAdapter.notifyDataSetChanged();
        edtTask.getText().clear();
        //startActivity(intent);
    }



}
