package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    private View vw;
    private ListView mTaskListView;
    private List items;
    private Integer idItem;
    private String item;
    private ArrayAdapter<String> itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);


        mHelper = new FeedReaderDbHelper(this);

        Bundle extras = getIntent().getExtras();
        String txt = extras.getString("name");
        this.item = txt;
        this.idItem = mHelper.getAnyID("Items", "title", txt);



        List<String> intermediaire = new ArrayList<String>(mHelper.getTasksFromItem(idItem));
        items = new ArrayList<String>(mHelper.getTasksFromItem(idItem));

//        for(String s : intermediaire) {
//
//        }

        TextView txtV = findViewById(R.id.titleDisplay);
        //txtV.setPaintFlags(txtV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        //txtV.setText(item);
//        String vvvvv = mHelper.readDone("salut").toString();

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

    public void deleteThisItem(View view){
        Intent intent = new Intent(this, MainActivity.class);

        mHelper.deleteItem(this.idItem);

        startActivity(intent);

    }

    public void updateTitle(View view) {

            mHelper = new FeedReaderDbHelper(this);

            // Création d'un alert dialog pour l'ajout d'une tâche
            final EditText taskEditText = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Modifier le titre")
                    .setMessage("Entrez votre nouveau titre")
                    .setView(taskEditText)
                    .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(taskEditText.getText());
                            mHelper.insertIntoItems(task, "", "");
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .create();
            dialog.show();
        }

     public void goGreen(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
         l.setBackgroundResource(R.color.bckgrdGreen);
    }
    public void goBlue(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
         l.setBackgroundResource(R.color.bckgrdBlue);
    }
    public void goYellow(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
         l.setBackgroundResource(R.color.bckgrdYellow);
    }
    public void goRed(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
         l.setBackgroundResource(R.color.bckgrdRed);
    }
}
