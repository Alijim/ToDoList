package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    // Create Table
    private static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + FeedReaderContract.TaskEntry.TABLE_NAME + " (" +
                    FeedReaderContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_FK + " INTEGER," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING + " TEXT," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_DONE + " BOOLEAN)";

    private static final String SQL_CREATE_TAGS =
            "CREATE TABLE " + FeedReaderContract.TagsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.TagsEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " TEXT)";


    private static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + FeedReaderContract.ItemsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.ItemsEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE + " DATE," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE + " TEXT)";

    private static final String SQL_CREATE_TAGITEMS =
            "CREATE TABLE " + FeedReaderContract.TagsItemsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.TagsItemsEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_ITEMS + " INTEGER," +
                    FeedReaderContract.TagsItemsEntry.COLUMN_NAME_FK_TAGS + " INTEGER)";



    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.TaskEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASKS);
        db.execSQL(SQL_CREATE_TAGS);
        db.execSQL(SQL_CREATE_ITEMS);
        db.execSQL(SQL_CREATE_TAGITEMS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public int getAnyID(String tableName, String columnName, String args) {
        String s = "";
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();



        String[] projection = {
                BaseColumns._ID,
                columnName,
        };

        String selection = columnName + " = ?";
        String[] selectionArgs = { args };

        String sortOrder =
                columnName + " DESC";

        Cursor cursor = db.query(
                tableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        HashMap<Long, String> items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("_id"));
        }

        return id;
    }




    public void insertFakeData() {
        SQLiteDatabase db = getWritableDatabase();

// Create a new map of values, where column names are the keys
        //ContentValues valuesTagsItem = new ContentValues();
        ContentValues valuesItems = new ContentValues();
        ContentValues valuesTags = new ContentValues();
        ContentValues valuesTasks = new ContentValues();


        valuesTags.put( FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING, "DEV");
        long tagsRow1 = db.insert(FeedReaderContract.TagsEntry.TABLE_NAME, null, valuesTags);

        valuesTags.put( FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING, "HEG");
        long tagsRow2 = db.insert(FeedReaderContract.TagsEntry.TABLE_NAME, null, valuesTags);

        valuesTags.put( FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING, "ECOLE");
        long tagsRow3 = db.insert(FeedReaderContract.TagsEntry.TABLE_NAME, null, valuesTags);

        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, "Devenir développeur");
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, "");
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, "sql.jpg");
        long itemsRow1 = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);

        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, "Apprendre SQL");
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, Boolean.TRUE);
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, getAnyID("Items", "title", "Devenir développeur"));
        long tasksRow1 = db.insert(FeedReaderContract.TaskEntry.TABLE_NAME, null, valuesTasks);

        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, "Apprendre JAVA");
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, Boolean.FALSE);
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, getAnyID("Items", "title", "Devenir développeur"));
        long tasksRow3 = db.insert(FeedReaderContract.TaskEntry.TABLE_NAME, null, valuesTasks);

        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, "Faire le grand écart");
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, "");
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, "sport.jpg");
        long itemsRow2 = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);

        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, "S'inscrire à un fitness");
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, Boolean.TRUE);
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, getAnyID("Items", "Title", "Faire le grand écart"));
        long tasksRow4 = db.insert(FeedReaderContract.TaskEntry.TABLE_NAME, null, valuesTasks);

        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, "Améliorer sa souplesse");
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, Boolean.FALSE);
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, getAnyID("Items", "Title", "Faire le grand écart"));
        long tasksRow2 = db.insert(FeedReaderContract.TaskEntry.TABLE_NAME, null, valuesTasks);


//        long newRowId = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);
//

//

//        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, "Test");
//
//

//

//        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, "Test");
//
//        valuesTags.put( FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, "SPORT");
//
//        valuesTags.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, "Test");






