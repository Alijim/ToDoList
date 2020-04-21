package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.model.Item;
import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckBoxAdapter extends ArrayAdapter {

    Context context;
    List<Boolean> checkboxState;
    List<Integer> itemsState;
    List<String> checkboxItems;

    public CheckBoxAdapter(Context context, List<Task> resource) {
        super(context, 0, resource);

        this.context = context;
        //this.checkboxItems = resource;
        //this.checkboxState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), true));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = (Task) getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        TextView name = convertView.findViewById(R.id.txtv_task);
        CheckBox checkBox = convertView.findViewById(R.id.chkBox);
//        name.setText(task.getWording());
        if(task.getDone() == true){
            checkBox.setText(task.getId().toString());
            name.setPaintFlags(name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            name.setText(task.getWording());
            checkBox.setChecked(true);
        } else {
            name.setPaintFlags(0);
            name.setText(task.getWording());
            checkBox.setText(task.getId().toString());
            checkBox.setChecked(false);
        }
        return convertView;

    }

    /*public CheckBoxAdapter(Context context, List<String> resource, List<Integer> state) {
        super(context, R.layout.item_todo, resource);

        this.context = context;
        this.checkboxItems = resource;
//        this.checkboxState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), true));
        this.itemsState = state;
    }


    public void matchItemState() {


//        for(checkboxItems.size()) {
//            TextView textView = (TextView) convertView.findViewById(R.id.txtv_task);
//            CheckBox cb = (CheckBox) convertView.findViewById(R.id.chkBox);
//        }
    }


    public  View getView(int position, View convertView) {

        TextView textView = (TextView) convertView.findViewById(R.id.txtv_task);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.chkBox);

        textView.setText(checkboxItems.get(position));
        cb.setChecked(checkboxState.get(position));
        return convertView;
    }

    void setChecked(boolean state, int position) {
        checkboxState.set(position, state);
    }*/
}
