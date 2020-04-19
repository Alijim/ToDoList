package com.example.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.todolist.model.Item;
import com.example.todolist.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import database.FeedReaderDbHelper;

public class TaskEditionActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private ImageButton btnDatePicker;
    private TextView txt_Date;
    private View vw;
    private Item item;
    private String txtDate = "";

    private ListView mTaskListView;
    private List<String> tasks;
    private List<Task> listTask;
    private List<Integer> itemsId;
    private Integer idItem;
    private CheckBoxAdapter cbxAdapter;
    private ArrayAdapter<String> itemsAdapter;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);
//        View l = findViewById(R.id.cv_Title).getRootView();
        ConstraintLayout l = findViewById(R.id.mainEdition);
        btnDatePicker= findViewById(R.id.btn_Date);
        txt_Date = findViewById(R.id.txtv_Date);


        mHelper = new FeedReaderDbHelper(this);
        String txt = "";


        Bundle extras = getIntent().getExtras();

        txt = extras.getString("name");

        this.item = mHelper.researchItem(txt);
        this.tasks = new ArrayList<String>();
        this.listTask = item.getListTasks();

        Integer color = Integer.parseInt(item.getBackground_color());


        l.setBackgroundResource(color);

        if (cbxAdapter != null) {
            tasks.clear();
            cbxAdapter.clear();
        }
//        listItems = mHelper.getListItemByTodo(task.getNumID());
        cbxAdapter = new CheckBoxAdapter(this, listTask);


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
        lv.setAdapter(cbxAdapter);

//        lv.setAdapter(itemsAdapter);



    }



    public void onClickDatePicker(final View v) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txtDate = dayOfMonth + " " + (getTextMonthFR(mMonth+1)) + " " + year;
                        displayTimePickerDialog(v);
//                        txt_Date.setText(dayOfMonth + " " + (getTextMonthFR(mMonth+1)) + " " + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void displayTimePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtDate += " à "+hourOfDay+ ":"+ minute;
//                        String date = mYear+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute;
                        String date = mDay+"-"+mMonth+1+"-"+mYear+" "+mHour+":"+mMinute;
                        Date datee = null;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm");
                        try {
                            datee = new SimpleDateFormat("dd-M-yyyy hh:mm").parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String str = datee.toString();

                        item.setDeadline(datee);
                        txt_Date.setText(txtDate);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void insertTaskIntoItem(View view) {
        EditText edtTask = findViewById(R.id.txtTask);

        Task t = new Task(edtTask.getText().toString(), Boolean.FALSE);

        long id = mHelper.insertTask(item.getId(), t);
        Integer iId = (int) (long) id;

        Task tWithId = new Task(iId, t.getWording(), t.getDone());

        item.getListTasks().add(tWithId);
        tasks.add(edtTask.getText().toString());
        itemsAdapter.notifyDataSetChanged();
        edtTask.getText().clear();
        //startActivity(intent);
    }

    public String getTextMonthFR(Integer m) {
        switch(m){
            case 1 : return "janvier";
            case 2 : return "février";
            case 3 : return "mars";
            case 4 : return "avril";
            case 5 : return "mai";
            case 6 : return "juin";
            case 7 : return "juillet";
            case 8 : return "août";
            case 9 : return "septembre";
            case 10 : return "octobre";
            case 11 : return "novembre";
            case 12 : return "décembre";
            default :return"erreur";
        }
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

    public void updateCheckBox(View view){
        View parent = (View) view.getParent();
        CheckBox cbx = parent.findViewById(R.id.chkBox);
        TextView txtv_task = parent.findViewById(R.id.txtv_task);
        Integer i = Integer.parseInt(cbx.getText().toString());

        Task t = new Task(i, txtv_task.getText().toString(), cbx.isChecked());
        cbx.setChecked(t.getDone());
        if(t.getDone() == true) {
            txtv_task.setPaintFlags(0);
            txtv_task.setText(t.getWording());
        } else {
            txtv_task.setPaintFlags(txtv_task.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            txtv_task.setText(t.getWording());
        }

        mHelper.updateTask(t);
    }

    public void updateDate(Integer y, Integer m, Integer d) {
        Date dt = new Date();
//        dt.
//        item.setDeadline();
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
