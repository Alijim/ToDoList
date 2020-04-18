package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.todolist.model.Item;
import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

import database.FeedReaderDbHelper;

public class TaskEditionActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private View vw;
    private Item item;
    private ListView mTaskListView;
    private List<String> tasks;
    private List<Integer> itemsId;
    private Integer idItem;
    private ArrayAdapter<String> itemsAdapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);
//        View l = findViewById(R.id.cv_Title).getRootView();
        ConstraintLayout l = findViewById(R.id.mainEdition);
        mHelper = new FeedReaderDbHelper(this);
        String txt = "";


        Bundle extras = getIntent().getExtras();

        txt = extras.getString("name");

        this.item = mHelper.researchItem(txt);
        this.tasks = new ArrayList<String>();

        Integer color = Integer.parseInt(item.getBackground_color());


        l.setBackgroundResource(color);


        TextView txtV = findViewById(R.id.titleDisplay);
        //txtV.setPaintFlags(txtV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        //txtV.setText(item);
//        String vvvvv = mHelper.readDone("salut").toString();

        txtV.setText(item.getTitle());

        for(Task t : item.getListTasks()) {
            tasks.add(t.getWording());
        }

//        itemsAdapter = new ArrayAdapter<String>(this,R.layout.item_todo, R.id.task_title, items);

//        CheckBoxAdapter cbxAdapter = new CheckBoxAdapter(this, items );
        itemsAdapter = new ArrayAdapter<String>(this,R.layout.item_todo, R.id.chkBox, tasks);

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

        Task t = new Task(edtTask.getText().toString(), Boolean.FALSE);

        mHelper.insertTask(item.getId(), t);
        item.getListTasks().add(t);
        tasks.add(edtTask.getText().toString());
        itemsAdapter.notifyDataSetChanged();
        edtTask.getText().clear();
        //startActivity(intent);
    }

    public void deleteThisItem(View view){
        Intent intent = new Intent(this, MainActivity.class);
        mHelper.deleteItemById(item.getId());
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
                            item.setTitle(task);
                            mHelper.updateItem(item);
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
        Integer color = R.color.bckgrdGreen;
        item.setBackground_color(color.toString());
        mHelper.updateItem(item);
      l.setBackgroundResource(R.color.bckgrdGreen);
    }
    public void goBlue(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdBlue;
        item.setBackground_color(color.toString());
        mHelper.updateItem(item);
        l.setBackgroundResource(color);
    }
    public void goYellow(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdYellow;
        item.setBackground_color(color.toString());
        mHelper.updateItem(item);
        l.setBackgroundResource(R.color.bckgrdYellow);
    }
    public void goRed(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdRed;
        item.setBackground_color(color.toString());
        mHelper.updateItem(item);
        l.setBackgroundResource(color);
    }
    public void goWhite(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdWhite;
        item.setBackground_color(color.toString());
        mHelper.updateItem(item);
        l.setBackgroundResource(R.color.bckgrdWhite);
    }
}
