package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {

    private final ArrayList<String> myToDoList;
    private LayoutInflater myInflater;

    public ToDoListAdapter(Context context, ArrayList<String> toDoList) {
        myInflater = LayoutInflater.from(context);
        this.myToDoList = toDoList;
    }


    @Override
    public ToDoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myItemView = myInflater.inflate(R.layout.todolist_item, parent, false);
        return new ToDoListViewHolder(myItemView, this);
    }

    @Override
    public void onBindViewHolder(ToDoListAdapter.ToDoListViewHolder holder, int position) {
        String myCurrent = myToDoList.get(position);
        holder.toDoItemView.setText(myCurrent);
    }

    @Override
    public int getItemCount() {
        return myToDoList.size();
    }

    class ToDoListViewHolder extends RecyclerView.ViewHolder {

        public final TextView toDoItemView;
        final ToDoListAdapter myAdapter;

        ToDoListViewHolder(View itemView, ToDoListAdapter adapter) {
            super(itemView);
            toDoItemView = itemView.findViewById(R.id.todo);
            this.myAdapter = adapter;
            toDoItemView.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }));
        }
    }
}