// Insert the new row, returning the primary key value of the new row
        //long newRowId = db.insert(FeedReaderContract.TagsEntry.TABLE_NAME, null, values);
    }

    public void insertIntoItems(String title, String deadLine, String img) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues valuesItems = new ContentValues();

        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, title);
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, deadLine);
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, img);

        long itemsRow1 = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);
    }

    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FeedReaderContract.TagsEntry.TABLE_NAME, null, null);
        db.delete(FeedReaderContract.ItemsEntry.TABLE_NAME, null, null);
        db.delete(FeedReaderContract.TagsItemsEntry.TABLE_NAME, null, null);
        db.delete(FeedReaderContract.TaskEntry.TABLE_NAME, null, null);
    }

    public String readFakeData() {
        String s = "";
        SQLiteDatabase db = getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING,
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " = ?";
        String[] selectionArgs = { "SPORT" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.TagsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        HashMap<Long, String> items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.TagsEntry._ID));
            String value = cursor.getString(cursor.getColumnIndex("wording"));
            items.put(itemId, value);
        }
        return items.toString();
    }

    public String readAllFromTable(String tableName) {
        String s = "";
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();



        String[] projection = {
                BaseColumns._ID,
        };

        //String selection = columnName + " = ?";
        //String[] selectionArgs = { args };

        String sortOrder =  " DESC";

        Cursor cursor = db.query(
                tableName,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        return cursor.toString();
//        List itemIds = new ArrayList<>();
//
//        HashMap<Long, String> items = new HashMap<Long, String>();
//        while(cursor.moveToNext()) {
//            String value = cursor.getString(cursor.getColumnIndex(columnName));
//            items.put(itemId, value);
//        }
//        return items.toString();
    }

    public HashMap<Long, List> readAllItems() {
        String s = "";
        SQLiteDatabase db = getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE
        };

//        String selection = FeedReaderContract.ItemsEntry.COLUMN_NAME_WORDING + " = ?";
//        String[] selectionArgs = { "SPORT" };

        String sortOrder =
                FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.ItemsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        HashMap<Long, List>items = new HashMap<Long, List>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            List values = new ArrayList<>();
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.ItemsEntry._ID));
            values.add(cursor.getString(cursor.getColumnIndex("title")));
            values.add(cursor.getString(cursor.getColumnIndex("deadLine")));
            values.add(cursor.getString(cursor.getColumnIndex("image")));
            //items.put(itemId, values);
//            String value = cursor.getString(cursor.getColumnIndex("fk_Items"));
            items.put(itemId, values);
        }

        return items;
    }

    public List<String> getTaskFromItem(String args) {
        String s = "";
        Integer id = getAnyID("Items", "Title", args);
        List values = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING,
        };

        String selection = FeedReaderContract.TaskEntry.COLUMN_NAME_FK + " = ?";
        String[] selectionArgs = {  id.toString()};

        String sortOrder =
                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING + " DESC";

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
            //long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.TaskEntry._ID));
            values.add(cursor.getString(cursor.getColumnIndex("wording")));
            //items.put(itemId, cursor.getString(cursor.getColumnIndex("wording")));
        }

        return values;

        //return items.toString();
    }

    public HashMap<Long, List> readAllTasks() {
        String s = "";
        SQLiteDatabase db = getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TaskEntry.COLUMN_NAME_FK,
                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING,
                FeedReaderContract.TaskEntry.COLUMN_NAME_DONE
        };

        String selection = FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " = ?";
        String[] selectionArgs = { "SPORT" };

        String sortOrder =
                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.TaskEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        HashMap<Long, List>items = new HashMap<Long, List>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            List values = new ArrayList<>();
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.TaskEntry._ID));
            values.add(cursor.getInt(cursor.getColumnIndex("fk_Items")));
            values.add(cursor.getString(cursor.getColumnIndex("wording")));
            values.add(cursor.getInt(cursor.getColumnIndex("done")));
            //items.put(itemId, values);
//            String value = cursor.getString(cursor.getColumnIndex("fk_Items"));
            items.put(itemId, values);
        }

        return items;

        //return items.toString();
    }

    public String readAny(String tableName, String columnName, String args)  {
        String s = "";
        int id = 0;
        SQLiteDatabase db = getReadableDatabase();



        String[] projection = {
                BaseColumns._ID,
                columnName,
        };

        String selection = columnName + " = ?";
        String[] selectionArgs = { args };

        String sortOrder =
                columnName + " DESC";

        Cursor cursor = db.query(
                tableName,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        HashMap<Long, String> items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.TagsEntry._ID));
            String value = cursor.getString(cursor.getColumnIndex("wording"));
            items.put(itemId, value);
        }
        return items.toString();
    }

}
