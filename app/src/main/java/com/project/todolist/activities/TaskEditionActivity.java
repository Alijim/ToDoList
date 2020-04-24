package com.project.todolist.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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

import com.project.todolist.DAO.ItemsDAO;
import com.project.todolist.DAO.TasksDAO;
import com.project.todolist.adapters.CheckBoxAdapter;
import com.project.todolist.R;
import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;
import com.project.todolist.model.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import database.FeedReaderDbHelper;

public class TaskEditionActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private ImageView  imgv_image;
    private ImageButton btnDatePicker;
    private static final int PICK_IMAGE = 1;
    private static final int PERSMISSION_CODE = 1001;
    private Uri imageUri;
    private TextView txt_Date;
    private View vw;
    private Item item;
    private String txtDate = "";
    private String txtToTest;

    private ItemsDAO itemsDAO;
    private TasksDAO tasksDAO;
    private int yr;
    private int mth;
    private int dt;

    private ListView mTaskListView;
    private List<String> tasks;
    private List<Task> listTask;
    private List<String> listTag;
    private List<Integer> itemsId;
    private Integer idItem;
    private CheckBoxAdapter cbxAdapter;
    private ArrayAdapter<String> itemsAdapter;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Calendar myCalendar = Calendar.getInstance () ;
    private Calendar notification ;
    private Date mdate = myCalendar.getTime() ;
    private Date dateNotification;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);
        createNotificationChannel();
//        View l = findViewById(R.id.cv_Title).getRootView();
        ConstraintLayout l = findViewById(R.id.mainEdition);
        btnDatePicker= findViewById(R.id.btn_Date);
        txt_Date = findViewById(R.id.txtv_Date);
        TextView txt_Tag = findViewById(R.id.lv_ItemTag);
        ImageView img =  findViewById(R.id.imageTask);
        ItemsDAO iDAO = new ItemsDAO(this);
        TasksDAO tDAO = new TasksDAO(this);

        this.itemsDAO = iDAO;
        this.tasksDAO = tDAO;

        imgv_image = img;

        mHelper = new FeedReaderDbHelper(this);
        String txt = "";


        Bundle extras = getIntent().getExtras();

        txt = extras.getString("name");

        this.item = itemsDAO.researchItem(txt);
//        this.item = mHelper.researchItem(txt);
        this.tasks = new ArrayList<String>();
        this.listTask = item.getListTasks();
        this.listTag = new ArrayList<String>();

        if (item.getImage() != null) {
//            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            String imgUri = item.getImage();
            Uri imgur = Uri.parse(imgUri);

                    imgv_image.setImageURI(imgur);

        } else {
            imgv_image.setImageResource(R.drawable.img_addapicture);
        }

//        if(item.getDeadline() != null ) {
//            txt_Date.setText(item.getDeadline());
//        }

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



//
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    openGallery();
//                }
//        });{
//
//        }


//        lv.setAdapter(itemsAdapter);



    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("667", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification (Context context, long time/*, String title, String text*/) {

        Intent intent = new Intent(this, MyNotificationPublisher.class);
        PendingIntent pending = PendingIntent.getBroadcast(
                TaskEditionActivity.this,
                0,
                intent,
                0);

        // Schdedule notification
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pending);
        manager.set(AlarmManager.RTC_WAKEUP, time, pending);
    }

    @SuppressLint("WrongConstant")
