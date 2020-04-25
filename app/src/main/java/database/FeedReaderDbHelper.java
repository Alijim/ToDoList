package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.project.todolist.model.Item;
import com.project.todolist.model.Tag;
import com.project.todolist.model.Task;

import java.text.SimpleDateFormat;
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
                    FeedReaderContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_FK + " INTEGER," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING + " TEXT," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_DONE + " BOOLEAN)";

    private static final String SQL_CREATE_TAGS =
            "CREATE TABLE " + FeedReaderContract.TagsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.TagsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " TEXT)";

  /*Le titre de l'item est unique, pour nous, il n'est pas cohérent d'avoir 2 items avec le même nom.
   * 2 items qui ont le même nom, sont finalement les mêmes, donc il suffit d'ajouter les tâches
   * dans un seul Item afin d'avoir d'avoir des tâches dispersées.
   */

    private static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + FeedReaderContract.ItemsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.ItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE + " TEXT UNIQUE," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE + " BIGINT," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE + " TEXT, "+
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR + " TEXT)";

    private static final String SQL_CREATE_TAGITEMS =
            "CREATE TABLE " + FeedReaderContract.TagsItemsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.TagsItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
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
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}



