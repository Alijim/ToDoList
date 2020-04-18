package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.FeedReaderDbHelper;

public class TaskEditionActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private View vw;
    private ListView mTaskListView;
    private List<String> items;
    private List<Integer> itemsId;
    private Integer idItem;
    private String item;
    private ArrayAdapter<String> itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);

        View l = findViewById(R.id.mainEdition).getRootView();



        mHelper = new FeedReaderDbHelper(this);



        Bundle extras = getIntent().getExtras();
        String txt = extras.getString("name");
        this.item = txt;
        this.idItem = mHelper.getAnyID("Items", "title", txt);

        l.setBackgroundResource(mHelper.getBackgroundColorFromItem(idItem));


        List<String> intermediaire = new ArrayList<String>(mHelper.getTasksFromItem(idItem));
        items = new ArrayList<String>(mHelper.getTasksFromItem(idItem));
        itemsId = new ArrayList<Integer>(mHelper.getTasksIdFromItem(idItem));

//        for(String s : intermediaire) {
//
//        }

        TextView txtV = findViewById(R.id.titleDisplay);
        //txtV.setPaintFlags(txtV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        //txtV.setText(item);
//        String vvvvv = mHelper.readDone("salut").toString();

        txtV.setText(item);

//        itemsAdapter = new ArrayAdapter<String>(this,R.layout.item_todo, R.id.task_title, items);

//        CheckBoxAdapter cbxAdapter = new CheckBoxAdapter(this, items );
        itemsAdapter = new ArrayAdapter<String>(this,R.layout.item_todo, R.id.chkBox, items);

//        for (Integer i : itemsId) {
//            if(mHelper.readDone(i) == 0) {
//                CheckBox cbx = null;
//                cbx.setChecked(true);
//                itemsAdapter.add(cbx);
//            } else {
//
//            }
//        }
//        itemsAdapter.add("test");

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
                            mHelper.updateItemTitle(idItem, task);
                            TextView txtV = findViewById(R.id.titleDisplay);
                            txtV.setText(task);
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .create();
            dialog.show();
        }


    public void goGreen(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
         mHelper.updateBackgroundColorFromItem(idItem, R.color.bckgrdGreen);
         l.setBackgroundResource(R.color.bckgrdGreen);
    }
    public void goBlue(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        mHelper.updateBackgroundColorFromItem(idItem, R.color.bckgrdBlue);

        l.setBackgroundResource(R.color.bckgrdBlue);
    }
    public void goYellow(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        mHelper.updateBackgroundColorFromItem(idItem, R.color.bckgrdYellow);

        l.setBackgroundResource(R.color.bckgrdYellow);
    }
    public void goRed(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        mHelper.updateBackgroundColorFromItem(idItem, R.color.bckgrdRed);

        l.setBackgroundResource(R.color.bckgrdRed);
    }
    public void goWhite(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        mHelper.updateBackgroundColorFromItem(idItem, R.color.bckgrdWhite);

        l.setBackgroundResource(R.color.bckgrdWhite);
    }
}
