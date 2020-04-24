package com.project.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

        Button btnAdd = findViewById(R.id.btn_AddTag);
        EditText edtx = findViewById(R.id.edtxt_Tag);


        TextView textViewTag = findViewById(R.id.txtv_DisplayTxtTag);
        Bundle extras = getIntent().getExtras();
        mHelper = new FeedReaderDbHelper(this);
        itemsDAO = new ItemsDAO(this);
        tasksDAO = new TasksDAO(this);
        tagsDAO = new TagsDAO(this);
        tagsItemsDAO = new TagsItemsDAO(this);

        parentIsActivityA = Boolean.FALSE;


        String txt = extras.getString("name");
        this.isItem = Boolean.FALSE;
        if(txt != null){
            parentIsActivityA = Boolean.TRUE;
            this.isItem = Boolean.TRUE;
            item = itemsDAO.researchItem(txt);
            textViewTag.setText("Gérer les tags de : "+item.getTitle());
            btnAdd.setVisibility(View.INVISIBLE);
            edtx.setVisibility(View.INVISIBLE);
        }

        this.tags = new ArrayList<Tag>();
        this.tagsItem = new ArrayList<Tag>();
        this.tagList = new ArrayList<String>();
        tags = tagsDAO.getAllTags();
        if(txt != null){
           tagsItem = tagsDAO.getTagFromItem(item.getId());
            if(tagsItem.size() >= 1) {
                for(Tag t : tagsItem) {
                    tagList.add(t.getWording());
                }
            }
        } else {
            if(tags.size() >= 1) {
                for(Tag t : tags) {
                    tagList.add(t.getWording());
                }
            }
        }

        tagListAdapter = new TagListAdapter(this, tags, item);

//        tagList.add("Coucou");
            itemsAdapter = new ArrayAdapter<String>(this,R.layout.tag_item, R.id.txtv_TagItem, tagList);



        ListView lv = findViewById(R.id.lv_Tag);

        if(txt != null) {
            lv.setAdapter(tagListAdapter);

        } else {
            lv.setAdapter(itemsAdapter);
        }

    }


    public void createNewTag(View view) {

        if(item != null){
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
            Tag t = new Tag();
            EditText tv = findViewById(R.id.edtxt_Tag);

            t.setWording(tv.getText().toString());
            Integer id = tagsDAO.insertTag(t);
            Tag tt = new Tag(id, t.getWording());
            tags.add(tt);
            itemsAdapter.add(t.getWording());
            itemsAdapter.notifyDataSetChanged();
            tv.getText().clear();
        }

    }


    public void deleteTag(View view) {
        View parent = (View) view.getParent();
        TextView tag = (TextView) parent.findViewById(R.id.txtv_TagItem);
        Button btn = (Button) parent.findViewById(R.id.btn_DeleteTag);
        String t = String.valueOf(tag.getText());
        Integer index = tags.indexOf(t);

        if(isItem) {
            Tag tDelete = new Tag();
//            tagList.remove(tagList.indexOf(t));
            if(btn.getText().toString().equals("Supprimer")) {
                for(Tag tagg : tags) {
                    if(tagg.getWording().equals(t)) {
                        tDelete = tagg;
//                tags.remove(tags.indexOf(tagg));
                    }
                }

                tagsItem.remove(tDelete);
               Integer i = tagsItemsDAO.deleteTagItem(item.getId(), tDelete.getId());
//                tagListAdapter.notifyDataSetChanged();
                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdGreen));
                btn.setText("Ajouter");

            } else {
                Tag tAdd = new Tag();

                tAdd.setWording(tag.getText().toString());
//                Integer id = mHelper.insertTagItems(item.getId(), );
                for(Tag tTemp : tags) {
                    if(tTemp.getWording().equals(tAdd.getWording())) {
                        tAdd = tTemp;
                    }
                }
//                tAdd = tags.(tag.getText().toString());
//                Tag tat = new Tag(id, tAdd.getWording());
                tagsItem.add(tAdd);
                tagsItemsDAO.insertTagItems(item, tAdd);
//                tagListAdapter.notifyDataSetChanged();

//                ListView lv = findViewById(R.id.lv_Tag);
//                tagListAdapter = new TagListAdapter(this, tags, item);
//                lv.setAdapter(tagListAdapter);
//                tagListAdapter.add(tAdd);
//                tagListAdapter.notifyDataSetChanged();
//                tv.getText().clear();
                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdRed));
                btn.setText("Supprimer");
            }
        } else {
            Tag tDelete = new Tag();
            tagList.remove(tagList.indexOf(t));
            if(btn.getText().toString().equals("Supprimer")) {
                for(Tag tagg : tags) {
                    if(tagg.getWording().equals(t)) {
                        tDelete = tagg;
//                tags.remove(tags.indexOf(tagg));
                    }
                }

                tags.remove(tDelete);

                tagsDAO.deleteTag(tDelete.getId());
                itemsAdapter.remove(t);
                itemsAdapter.notifyDataSetChanged();
//                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdGreen));
//                btn.setText("Ajouter");
           }
//            else {
//                Tag tAdd = new Tag();
//
//                tAdd.setWording(tag.getText().toString());
////                Integer id = mHelper.insertTagItems(item.getId(), );
//                for(Tag tTemp : tags) {
//                    if(tTemp.getWording().equals(tAdd.getWording())) {
//                        tAdd = tTemp;
//                    }
//                }
////                tAdd = tags.(tag.getText().toString());
////                Tag tat = new Tag(id, tAdd.getWording());
//                tagsItem.add(tAdd);
//                mHelper.insertTagItems(item, tAdd);
//                itemsAdapter.add(tAdd.getWording());
//                itemsAdapter.notifyDataSetChanged();
////                tv.getText().clear();
//                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdRed));
//                btn.setText("Supprimer");
//            }
        }

