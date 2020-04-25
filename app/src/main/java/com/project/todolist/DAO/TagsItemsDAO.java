package com.project.todolist.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;

import java.util.ArrayList;
import java.util.List;

import database.FeedReaderContract;
import database.FeedReaderDbHelper;

public class TagsItemsDAO {

    private FeedReaderDbHelper mHelper;
    private Context context;
    private FeedReaderContract mContract;

    public TagsItemsDAO(Context c) {
        this.mHelper = new FeedReaderDbHelper(c);
        this.context = c;

    }

    public void deleteTagsInTagsItem(Integer id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.TagsItemsEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void deleteItemInTagsItem(Integer id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.TagsItemsEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public int deleteTagItem(Integer i, Integer t){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS + " LIKE ? AND "+FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS+" LIKE ?";
        String[] selectionArgs = { t.toString(), i.toString() };
        int deletedRows = db.delete(FeedReaderContract.TagsItemsEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows;

    }

    public void insertTagItems(Item i, Tag t) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues valuesItems = new ContentValues();

        valuesItems.put(FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS, i.getId());
        valuesItems.put(FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS, t.getId());

        long itemsRow1 = db.insert(FeedReaderContract.TagsItemsEntry.TABLE_NAME, null, valuesItems);
        db.close();
    }

    public Boolean isTagItem(Integer i, Integer t){
        Boolean s = Boolean.FALSE;
        List values = new ArrayList<>();
        List<Tag> tagList = new ArrayList<Tag>();
        SQLiteDatabase db = mHelper.getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS,
                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS
        };

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " = ? AND "+FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS+" = ?";
        String[] selectionArgs = {  i.toString(), t.toString()};

        String sortOrder =
                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " ASC ";

        Cursor cursor = db.query(
                FeedReaderContract.TagsItemsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );


        while(cursor.moveToNext()) {
            s = Boolean.TRUE;
        }
        db.close();
        return s;
    }


}
