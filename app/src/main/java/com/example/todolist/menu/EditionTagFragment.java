package com.example.todolist.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.TagListActivity;
import com.example.todolist.TaskEditionActivity;

public class EditionTagFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_tag_list, container, false);
        return root;
    }


}
