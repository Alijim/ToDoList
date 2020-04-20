package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.model.Item;
import com.example.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.FeedReaderDbHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration myAppBarConfiguration;

    private final List<Item> myToDoList = new ArrayList<Item>();
    private RecyclerView myRecyclerView;
    private ToDoListAdapter myAdapter;
    private FeedReaderDbHelper mHelper;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new FeedReaderDbHelper(this);
        //mHelper.deleteAllData();
//        mHelper.insertFakeData();

//        Boolean bTest = Boolean.FALSE;
//        Boolean bTrue = Boolean.FALSE;
//
//        bTest = mHelper.isTagItem(10, 17);
//        bTrue = mHelper.isTagItem(10, 20);
//
//        String s = ";";

//        Integer delete = mHelper.deleteTagItem(2,2);
//
//        String s = "";

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Task t = new Task(2, "salut", true);
//        mHelper.updateTask(t);

        FloatingActionButton fab = findViewById(R.id.fab);

        // Get a handle to the RecyclerView.
        myRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        myAdapter = new ToDoListAdapter(this, myToDoList);
        // Connect the adapter with the RecyclerView.
        // L'adapter est un composant qui permet de faire la liaison (Bind) entre la vue RecyclerView et une liste de données.
        myRecyclerView.setAdapter(myAdapter);
        // Give the RecyclerView a default layout manager.
        // Le LayoutManager permet de positionner correctement l'ensemble des données de la liste.
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mHelper.testDate();

        this.initialisationData();

        //Creation du menu
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        myAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_todolist).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, myAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }



    //Méthode à redéfinir avec les données de la BD
    public void initialisationData() {

        mHelper = new FeedReaderDbHelper(this);

        HashMap<Integer, List> dbItemList = new HashMap<Integer, List>();

        this.items = mHelper.getAllItems();



        for (Item i : items) {
            i.setImageRessource(R.drawable.img_addapicture);

            myToDoList.add(i);

        }
   }

    //Active le bouton de la barre de navigation (les 3 trais horizontaux)
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, myAppBarConfiguration) || super.onSupportNavigateUp();
    }

    //Activité lancé lorsqu'on clique sur le fab+ ou sur une carte
    public void launchTaskEditionActivity(View view) {
        Intent intent = new Intent(this, TaskEditionActivity.class);
        TextView txt = view.findViewById(R.id.cv_Title);
        intent.putExtra("name", txt.getText());

        startActivity(intent);
    }

    public void launchTaskEditionActivityF(String s) {
        Intent intent = new Intent(this, TaskEditionActivity.class);
        intent.putExtra("name", s);

        startActivity(intent);
    }

    public void refreshUI() {
        finish();
        startActivity(getIntent());
    }


    public void launchEditionDialog(View v) {
        mHelper = new FeedReaderDbHelper(this);

        // Création d'un alert dialog pour l'ajout d'une tâche
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Ajouter une nouvelle tâche")
                .setMessage("Créer votre tâche")
                .setView(taskEditText)
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Item i = new Item();
                        i.setTitle(String.valueOf(taskEditText.getText()));
                        Integer color = R.color.bckgrdWhite;
                        i.setBackground_color(color.toString());
//                        i.setBackground_color(R.color.bckgrdWhite);
                        mHelper.insertIntoItems(i);
                        launchTaskEditionActivityF(i.getTitle());
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
        dialog.show();
    }

}