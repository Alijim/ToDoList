package com.project.todolist.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    }

    public void deleteItemInTagsItem(Integer id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.TagsItemsEntry.TABLE_NAME, selection, selectionArgs);
    }

    public int deleteTagItem(Integer i, Integer t){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS + " LIKE ? AND "+FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS+" LIKE ?";
        String[] selectionArgs = { t.toString(), i.toString() };
        int deletedRows = db.delete(FeedReaderContract.TagsItemsEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

}
