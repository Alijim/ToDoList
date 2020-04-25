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
import android.widget.Toast;

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
    private Item item;
    private String txtDate = "";

    private ItemsDAO itemsDAO;
    private TasksDAO tasksDAO;
    private int yr;
    private int mth;
    private int dt;

    private List<String> tasks;
    private List<Task> listTask;
    private List<String> listTag;
    private CheckBoxAdapter cbxAdapter;
    private ArrayAdapter<String> itemsAdapter;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Calendar myCalendar = Calendar.getInstance () ;
    private Calendar notification ;
    private Date dateNotification;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edition);

        createNotificationChannel();

        /* Initalisation des ressources */
        ConstraintLayout l = findViewById(R.id.mainEdition);
        btnDatePicker= findViewById(R.id.btn_Date);
        txt_Date = findViewById(R.id.txtv_Date);
        TextView txt_Tag = findViewById(R.id.lv_ItemTag);
        ImageView img =  findViewById(R.id.imageTask);
        TextView txtV = findViewById(R.id.titleDisplay);
        ListView lv = findViewById(R.id.taskListView);

        /* Initialisations */
        ItemsDAO iDAO = new ItemsDAO(this);
        TasksDAO tDAO = new TasksDAO(this);

        Bundle extras = getIntent().getExtras(); //Récupération des valeurs passés dans l'intent (acitivité précédente)
        String itemName = extras.getString("name");

        /*Initialisation des variables gloables */
        this.itemsDAO = iDAO;
        this.tasksDAO = tDAO;
        this.imgv_image = img;
        this.item = itemsDAO.researchItem(itemName);
        this.tasks = new ArrayList<String>();
        this.listTask = item.getListTasks();
        this.listTag = new ArrayList<String>();

        /* --------------------------------------------------------  */
        /*  On va adapter l'affichage selon les données de la base de données : */

        /* Si l'item a une image, alors on affichage l'image.
        *  si il n'a pas d'image, on va alors afficher l'image par défaut.
        * */
        if (item.getImage() != null) {
            // Récupère l'URI qui est converti en URI et l'affiche
            String imgUri = item.getImage();
            Uri imgur = Uri.parse(imgUri);
            imgv_image.setImageURI(imgur);

        } else {
            imgv_image.setImageResource(R.drawable.img_addapicture);
        }


        /* Si la deadLine est à 0, ca veut dire qu'on a pas saisit de date de fin.
        *  si elle est différente de 0, alors on affiche la date en format "propre"
        *  */
        if(item.getDeadline() != 0 ) {
            txt_Date.setText(item.displayDeadline());
        }


        /*Récupère la couleur de fond. Par défaut, c'est blanc.*/
        Integer color = Integer.parseInt(item.getBackground_color());
        l.setBackgroundResource(color);

        /* Refresh l'affichage des Checkbox afin d'éviter des problèmes de cohérences entre le model et la view*/
        if (cbxAdapter != null) {
            tasks.clear();
            cbxAdapter.clear();
        }

        cbxAdapter = new CheckBoxAdapter(this, listTask);


        /* Récupère le nom des tags depuis le model*/
        for(Tag tag : item.getListTags()) {
            if(tag.getId() != null) {
                listTag.add(tag.getWording());
            }
        }

        /* Affiche les tags de manière simple entre crochets : [...]*/
        String displayTag = "";
        if(listTag.size() > 0) {
            for(String s : listTag) {
                displayTag += "[ "+s+" ] ";
            }
        }
        txt_Tag.setText(displayTag);

        txtV.setText(item.getTitle());

        /* Récupère les libelles des tâches depuis le model...*/
        for(Task t : item.getListTasks()) {
            tasks.add(t.getWording());
        }

        //Affiche les données via l'adapter....
        lv.setAdapter(cbxAdapter);

    }

    private void createNotificationChannel() {
        // Créer une NotificationChannel. API 26+ seulement!!!
        // Test si c'est compatible.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("667", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification (Context context, long time) {
        Intent intent = new Intent(this, MyNotificationPublisher.class);
        intent.putExtra("itemName", item.getTitle());
        PendingIntent pending = PendingIntent.getBroadcast(
                TaskEditionActivity.this,
                0,
                intent,
                0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, time, pending);
    }

    public void setupNotification(Calendar myCalendar){
        String myFormat = "dd/MM/yyyy' 'HH'h'mm':'ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale.getDefault ());
        Date date = myCalendar.getTime();
        long l = myCalendar.getTimeInMillis();

        scheduleNotification(this, l);
    }

    public void onClickSetImage(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //Si les permissions ne sont pas activées, les demandes.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERSMISSION_CODE);
            }else{
                //Si les permissions ont déjà été accordées alors on ouvre la galerie
                openGallery();
            }
        }else{
            //Si la version est trop vieille...
            openGallery();
        }
    }

    /* Va se lancer après que l'image est été sélectionné. onActivityResult
    *  va nous permettre de récupérer l'image qui a été choisie par l'utilisateur.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            //Enregistre l'image dans la base de données et l'affiche. On enregistre l'uri sous forme de String, qu'on re-convertira en URI quand il faudra l'afficher.
            imgv_image.setImageURI(data.getData());
            imageUri = data.getData();
            item.setImage(imageUri.toString());
            itemsDAO.updateItem(item);
        }
    }

    /*Ouvre la galerie*/
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //Important de mettre OPEN DOCUMENT afin de garder les droits.
        intent.setType("image/*"); //se placer là où la galerie doit s'ouvrir
        startActivityForResult(intent, PICK_IMAGE);
    }

    /* Commence l'activité qui va permettre d'éditer les tags associés à cet item. */
    public void onClickAddTag(View view) {
        Intent intent = new Intent(this, EditionTagActivity.class);
        intent.putExtra("name", item.getTitle()); //important de préciser, afin que EditionTag, édition l'association des tags avec l'item actuel.
        startActivity(intent);
    }


    /* Affiche un DatePickerDialog afin de permettre à l'utilisateur de saisir une date */
    public void onClickDatePicker(final View v) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Locale.setDefault(Locale.FRANCE); //Pour que les valeurs soit affichées en français
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    /* Quand la date est définie : */
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        yr = year;
                        mth = monthOfYear;
                        dt = dayOfMonth;

                        //Une fois que l'utilisateur a choisit sa date, on appelle une méthode qui ouvre un TimePickerDialog afin de choisir l'heure pour la notif.
                        displayTimePickerDialog(v, year, monthOfYear, dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); //Désactive les dates passées
        datePickerDialog.show();
    }

    public void displayTimePickerDialog(View v, int y, int m, int d) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        Locale.setDefault(Locale.FRANCE); //Affichage en français....

        //Lance le timePickerDialog !
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtDate += " à "+hourOfDay+ ":"+ minute;
                        String date = mDay+"-"+mMonth+1+"-"+mYear+" "+mHour+":"+mMinute;

                        Calendar ca = Calendar.getInstance();
                        ca.set(yr, mth, dt, hourOfDay, minute, 0); //La seconde est définie à 0, afin de recevoir la notification à l'heure pile!
                        notification = ca;
                        setupNotification(ca); //On appelle la fonction qui va définir la notification.

                        dateNotification = ca.getTime();
                        long ln = dateNotification.getTime(); //Récupère la date et l'heure en long afin de pouvoir la mettre dans la base de données.
                        item.setDeadline(ln);
                        itemsDAO.updateItem(item);
                        txt_Date.setText(item.displayDeadline());

                        /* Informe l'utilisateur qu'il va recevoir une notification ! */
                        Toast toast = Toast.makeText(getApplicationContext(), "Vous serez notifié quand la date limite arrivera. ", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }, mHour, mMinute, true); //Affiche en format 24 heures
        timePickerDialog.show();
    }

    //Insert une tâche dans un item....
    public void onClickInsertTaskIntoItem(View view) {
        EditText edtTask = findViewById(R.id.txtTask);
        if (edtTask.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Tâche vide ! ", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Task t = new Task(edtTask.getText().toString(), Boolean.FALSE);

            long id = tasksDAO.insertTask(item.getId(), t);
            Integer iId = (int) (long) id;
            Task tWithId = new Task(iId, t.getWording(), t.getDone());

            item.getListTasks().add(tWithId);
            tasks.add(edtTask.getText().toString());
            edtTask.getText().clear();
        }
    }
    //Supprime la tache lorsqu'on appuie sur le nom de la tâche !
    /* Je n'ai pas laissé la possibilité de modifier la tâche, j'ai pensé qu'il était plus
    *  instinctif de permettre uniquement la supression de celle-ci. Car généralement, on ne modifie
    * pas une tâche afin d'éviter d'avoir une incohérence ! Mais j'ai laissé possibilité de supprimer
    * afin d'éviter de pas avoir des tâches avec des erreurs de frappes.
    * */
    public void onClikDeleteTask(View view) {
        View parent = (View) view.getParent();
        CheckBox cbx = parent.findViewById(R.id.chkBox);
        TextView txtv_task = parent.findViewById(R.id.txtv_task);
        Integer i = Integer.parseInt(cbx.getText().toString());

        Task t = new Task(i, txtv_task.getText().toString(), cbx.isChecked());
        tasksDAO.deleteTask(i);
        item.setListTasks(tasksDAO.getTasksFromItem(item.getId()));
        listTask = item.getListTasks();
        cbxAdapter = new CheckBoxAdapter(this, listTask);

        ListView lv = findViewById(R.id.taskListView);
        lv.setAdapter(cbxAdapter);

    }

    public void onClickDeleteThisItem(View view){
        Intent intent = new Intent(this, MainActivity.class);
        itemsDAO.deleteItemById(item.getId());
        startActivity(intent);

    }
    /* On peut modifier le titre si on clique dessus ! */
    public void onClickUpdateTitle(View view) {
            // Création d'un alert dialog pour l'ajout d'une tâche
            final EditText taskEditText = new EditText(this);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Modifier le titre")
                    .setMessage("Entrez votre nouveau titre")
                    .setView(taskEditText)
                    .setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String itemTitle = String.valueOf(taskEditText.getText());

                            if(taskEditText.getText().toString().isEmpty()) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Titre vide ! ", Toast.LENGTH_SHORT);
                                toast.show();
                            } else if (itemsDAO.existItem(itemTitle)) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Ce nom est déjà utilisé ! ", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                item.setTitle(itemTitle);
                                itemsDAO.updateItem(item);
                                TextView txtV = findViewById(R.id.titleDisplay);
                                txtV.setText(itemTitle);
                            }
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .create();
            dialog.show();
        }

    public void onClickUpdateCheckBox(View view){
        View parent = (View) view.getParent();
        CheckBox cbx = parent.findViewById(R.id.chkBox);
        TextView txtv_task = parent.findViewById(R.id.txtv_task);
        Integer i = Integer.parseInt(cbx.getText().toString());

        Task t = new Task(i, txtv_task.getText().toString(), cbx.isChecked());
        if(t.getDone() == true) { //Si la tâche est effectuée ....
            txtv_task.setPaintFlags(txtv_task.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG); //Barre le text
            txtv_task.setText(t.getWording());
        } else {
            txtv_task.setPaintFlags(0); //Remet le texte sans barre
            txtv_task.setText(t.getWording());
        }
        cbx.setChecked(t.getDone());
        tasksDAO.updateTask(t);
    }


    /* Change la couleur selon la couleur selectionnée ! Ce sont tous des OnClick....*/
    public void goGreen(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdGreen;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
      l.setBackgroundResource(R.color.bckgrdGreen);
    }
    public void goBlue(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdBlue;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
        l.setBackgroundResource(color);
    }
    public void goYellow(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdYellow;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
        l.setBackgroundResource(R.color.bckgrdYellow);
    }
    public void goRed(View view) {
        ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdRed;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
        l.setBackgroundResource(color);
    }
    public void goWhite(View view) {
         ConstraintLayout l = findViewById(R.id.mainEdition);
        Integer color = R.color.bckgrdWhite;
        item.setBackground_color(color.toString());
        itemsDAO.updateItem(item);
        l.setBackgroundResource(R.color.bckgrdWhite);
    }

//Refresh l'activité lors d'un restart pour éviter une incohérence des données
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
