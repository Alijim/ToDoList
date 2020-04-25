package com.project.todolist.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.todolist.DAO.ItemsDAO;
import com.project.todolist.DAO.TagsDAO;
import com.project.todolist.DAO.TagsItemsDAO;
import com.project.todolist.DAO.TasksDAO;
import com.project.todolist.R;
import com.project.todolist.adapters.TagListAdapter;
import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;

import java.util.ArrayList;
import java.util.List;

import database.FeedReaderDbHelper;

public class EditionTagActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private List<Tag> tags;
    private List<Tag> tagsItem;
    private Item item;
    private Boolean isItem;
    private List<String> tagList;
    private List<String> listTag;
    private ArrayAdapter<String> itemsAdapter;
    private TagListAdapter tagListAdapter;
    private Boolean parentIsActivityA;
    private TasksDAO tasksDAO;
    private TagsDAO tagsDAO;
    private TagsItemsDAO tagsItemsDAO;
    private ItemsDAO itemsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);

        /*  Initialisation des ressources */
        Button btnAdd = findViewById(R.id.btn_AddTag);
        EditText edtx = findViewById(R.id.edtxt_Tag);
        TextView textViewTag = findViewById(R.id.txtv_DisplayTxtTag);
        ListView lv = findViewById(R.id.lv_Tag);

        /*  Initialisation des variables globales */
        this.tags = new ArrayList<Tag>();
        this.tagsItem = new ArrayList<Tag>();
        this.tagList = new ArrayList<String>();

        itemsDAO = new ItemsDAO(this);
        tasksDAO = new TasksDAO(this);
        tagsDAO = new TagsDAO(this);
        tagsItemsDAO = new TagsItemsDAO(this);
        this.isItem = Boolean.FALSE;

        Bundle extras = getIntent().getExtras(); //Récupération des valeurs passés dans l'intent (acitivité précédente)
        parentIsActivityA = Boolean.FALSE; //Booléen qui détermine qui est le parent.

        String itemName = extras.getString("name"); //récupère le nom de l'item(qui est unique) afin d'afficher les items

        tags = tagsDAO.getAllTags(); //Récupère la liste de tous les tags


        if (itemName != null) { //Si le nom n'est pas null, alors l'activité va seulement permettre à l'utilisateur d'ajouter/supprimer un tag à l'item.
            parentIsActivityA = Boolean.TRUE;
            this.isItem = Boolean.TRUE;
            item = itemsDAO.researchItem(itemName);
            textViewTag.setText("Gérer les tags de : " + item.getTitle());
            /* Désactive les ressources permettant d'ajouter un tag*/
            btnAdd.setVisibility(View.INVISIBLE);
            edtx.setVisibility(View.INVISIBLE);
        }


        /* Récupère soit tous les tags, soit les tags associés à l'item.*/
        if (itemName != null) {
            tagsItem = tagsDAO.getTagFromItem(item.getId());
            if (tagsItem.size() >= 1) {
                for (Tag t : tagsItem) {
                    tagList.add(t.getWording());
                }
            }
        } else {
            if (tags.size() >= 1) {
                for (Tag t : tags) {
                    tagList.add(t.getWording());
                }
            }
        }
        /* Appelle de l'adapter permettant d'afficher les tags avec le comportement de boutons qui va différer (ajouter/supprimer)*/
        tagListAdapter = new TagListAdapter(this, tags, item);
        /*Lors de l'édition, affiche simplement à l'aide d'un ArrayAdapter, vu qu'on a besoin que des noms. L'option ajouter/supprimer n'a pas de sens ici.*/
        itemsAdapter = new ArrayAdapter<String>(this, R.layout.tag_item, R.id.txtv_TagItem, tagList);

        if (itemName != null) {
            lv.setAdapter(tagListAdapter);

        } else {
            lv.setAdapter(itemsAdapter);
        }

    }

    /* Créer un nouveau tag lorsqu'on appuie sur le bouton "Ajouter"*/
    public void onClickCreateNewTag(View view) {

        if (item != null) {
            Tag t = new Tag();
            EditText tv = findViewById(R.id.edtxt_Tag);
            t.setWording(tv.getText().toString());
            Integer id = tagsDAO.insertTag(t);
            Tag tt = new Tag(id, t.getWording());
            tagsItemsDAO.insertTagItems(item, tt);
            tags.add(tt);
            tagListAdapter.add(t.getWording());
            tagListAdapter.notifyDataSetChanged();
            tv.getText().clear();
        } else {
            Tag t = new Tag(); //création d'un tag sans constructeur, il servira à alimenter et récupérer l'ID.
            EditText tv = findViewById(R.id.edtxt_Tag);
            if (tv.getText().toString().isEmpty()) { //Si le contenu est vide, alors on affiche un message d'erreur
                Toast toast = Toast.makeText(getApplicationContext(), "Tag vide ! ", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                t.setWording(tv.getText().toString());
                if(tagsDAO.existTag(t.getWording())) { //Si le tag existe déjà, alors on affiche un message d'erreur
                    Toast toast = Toast.makeText(getApplicationContext(), "Tag existe déjà! ", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                Integer id = tagsDAO.insertTag(t);
                Tag tt = new Tag(id, t.getWording()); //Création du tag qui sera envoyé, vu que setId n'est pas implémenté
                tags.add(tt);
                itemsAdapter.add(t.getWording());
                itemsAdapter.notifyDataSetChanged(); //Refresh l'affichage des Tags
                tv.getText().clear();
                }
            }
        }

    }

    /*  Permet de mettre à jour un tag en cliquant dessus.*/
    public void onClickUpdateTag(View view) {
        if (item != null) { //Si la page n'est pas en mode "édition", alors il est impossible de modifier le Tag.
            Toast toast = Toast.makeText(getApplicationContext(), "Rendez-vous dans le menu d'édition pour modifier ! ", Toast.LENGTH_SHORT);
            toast.show();
        } else { //Si la page est en mode "édition", on peut alors modifier un tag.
            View parent = (View) view.getParent();
            TextView txv_tag = parent.findViewById(R.id.txtv_TagItem);
            final EditText taskEditText = new EditText(this);
            Tag t = new Tag();

            /* Affiche un AlertDialog qui va permettre de saisir le nouveau nom du Tag */
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Modifier le tag")
                    .setMessage("Entrez le nouveau nom du tag")
                    .setView(taskEditText)
                    .setPositiveButton("Modifier", new DialogInterface.OnClickListener() { //Lorsqu'on appuie sur le bouton "Modifier"
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tag = String.valueOf(taskEditText.getText());

                            if (taskEditText.getText().toString().isEmpty()) { //Si c'est vide, on ajoute pas...
                                Toast toast = Toast.makeText(getApplicationContext(), "Labelle vide ! ", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                Integer i = tagsDAO.getIdFromTitle(txv_tag.getText().toString());
                                tagsDAO.updateTagFromWording(i, tag);
                                tagList.remove(txv_tag.getText().toString());
                                tagList.add(tag);
                                itemsAdapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .create();
            dialog.show();
        }

    }

    /* Ici la logique va dépendre de plusieurs paramètres.
    *  Si on est en mode "Edition" alors on va afficher :
    *  - "Ajouter" si l'association avec l'item n'existe pas, ça va la créer.
    *  - "Supprimer" si l'association avec l'item existe, on va alors supprimer cette association.
    * Si on est en mode "gestion de tags", alors le bouton supprimer aura comme comportement
    * de supprimer le tag dans la table Tags.
    *  */
    public void onClickAddOrDeleteTag(View view) {
        /*Récupère les éléments du parents, afin qu'on ait accès aux ressources de la TagList affichée*/
        View parent = (View) view.getParent();
        TextView tag = (TextView) parent.findViewById(R.id.txtv_TagItem);
        Button btn = (Button) parent.findViewById(R.id.btn_DeleteTag);
        String t = String.valueOf(tag.getText());
        Integer index = tags.indexOf(t);
        if (isItem) { //Si l'item existe, on va alors supprimer les associations entre Tags et Items
            Tag tDelete = new Tag();
            if (btn.getText().toString().equals("Supprimer")) { //Si le bouton affiche supprimer, alors on va supprimer l'association !
                for (Tag tagg : tags) {
                    if (tagg.getWording().equals(t)) { //On recherche le Tag...
                        tDelete = tagg;
                    }
                }
                tagsItem.remove(tDelete);
                Integer i = tagsItemsDAO.deleteTagItem(item.getId(), tDelete.getId());
                //On change le bouton en "Ajouter", vu qu'on l'a supprimer
                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdGreen));
                btn.setText("Ajouter");

            } else { //Si le bouton est "Ajouter" on va faire le comportement inverse ! C'est à dire ajouter l'association et changer le bouton en "Supprimer"
                Tag tAdd = new Tag();

                tAdd.setWording(tag.getText().toString());
                for (Tag tTemp : tags) {
                    if (tTemp.getWording().equals(tAdd.getWording())) {
                        tAdd = tTemp;
                    }
                }
                tagsItem.add(tAdd);
                tagsItemsDAO.insertTagItems(item, tAdd);
                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdRed));
                btn.setText("Supprimer");
            }
        } else { //Si on se retrouve ici, c'est qu'on est en mode "édition" vu que l'item n'est pas définit.
            Tag tDelete = new Tag();
            tagList.remove(tagList.indexOf(t));
            if (btn.getText().toString().equals("Supprimer")) {
                for (Tag tagg : tags) {
                    if (tagg.getWording().equals(t)) {
                        tDelete = tagg;
                    }
                }

                tags.remove(tDelete); //On supprime le tag des tags et non pas l'association Tag/Item

                tagsDAO.deleteTag(tDelete.getId());
                itemsAdapter.remove(t);
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }

    /* Les fonctions ci-dessous permettent de déterminer qui est l'Activité parente
    *  On va alors adapter le comportement de cette page, selon l'activité parente
    * Si c'est l'activité principale (MainActivity) l'activité permet de gérer les tags dans
    * la base de données.
    * Si c'est l'activité d'édition des tâches (TaskEditionActivity) alors, l'activté est en mode
    * "édition" et va gérer l'association entre l'item et les tags.
    * */
    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImpl();
    }


    private Intent getParentActivityIntentImpl() {
        Intent i = null;

        if (parentIsActivityA) {
            i = new Intent(this, TaskEditionActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("name", this.item.getTitle());

        } else {
            i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        return i;
    }

    /* Refresh le contenu*/
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
