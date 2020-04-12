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


    public void insertFakeData() {
        SQLiteDatabase db = getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put( FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING, "Test");

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.TagsEntry.TABLE_NAME, null, values);
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
        String[] selectionArgs = { "Test" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.TagsEntry.TABLE_NAME,   // The table to query
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