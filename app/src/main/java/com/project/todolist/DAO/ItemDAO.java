package com.project.todolist.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.project.todolist.model.Item;

import database.FeedReaderContract;
import database.FeedReaderDbHelper;

public class ItemDAO {

    private FeedReaderDbHelper mHelper;
    private Context context;
    private FeedReaderContract mContract;

    public ItemDAO(Context c) {
        this.mHelper = new FeedReaderDbHelper(c);
    }

    public void insert(Item i) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues valuesItems = new ContentValues();
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, i.getTitle());
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, i.getImage());
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR, i.getBackground_color());

        long itemsRow1 = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);
    }

}
