package com.project.todolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.todolist.DAO.TagsItemsDAO;
import com.project.todolist.R;
import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;

import java.util.List;

import database.FeedReaderDbHelper;

public class TagListAdapter extends ArrayAdapter {
    private Context context;
    private Item item;
    private List<Tag> allTags;
    private FeedReaderDbHelper mHelper;
    private List<Boolean> checkboxState;
    private List<Integer> itemsState;
    private List<String> checkboxItems;
    private TagsItemsDAO tagsItemsDAO;

    public TagListAdapter(Context context, List<Tag> resource, Item i) {
        super(context, 0, resource);

        this.context = context;
        this.item = i;
        this.allTags = resource;
        this.tagsItemsDAO = new TagsItemsDAO(context);
//        mHelper = new FeedReaderDbHelper(context);


        //this.checkboxItems = resource;
        //this.checkboxState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), true));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tag t = (Tag) getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_item, parent, false);
        }
        TextView name = convertView.findViewById(R.id.txtv_TagItem);
        Button btn = convertView.findViewById(R.id.btn_DeleteTag);
        List<Tag> itemTag = allTags;

            if(tagsItemsDAO.isTagItem(item.getId(), t.getId())) {
                name.setText(t.getWording());
                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdRed));
                btn.setText("Supprimer");
            } else {
                btn.setBackgroundColor(btn.getContext().getResources().getColor(R.color.bckgrdGreen));
                name.setText(t.getWording());
                btn.setText("Ajouter");
            }

        return convertView;

    }
}
