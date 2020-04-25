package com.project.todolist.adapters;

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

import com.project.todolist.R;
import com.project.todolist.model.Task;

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

    /* Si la tâche est "done"(faite) donc si done = true, alors on la checkbox doit être déjà sous "checked" et le texte doit être barrée. Sinon, c'est l'inverse.*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = (Task) getItem(position); //Savoir la position où nous nous trouvons dans la liste
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        TextView name = convertView.findViewById(R.id.txtv_task);
        CheckBox checkBox = convertView.findViewById(R.id.chkBox);
        /*  Si la tâche est effecutée, alors on affiche le tâche cochée et barrée, sinon on l'affiche normalement. */
        if(task.getDone() == true){
            checkBox.setText(task.getId().toString()); //Je met l'ID ici afin de pouvoir faire des modifications. L'ID n'est pas visible pour l'utilisateur.
            name.setPaintFlags(name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            name.setText(task.getWording());
            checkBox.setChecked(true);
        } else {
            checkBox.setText(task.getId().toString());
            checkBox.setChecked(false);
            name.setPaintFlags(0);
            name.setText(task.getWording());
        }
        return convertView;

    }
    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

}
