package com.example.todolist;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.menu.EditionTagFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import database.FeedReaderDbHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration myAppBarConfiguration;

    private final ArrayList<ItemToDo> myToDoList = new ArrayList<>();
    private RecyclerView myRecyclerView;
    private ToDoListAdapter myAdapter;
    private FeedReaderDbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        HashMap<Long, List> dbItemList = new HashMap<Long, List>();

        dbItemList = mHelper.readAllTasks();


        for (long i = 1; i < dbItemList.size(); i++) {

            dbItemList.get(i);
           ArrayList<String> myItemList = new ArrayList<>(dbItemList.get(i));
           myItemList.add(dbItemList.get(i).get(0).toString());
           myItemList.add(dbItemList.get(i).get(1).toString());
           myItemList.add(dbItemList.get(i).get(2).toString());

            myToDoList.add(new ItemToDo("Titre" + i, myItemList, R.drawable.img_addapicture));

        }

//        for (long i = 0; i < dbItemList.size(); i++) {
//            ArrayList<String> myItemList = new ArrayList<>();
//            myItemList.add("Item_1");
//            myItemList.add("Item_2");
//            myItemList.add("Item_3");
//            myToDoList.add(new ItemToDo("Titre" + i, myItemList, R.drawable.img_addapicture));
//
//        }
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
        startActivity(intent);
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
                        String task = String.valueOf(taskEditText.getText());
                        mHelper.insertFakeData();
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
        dialog.show();
    }

}