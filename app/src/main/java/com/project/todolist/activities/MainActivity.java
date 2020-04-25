package com.project.todolist.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.project.todolist.R;
import com.project.todolist.adapters.ToDoListAdapter;
import com.project.todolist.model.Item;
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

import database.FeedReaderDbHelper;
import com.project.todolist.DAO.ItemsDAO;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration myAppBarConfiguration;

    private final List<Item> myToDoList = new ArrayList<Item>();
    private RecyclerView myRecyclerView;
    private ToDoListAdapter myAdapter;
    private List<Item> items;
    private ItemsDAO itemsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItemsDAO iDAO = new ItemsDAO(this);
        this.itemsDAO = iDAO;

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        setSupportActionBar(toolbar);



        myRecyclerView = findViewById(R.id.recyclerview);
        // Créer l'adaptateur qui va afficher les valeurs de la todolist
        myAdapter = new ToDoListAdapter(this, myToDoList);
        // L'adapter est un composant qui permet de faire la liaison (Bind) entre la vue RecyclerView et une liste de données.
        myRecyclerView.setAdapter(myAdapter);
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
        ItemsDAO iDAO = new ItemsDAO(this);
        this.items = iDAO.getAllItems();

        for (Item i : items) {
            myToDoList.add(i);

        }
   }

    //Active le bouton de la barre de navigation (les 3 trais horizontaux)
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, myAppBarConfiguration) || super.onSupportNavigateUp();
    }

    //Activité lancé lorsqu'on clique sur le fab+ ou sur une carte, elle permet de lancer l'activité d'édition de l'item sélectionné
    public void launchTaskEditionActivity(View view) {
        Intent intent = new Intent(this, TaskEditionActivity.class);
        TextView txt = view.findViewById(R.id.cv_Title);
        intent.putExtra("name", txt.getText());
        startActivity(intent);
    }

    //Même comportement que précédement, sauf qu'ici on lui passe un String en paramètre, vu qu'elle est appelée depuis le bouton flottant
    public void launchTaskEditionActivityF(String s) {
        Intent intent = new Intent(this, TaskEditionActivity.class);
        intent.putExtra("name", s);
        startActivity(intent);
    }

    public void launchEditionDialog(View v) {
//        mHelper = new FeedReaderDbHelper(this);
        ItemsDAO iDAO = new ItemsDAO(this);
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
                        if(taskEditText.getText().toString().isEmpty()) {
                            i.setTitle("Nouvelle tâche");
                        } else {
                            i.setTitle(String.valueOf(taskEditText.getText()));
                        }
                        Integer color = R.color.bckgrdWhite;
                        i.setBackground_color(color.toString());
//                        i.setBackground_color(R.color.bckgrdWhite);
                        itemsDAO.insert(i);
                        launchTaskEditionActivityF(i.getTitle());
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
        dialog.show();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}