//    public void updateLabelButtonDateTime(View view){
//        String myFormat = "dd/MM/yyyy' 'HH'h'mm':'ss"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale.getDefault ());
//        myCalendar.add(12, 1);
//        Date date = myCalendar.getTime();
////        btnDateTime.setText("NOTIF A: "+sdf.format(date));
//        long l = myCalendar.getTimeInMillis();
//
//        scheduleNotification(this, l);
//    }
    public void setupNotification(Calendar myCalendar){
        String myFormat = "dd/MM/yyyy' 'HH'h'mm':'ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale.getDefault ());
//        myCalendar.add(12, 1);
        Date date = myCalendar.getTime();
//        btnDateTime.setText("NOTIF A: "+sdf.format(date));
        long l = myCalendar.getTimeInMillis();

        scheduleNotification(this, l);
    }

    public void onClickSetImage(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERSMISSION_CODE);
            }else{
                // permission already granted
                openGallery();
            }
        }else{
            // system OS is less than marshmallow
            openGallery();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
//            imageUri = data.getData();
//            String imgUri = imageUri.toString();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                item.setImage(imgUri);
//                itemsDAO.updateItem(item);
//                imgv_image.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch(requestCode){
//            case PERSMISSION_CODE : {
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    openGallery();
//                }else{
//                    Toast errorToast = Toast.makeText(TaskEditionActivity.this, "erreur", Toast.LENGTH_SHORT);
//                    errorToast.show();
//                }
//            }
//
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imgv_image.setImageURI(data.getData());
            imageUri = data.getData();
            item.setImage(imageUri.toString());
            itemsDAO.updateItem(item);

        } }
////        @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
////            // Accès à l'image à partir de data
////            Uri selectedImage = data.getData();
////            String[] filePathColumn = {MediaStore.Images.Media.DATA};
////            // Curseur d'accès au chemin de l'image
////            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
////// Position sur la première ligne (normalement une seule)
////            if(cursor.moveToFirst()){
////                // Récupération du chemin précis de l'image
////                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                String photoPath = cursor.getString(columnIndex);
////                cursor.close();
////                // Récupération image
////                Bitmap image = BitmapFactory.decodeFile(photoPath);
////                // Affichage
////                imgv_image.setImageBitmap(image);
////            }
////
////        }
//    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
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
//        dateNotification = c.getTime();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate = dayOfMonth + " " + (getTextMonthFR(monthOfYear+1)) + " " + year;
//                        notification.add
                        yr = year;
                        mth = monthOfYear;
                        dt = dayOfMonth;


                        displayTimePickerDialog(v, year, monthOfYear, dayOfMonth);
//                        txt_Date.setText(dayOfMonth + " " + (getTextMonthFR(mMonth+1)) + " " + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void displayTimePickerDialog(View v, int y, int m, int d) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
//        dateNotification = c.getTime();

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

                        Calendar ca = Calendar.getInstance();
                        ca.set(yr, mth, dt, hourOfDay, minute, 0);
                        notification = ca;
                        setupNotification(ca);
                        dateNotification = ca.getTime();
                        Integer in  = (int)dateNotification.getTime();
                        item.setDeadline(in);
                        itemsDAO.updateItem(item);
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm");
//                        try {
//                            datee = new SimpleDateFormat("dd-M-yyyy hh:mm").parse(date);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        String str = datee.toString();
//
//                        item.setImage(txtDate);
//                        itemsDAO.updateItem(item);
//                        mHelper.updateItem(item);
                        txt_Date.setText(dateNotification.toString());
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void insertTaskIntoItem(View view) {
        EditText edtTask = findViewById(R.id.txtTask);

        Task t = new Task(edtTask.getText().toString(), Boolean.FALSE);

//        long id = mHelper.insertTask(item.getId(), t);
        long id = tasksDAO.insertTask(item.getId(), t);
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
        itemsDAO.deleteItemById(item.getId());
//        mHelper.deleteItemById(item.getId());
        startActivity(intent);

    }

    public void updateTitle(View view) {

//            mHelper = new FeedReaderDbHelper(this);

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
                            itemsDAO.updateItem(item);
//                            mHelper.updateItem(item);
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


        tasksDAO.updateTask(t);
//        mHelper.updateTask(t);
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
        itemsDAO.updateItem(item);
//        mHelper.updateItem(item);
      l.setBackgroundResource(R.color.bckgrdGreen);
    }
    public void goBlue(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdBlue;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
//        mHelper.updateItem(item);
        l.setBackgroundResource(color);
    }
    public void goYellow(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdYellow;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
//        mHelper.updateItem(item);
        l.setBackgroundResource(R.color.bckgrdYellow);
    }
    public void goRed(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdRed;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
//        mHelper.updateItem(item);
        l.setBackgroundResource(color);
    }
    public void goWhite(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdWhite;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
//        mHelper.updateItem(item);
        l.setBackgroundResource(R.color.bckgrdWhite);
    }

//
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
