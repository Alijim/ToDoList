package com.example.todolist.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todolist.R;
import com.example.todolist.model.Tag;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.FeedReaderDbHelper;

public class EditionTagActivity extends AppCompatActivity {

    private FeedReaderDbHelper mHelper;
    private List<Tag> tags;
    private List<String> tagList;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);
        Button btnAdd = findViewById(R.id.btn_AddTag);
        mHelper = new FeedReaderDbHelper(this);
        this.tags = new ArrayList<Tag>();
        this.tagList = new ArrayList<String>();
        tags = mHelper.getAllTags();

        if(tags.size() >= 1) {
            for(Tag t : tags) {
                tagList.add(t.getWording());
            }
        }
        tagList.add("Coucou");

        itemsAdapter = new ArrayAdapter<String>(this,R.layout.tag_item, R.id.txtv_TagItem, tagList);


        ListView lv = findViewById(R.id.lv_Tag);
        lv.setAdapter(itemsAdapter);



    }


    public void createNewTag(View view) {
        Tag t = new Tag();
        EditText tv = findViewById(R.id.edtxt_Tag);

        t.setWording(tv.getText().toString());
        Integer id = mHelper.insertTag(t);
        Tag tt = new Tag(id, t.getWording());
        tags.add(tt);
        itemsAdapter.add(t.getWording());
        itemsAdapter.notifyDataSetChanged();
        tv.getText().clear();
    }


    public void deleteTag(View view) {
        View parent = (View) view.getParent();
        Tag tDelete = new Tag();
        TextView tag = (TextView) parent.findViewById(R.id.txtv_TagItem);
        String t = String.valueOf(tag.getText());
        Integer index = tags.indexOf(t);
        tagList.remove(tagList.indexOf(t));



        for(Tag tagg : tags) {
            if(tagg.getWording().equals(t)) {
                tDelete = tagg;
//                tags.remove(tags.indexOf(tagg));
            }
        }

        tags.remove(tDelete);


        mHelper.deleteTag(tDelete.getId());
        itemsAdapter.remove(t);
        itemsAdapter.notifyDataSetChanged();

//
//       TextView tv = view.findViewById(R.id.txtv_TagItem);
//       TextView tvv = findViewById(R.id.txtv_TagItem);
//
//       String t = tvv.getText().toString();
//
//       t += " !";




    }
}
