package com.project.todolist.activities;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.project.todolist.adapters.CheckBoxAdapter;
import com.project.todolist.R;
import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;
import com.project.todolist.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import database.FeedReaderDbHelper;

public class TaskEditionActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private ImageView  imgv_image;
    private ImageButton btnDatePicker;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private TextView txt_Date;
    private View vw;
    private Item item;
    private String txtDate = "";
    private String txtToTest;

    private ListView mTaskListView;
    private List<String> tasks;
    private List<Task> listTask;
    private List<String> listTag;
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
        TextView txt_Tag = findViewById(R.id.lv_ItemTag);
        ImageView img = findViewById(R.id.imageTask);
        this.imgv_image = img;

        mHelper = new FeedReaderDbHelper(this);
        String txt = "";


        Bundle extras = getIntent().getExtras();

        txt = extras.getString("name");

        this.item = mHelper.researchItem(txt);
        this.tasks = new ArrayList<String>();
        this.listTask = item.getListTasks();
        this.listTag = new ArrayList<String>();

        if (item.getImage() != null) {
            txt_Date.setText(item.getImage());
        }


        Integer color = Integer.parseInt(item.getBackground_color());

//        txt_Tag.setText(" Test : "+item.getListTags().toString());
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
        for(Tag tag : item.getListTags()) {
            if(tag.getId() != null) {
                listTag.add(tag.getWording());
            }
        }

        String displayTag = "";
        if(listTag.size() > 0) {
            for(String s : listTag) {
                displayTag += "["+s+"] ";
            }
        }


        txt_Tag.setText(displayTag);

        txtV.setText(item.getTitle());

        for(Task t : item.getListTasks()) {
            tasks.add(t.getWording());
        }


        itemsAdapter = new ArrayAdapter<String>(this,R.layout.item_todo, R.id.chkBox, tasks);

        ListView lv = findViewById(R.id.taskListView);
        lv.setAdapter(cbxAdapter);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openGallery();
                }
        });{

        }


//        lv.setAdapter(itemsAdapter);



    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imgv_image.setImageDrawable(null);
            imgv_image.setImageURI(imageUri);
        }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
//            imageUri = data.getData();
//
//            try {
////                Uri selectedImage = data.getData();
//                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                imgv_image.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    }

    public void addTag(View view) {
        Intent intent = new Intent(this, EditionTagActivity.class);
//        TextView txt = view.findViewById(R.id.cv_Title);
        intent.putExtra("name", item.getTitle());
        startActivity(intent);
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
                        txtDate = dayOfMonth + " " + (getTextMonthFR(monthOfYear+1)) + " " + year;
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

                        item.setImage(txtDate);
                        mHelper.updateItem(item);
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
        if(t.getDone() == true) {
            txtv_task.setPaintFlags(txtv_task.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            txtv_task.setText(t.getWording());
        } else {
            txtv_task.setPaintFlags(0);
            txtv_task.setText(t.getWording());
        }
        cbx.setChecked(t.getDone());


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

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
