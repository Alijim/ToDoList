package com.project.todolist.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;
import com.project.todolist.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import database.FeedReaderContract;
import database.FeedReaderDbHelper;

public class ItemsDAO {

    private FeedReaderDbHelper mHelper;
    private Context context;
    private FeedReaderContract mContract;

    public ItemsDAO(Context c) {
        this.mHelper = new FeedReaderDbHelper(c);
        this.context = c;
    }

    /*  Petit lexique pour comprendre le fonctionnement :
    *   - projection    : les colonnes que l'on veut récupérer
    *   - selection     : condition WHERE
    *   - selectionArgs : Arguments de la condition
    *   - sortOrder     : comment on veut trier nos données.
    *   Le cursoor contient les résultats de la requête.
    * */

    public void insert(Item i) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues valuesItems = new ContentValues();
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, i.getTitle());
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, i.getDeadline());
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, i.getImage());
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR, i.getBackground_color());

        long itemsRow1 = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);
        db.close();
    }

    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<Item>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        TasksDAO tasksDAO = new TasksDAO(context);
        TagsDAO tagsDAO = new TagsDAO(context);

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR
        };

        String sortOrder =
                FeedReaderContract.ItemsEntry._ID + " DESC ";

        Cursor cursor = db.query(
                FeedReaderContract.ItemsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.ItemsEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            List<Task> tasks = tasksDAO.getTasksFromItem(id);
            List<Tag> tags = tagsDAO.getTagFromItem(id);
            Long deadLine = cursor.getLong(cursor.getColumnIndex("deadLine"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String color = cursor.getString(cursor.getColumnIndex("background_color"));
            Item i = new Item(id, title, deadLine, tasks, tags, image, color);
            itemList.add(i);
        }
        db.close();
        return itemList;
    }

    public Item researchItem(String title){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        TagsDAO tagsDAO = new TagsDAO(context);
        TasksDAO tasksDAO = new TasksDAO(context);

        Item i_n = new Item();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE
        };

        String selection = FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {  title };

        String sortOrder =
                FeedReaderContract.ItemsEntry._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.ItemsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
            List<Task> tasks = tasksDAO.getTasksFromItem(id);
            List<Tag> tags = tagsDAO.getTagFromItem(id);
            Long deadLine = cursor.getLong(cursor.getColumnIndex("deadLine"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String color = cursor.getString(cursor.getColumnIndex("background_color"));
            Item i = new Item(id, title, deadLine, tasks, tags, image, color);
            return i;

        }
        db.close();
        return i_n;

    }

    public Boolean existItem(String title){
        Boolean b = Boolean.FALSE;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE
        };

        String selection = FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {  title };

        String sortOrder =
                FeedReaderContract.ItemsEntry._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.ItemsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            b = Boolean.TRUE;
        }
        db.close();
        return b;
    }

    public void updateItem(Item i) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, i.getTitle());
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, i.getDeadline());
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, i.getImage());
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR, i.getBackground_color());

        String selection = FeedReaderContract.ItemsEntry._ID + " =  "+i.getId().toString();

        int count = db.update(
                FeedReaderContract.ItemsEntry.TABLE_NAME,
                values,
                selection,
                null);
        db.close();
    }

    public void deleteItemById(Integer id) {

        SQLiteDatabase db = mHelper.getWritableDatabase();

        TasksDAO tasksDAO = new TasksDAO(context);
        TagsItemsDAO tagsItemsDAO = new TagsItemsDAO(context);

        List<Integer> l = new ArrayList<Integer>(tasksDAO.getTasksIdFromItem(id));

        for(Integer i : l) {
            tasksDAO.deleteTask(i);
        }

        String selection = FeedReaderContract.ItemsEntry._ID + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.ItemsEntry.TABLE_NAME, selection, selectionArgs);

        tagsItemsDAO.deleteItemInTagsItem(id);
        db.close();
    }




}
