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
        List items = new ArrayList<String>();
        items.add("salut");
        items.add("yo");
        items.add("wsh");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this,R.layout.item_todo, R.id.task_title, items);

        ListView lv = (ListView) findViewById(R.id.taskListView);
        lv.setAdapter(itemsAdapter);


//
//        mHelper = new FeedReaderDbHelper(this);
//        mTaskListView = (ListView) findViewById(R.id.taskListView);
//
//        if (mAdapter == null) {
//            mAdapter = new ArrayAdapter<String>(this,
//                    R.layout.item_todo,
//                    R.id.task_title,
//                    (List<String>) mTaskListView);
//            mTaskListView.setAdapter(mAdapter);
//        } else {
//            mAdapter.clear();
//            mAdapter.addAll((Collection<? extends String>) mTaskListView);
//            mAdapter.notifyDataSetChanged();
//        }
//


        //  mHelper.deleteAllData();

//        //mHelper.insertFakeData();
//        ListView myList = findViewById(R.id.taskListView);
//
//        TextView myText = findViewById(R.id.titleDisplay);
//        TextView txt = findViewById(R.id.itemsDisplay);
//
//        Integer i = mHelper.getAnyID("Tags", "wording", "ECOLE");
//
//        List l = new ArrayList<>();
//        long idd = 1;
//        //l.addAll((mHelper.getTaskFromItem("Devenir développeur").get(idd)));

//        myText.setText(i.toString());
//        txt.setText(l.toString());
    }



}
