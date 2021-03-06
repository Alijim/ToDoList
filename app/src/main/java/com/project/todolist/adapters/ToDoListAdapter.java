package com.project.todolist.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.todolist.DAO.TagsDAO;
import com.project.todolist.R;
import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;
import com.project.todolist.model.Task;

import java.io.IOException;
import java.util.List;

import database.FeedReaderDbHelper;

import static androidx.core.content.ContextCompat.startActivities;

//L'adapter est un composant qui permet de faire la liaison (Bind) entre la vue RecyclerView et une liste de données.
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {

    private final List<Item> myToDoList;
    private LayoutInflater myInflater;
    private Context myContext;
    private FeedReaderDbHelper mHelper;
    private TagsDAO tagsDAO;

    //Constructeur
    public ToDoListAdapter(Context context, List<Item> toDoList) {
        myInflater = LayoutInflater.from(context);
        myContext = context;
        this.myToDoList = toDoList;
        tagsDAO = new TagsDAO(context);
//        mHelper = new FeedReaderDbHelper(context);
    }

    @Override
    public ToDoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myItemView = myInflater.inflate(R.layout.todolist_item, parent, false);
        return new ToDoListViewHolder(myItemView, this);
    }

    @Override
    public void onBindViewHolder(ToDoListAdapter.ToDoListViewHolder holder, int position) {
        Item myCurrent = myToDoList.get(position);
        holder.bindTo(myCurrent);
    }

    @Override
    public int getItemCount() {
        return myToDoList.size();
    }

    //Ce composant permet de représenter visuellement un élément de la liste de données dans le RecyclerView (Une ligne).
    class ToDoListViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        private TextView myTitre;
        private TextView myItem;
        private ImageView myImage;

        ToDoListViewHolder(View itemView, ToDoListAdapter adapter) {
            super(itemView);
            // Initialize the views.
            myTitre = itemView.findViewById(R.id.cv_Title);
            myItem = itemView.findViewById(R.id.Item);
            myImage = itemView.findViewById(R.id.Image);

            // Set the OnClickListener to the entire view.
            //itemView.setOnClickListener(this);
        }

        public void bindTo(Item myCurrent){
            // Populate the textviews with data.

                myTitre.setText(myCurrent.getTitle());
            Integer color = Integer.parseInt(myCurrent.getBackground_color());
            myTitre.getRootView().setBackgroundResource(color);



            String myListItem = "";
//            if(myCurrent.getDeadline() == null) {
//                myListItem += "";
//            } else {
//               myListItem += "\uD83D\uDCC5 Date de fin : "+myCurrent.getDeadline()+"\n";
//            }
            if (myCurrent.getListTasks() != null) {
                List<Task> myItemTab = myCurrent.getListTasks();
                StringBuilder sb = new StringBuilder();
                for(Task t : myItemTab){
                    if(t.getDone()) {
                        sb.append("☑ "+t.getWording()+"\n");

                    } else {
                        sb.append("□ "+t.getWording()+"\n");

                    }
                }
                myListItem += "\n"+sb.toString();
            }
            myListItem += tagsDAO.getTagFromItemListDisplay(myCurrent.getId());
            myItem.setText(myListItem);

            if(myCurrent.getImage() != null ) {
                String imgUri = myCurrent.getImage();
                Uri imgur = Uri.parse(imgUri);
                myImage.setImageURI(imgur);
            } else {
                myImage.setImageResource(R.drawable.img_addapicture);
            }


                // Load the images into the ImageView using the Glide library.
//            Glide.with(myContext).load(myCurrent.getImageRessource()).into(myImage);

//
//            myTitre.setText(myCurrent.getTitle());
//            String myListItem = "Vide";
//            if (myCurrent.getListTasks() != null) {
//                List myItemTab = myCurrent.getListTasks();
//                StringBuilder sb = new StringBuilder();
//                for (int i=0; i<myItemTab.size(); i++) {
//                    sb.append(myItemTab.get(i)+"\n");
//                }
//                myListItem = sb.toString();
//            }
//            myItem.setText(myListItem);
//            // Load the images into the ImageView using the Glide library.
//            Glide.with(myContext).load(myCurrent.getImage()).into(myImage);
        }

        // Ici on implémente l'action qui suit un click sur la cards
        /*@Override
        public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            // Mettre ici un Intent pour lancer TaskEditionActivity
            //Intent intent = new Intent(myContext, TaskEditionActivity.class);
            //startActivity(intent);
        }*/
    }
}