//        View parent = (View) view.getParent();
//        Tag tDelete = new Tag();
//        TextView tag = (TextView) parent.findViewById(R.id.txtv_TagItem);
//        Button btn = (Button) parent.findViewById(R.id.btn_DeleteTag);
//        String t = String.valueOf(tag.getText());
//        Integer index = tags.indexOf(t);
//        tagList.remove(tagList.indexOf(t));
//        if(btn.getText().toString().equals("Supprimer")) {
//            for(Tag tagg : tags) {
//                if(tagg.getWording().equals(t)) {
//                    tDelete = tagg;
////                tags.remove(tags.indexOf(tagg));
//                }
//            }
//
//            tags.remove(tDelete);
//
//            mHelper.deleteTag(tDelete.getId());
//            itemsAdapter.remove(t);
//            itemsAdapter.notifyDataSetChanged();
//
//        }
//
//
//        for(Tag tagg : tags) {
//            if(tagg.getWording().equals(t)) {
//                tDelete = tagg;
////                tags.remove(tags.indexOf(tagg));
//            }
//        }
//
//        tags.remove(tDelete);
//
//
//        mHelper.deleteTag(tDelete.getId());
//        itemsAdapter.remove(t);
//        itemsAdapter.notifyDataSetChanged();
//
////
//       TextView tv = view.findViewById(R.id.txtv_TagItem);
//       TextView tvv = findViewById(R.id.txtv_TagItem);
//
//       String t = tvv.getText().toString();
//
//       t += " !";




    }

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

        // Here you need to do some logic to determine from which Activity you came.
        // example: you could pass a variable through your Intent extras and check that.
        if (parentIsActivityA) {
            i = new Intent(this, TaskEditionActivity.class);

            // set any flags or extras that you need.
            // If you are reusing the previous Activity (i.e. bringing it to the top
            // without re-creating a new instance) set these flags:
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // if you are re-using the parent Activity you may not need to set any extras
            i.putExtra("name", this.item.getTitle());
//            startActivityForResult(i, 1);

        } else {
            i = new Intent(this, MainActivity.class);
            // same comments as above
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("someExtra", "whateverYouNeed");
        }

        return i;
    }
}
