package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckBoxAdapter extends ArrayAdapter {

    Context context;
    List<Boolean> checkboxState;
    List<Integer> itemsState;
    List<String> checkboxItems;

    public CheckBoxAdapter(Context context, List<String> resource) {
        super(context, 0, resource);

        this.context = context;
        //this.checkboxItems = resource;
        //this.checkboxState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), true));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);


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
