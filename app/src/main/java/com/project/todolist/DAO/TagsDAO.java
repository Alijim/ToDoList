package com.project.todolist.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.project.todolist.model.Tag;

import java.util.ArrayList;
import java.util.List;

import database.FeedReaderContract;
import database.FeedReaderDbHelper;

public class TagsDAO {

    private FeedReaderDbHelper mHelper;
    private Context context;
    private FeedReaderContract mContract;

    public TagsDAO(Context c) {
        this.mHelper = new FeedReaderDbHelper(c);
        this.context = c;

    }

    public Integer insertTag( Tag t) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues valuesTags = new ContentValues();

        valuesTags.put( FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING, t.getWording());
        long tasksRow1 = db.insert(FeedReaderContract.TagsEntry.TABLE_NAME, null, valuesTags);
        Integer i = (int) tasksRow1;
        return  i;
    }

    public List<Tag> getAllTags() {
        String s = "";
        List<Tag> tagList = new ArrayList<Tag>();
        SQLiteDatabase db = mHelper.getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING
        };

//        String selection = FeedReaderContract.ItemsEntry.COLUMN_NAME_WORDING + " = ?";
//        String[] selectionArgs = { "SPORT" };

        String sortOrder =
                FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " DESC ";

        Cursor cursor = db.query(
                FeedReaderContract.TagsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

//        HashMap<Integer, List>items = new HashMap<Integer, List>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.TagsEntry._ID));
            String wording = cursor.getString(cursor.getColumnIndex("wording"));
            Tag t = new Tag(id, wording);
            //items.put(itemId, values);
//            String value = cursor.getString(cursor.getColumnIndex("fk_Items"));
            tagList.add(t);
        }

        return tagList;
    }

    public Tag researchTag(Integer id){
        String s = "";
        List values = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Tag i_n = new Tag();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING
        };

        String selection = FeedReaderContract.TagsEntry._ID + " = ?";
        String[] selectionArgs = {  id.toString() };

        String sortOrder =
                FeedReaderContract.TagsEntry._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.TagsEntry.TABLE_NAME,   // The table to query
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
            Integer i = cursor.getInt(cursor.getColumnIndex("_id"));
            String wording = cursor.getString(cursor.getColumnIndex("wording"));
            Tag t = new Tag(i, wording);
            return t;

        }

        return i_n;

    }

    public List<Tag> getTagFromItem(Integer id) {
        String s = "";
        List values = new ArrayList<>();
        List<Tag> tagList = new ArrayList<Tag>();
        SQLiteDatabase db = mHelper.getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS,
                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS
        };

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " = ?";
        String[] selectionArgs = {  id.toString()};

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

//        HashMap<Long, String>items = new HashMap<Long, String>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            Integer idTag = cursor.getInt(cursor.getColumnIndexOrThrow("fk_tags"));
            Tag t = researchTag(idTag);
//            In wording = cursor.getString(cursor.getColumnIndex("wording"));
//            Integer done = cursor.getInt(cursor.getColumnIndex("done"));
//            if(done == 1) {
//                 b = Boolean.FALSE;
//            } else {
//                 b = Boolean.FALSE;
//            }
//            Tag t = new Tag(idTag, wording);
            tagList.add(t);
        }

        return tagList;

        //return items.toString();
    }

    public String getTagFromItemListDisplay(Integer id) {
        String s = "\n";
//        Integer id = getAnyID("Items", "Title", args);
        List values = new ArrayList<>();
        List<String> tagList = new ArrayList<String>();
        SQLiteDatabase db = mHelper.getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS,
                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS
        };

        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " = ?";
        String[] selectionArgs = {  id.toString()};

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

//        HashMap<Long, String>items = new HashMap<Long, String>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            Integer idTag = cursor.getInt(cursor.getColumnIndexOrThrow("fk_tags"));
            Tag t = researchTag(idTag);
//            In wording = cursor.getString(cursor.getColumnIndex("wording"));
//            Integer done = cursor.getInt(cursor.getColumnIndex("done"));
//            if(done == 1) {
//                 b = Boolean.FALSE;
//            } else {
//                 b = Boolean.FALSE;
//            }
//            Tag t = new Tag(idTag, wording);
            if(t.getWording() != null) {
                s += "["+t.getWording()+"] ";

            }
        }

        return s;

        //return items.toString();
    }
//    public Boolean isTagItem(Integer i, Integer t){
//        Boolean s = Boolean.FALSE;
////        Integer id = getAnyID("Items", "Title", args);
//        List values = new ArrayList<>();
//        List<Tag> tagList = new ArrayList<Tag>();
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//
//
//        String[] projection = {
//                BaseColumns._ID,
//                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS,
//                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS
//        };
//
//        String selection = FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " = ? AND "+FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS+" = ?";
//        String[] selectionArgs = {  i.toString(), t.toString()};
//
//        String sortOrder =
//                FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " ASC ";
//
//        Cursor cursor = db.query(
//                FeedReaderContract.TagsItemsEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                selection,              // The columns for the WHERE clause
//                selectionArgs,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                sortOrder               // The sort order
//        );
//
////        HashMap<Long, String>items = new HashMap<Long, String>();
////        HashMap<Long, String>items = new HashMap<Long, String>();
//        while(cursor.moveToNext()) {
//            s = Boolean.TRUE;
////            Integer idTag = cursor.getInt(cursor.getColumnIndexOrThrow("fk_tags"));
////            Tag t = researchTag(idTag);
////            In wording = cursor.getString(cursor.getColumnIndex("wording"));
////            Integer done = cursor.getInt(cursor.getColumnIndex("done"));
////            if(done == 1) {
////                 b = Boolean.FALSE;
////            } else {
////                 b = Boolean.FALSE;
////            }
////            Tag t = new Tag(idTag, wording);
//        }
//
//        return s;
//    }
    //DAO ICI
    public void deleteTag(Integer id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        TagsItemsDAO tagsItemsDAO = new TagsItemsDAO(context);

        String selection = FeedReaderContract.TagsEntry._ID + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.TagsEntry.TABLE_NAME, selection, selectionArgs);

        tagsItemsDAO.deleteTagsInTagsItem(id);
    }












}
