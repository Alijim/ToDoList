package com.project.todolist.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.project.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

import database.FeedReaderContract;
import database.FeedReaderDbHelper;

public class TasksDAO {

    private FeedReaderDbHelper mHelper;
    private Context context;
    private FeedReaderContract mContract;


    public TasksDAO(Context c) {
        this.mHelper = new FeedReaderDbHelper(c);
        this.context = c;

    }

    public long insertTask(Integer id, Task t) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues valuesTasks = new ContentValues();

        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, t.getWording());
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, t.getDone());
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, id);
        long tasksRow1 = db.insert(FeedReaderContract.TaskEntry.TABLE_NAME, null, valuesTasks);
        return  tasksRow1;
    }

    public List<Task> getTasksFromItem(Integer id) {
//        Integer id = getAnyID("Items", "Title", args);
        List values = new ArrayList<>();
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = mHelper.getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING,
                FeedReaderContract.TaskEntry.COLUMN_NAME_DONE
        };

        String selection = FeedReaderContract.TaskEntry.COLUMN_NAME_FK + " = ?";
        String[] selectionArgs = {  id.toString()};

        String sortOrder =
                FeedReaderContract.TaskEntry.COLUMN_NAME_DONE + " ASC ";

        Cursor cursor = db.query(
                FeedReaderContract.TaskEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

//        HashMap<Long, String>items = new HashMap<Long, String>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            Boolean b = Boolean.FALSE ;
            Integer idTask = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String wording = cursor.getString(cursor.getColumnIndex("wording"));
            b = cursor.getInt(cursor.getColumnIndex("done")) > 0;
//            Integer done = cursor.getInt(cursor.getColumnIndex("done"));
//            if(done == 1) {
//                 b = Boolean.FALSE;
//            } else {
//                 b = Boolean.FALSE;
//            }
            Task t = new Task(idTask, wording, b);
            taskList.add(t);
        }

        return taskList;

        //return items.toString();
    }

    public List<Integer> getTasksIdFromItem(Integer id) {
        String s = "";
        List values = new ArrayList<Integer>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TaskEntry._ID,
                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING
        };

        String selection = FeedReaderContract.TaskEntry.COLUMN_NAME_FK + " = ?";
        String[] selectionArgs = {  id.toString()};

        String sortOrder =
                FeedReaderContract.TaskEntry.COLUMN_NAME_DONE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.TaskEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            //long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.TaskEntry._ID));
            values.add(cursor.getInt(cursor.getColumnIndex("_id")));
            //items.put(itemId, cursor.getString(cursor.getColumnIndex("wording")));
        }

        return values;

    }

    public void updateTask(Task t) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, t.getWording());
        values.put(FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, t.getDone());

        String selection = FeedReaderContract.TaskEntry._ID + " =  "+t.getId().toString();

        int count = db.update(
                FeedReaderContract.TaskEntry.TABLE_NAME,
                values,
                selection,
                null);
    }

    public void deleteTask(Integer id){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String selection = FeedReaderContract.TaskEntry._ID + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }





}
