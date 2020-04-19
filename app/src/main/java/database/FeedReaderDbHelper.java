package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.todolist.R;
import com.example.todolist.model.Item;
import com.example.todolist.model.Task;

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
                    FeedReaderContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_FK + " INTEGER," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING + " TEXT," +
                    FeedReaderContract.TaskEntry.COLUMN_NAME_DONE + " BOOLEAN)";

    private static final String SQL_CREATE_TAGS =
            "CREATE TABLE " + FeedReaderContract.TagsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.TagsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " TEXT)";


    private static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + FeedReaderContract.ItemsEntry.TABLE_NAME + " (" +
                    FeedReaderContract.ItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE + " DATE," +
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

    public void insertIntoItems(Item i) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues valuesItems = new ContentValues();

        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, i.getTitle());
//        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, i.getDeadline().);
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, i.getImage());
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR, i.getBackground_color());

        long itemsRow1 = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);
    }

    public void insertTask(Integer id, Task t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valuesTasks = new ContentValues();

        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING, t.getWording());
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, t.getDone());
        valuesTasks.put( FeedReaderContract.TaskEntry.COLUMN_NAME_FK, id);
        long tasksRow1 = db.insert(FeedReaderContract.TaskEntry.TABLE_NAME, null, valuesTasks);
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

    public List<Item> getAllItems() {
        String s = "";
        List<Item> itemList = new ArrayList<Item>();
        SQLiteDatabase db = getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR
        };

//        String selection = FeedReaderContract.ItemsEntry.COLUMN_NAME_WORDING + " = ?";
//        String[] selectionArgs = { "SPORT" };

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

        HashMap<Integer, List>items = new HashMap<Integer, List>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.ItemsEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            List<Task> tasks = getTasksFromItem(id);
            String deadLine = cursor.getString(cursor.getColumnIndex("deadLine"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String color = cursor.getString(cursor.getColumnIndex("background_color"));
            Item i = new Item(id, title, tasks, image, color);
            //items.put(itemId, values);
//            String value = cursor.getString(cursor.getColumnIndex("fk_Items"));
            itemList.add(i);
        }

        return itemList;
    }

    public Item researchItem(String title){
        String s = "";
        List values = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
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

//        HashMap<Long, String>items = new HashMap<Long, String>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
             Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
            String title_1 = cursor.getString(cursor.getColumnIndex("title"));
            List<Task> tasks = getTasksFromItem(id);
            String deadLine = cursor.getString(cursor.getColumnIndex("deadLine"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String color = cursor.getString(cursor.getColumnIndex("background_color"));
             Item i = new Item(id, title, tasks, image, color);
             return i;

        }

        return i_n;

    }

    public List<Task> getTasksFromItem(Integer id) {
        String s = "";
//        Integer id = getAnyID("Items", "Title", args);
        List values = new ArrayList<>();
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = getReadableDatabase();


        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING,
        };

        String selection = FeedReaderContract.TaskEntry.COLUMN_NAME_FK + " = ?";
        String[] selectionArgs = {  id.toString()};

        String sortOrder =
                FeedReaderContract.TaskEntry._ID + " DESC";

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
//        Integer id = getAnyID("Items", "Title", args);
        List values = new ArrayList<Integer>();
        SQLiteDatabase db = getReadableDatabase();


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

//        HashMap<Long, String>items = new HashMap<Long, String>();
//        HashMap<Long, String>items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            //long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.TaskEntry._ID));
            values.add(cursor.getInt(cursor.getColumnIndex("_id")));
            //items.put(itemId, cursor.getString(cursor.getColumnIndex("wording")));
        }

        return values;

        //return items.toString();
    }

//    public List<Task> readAllTasks() {
//        String s = "";
//        SQLiteDatabase db = getReadableDatabase();
//
//
//        String[] projection = {
//                BaseColumns._ID,
//                FeedReaderContract.TaskEntry.COLUMN_NAME_FK,
//                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING,
//                FeedReaderContract.TaskEntry.COLUMN_NAME_DONE
//        };
//
//        String selection = FeedReaderContract.TagsEntry.COLUMN_NAME_WORDING + " = ?";
//        String[] selectionArgs = { "SPORT" };
//
//        String sortOrder =
//                FeedReaderContract.TaskEntry.COLUMN_NAME_WORDING + " DESC";
//
//        Cursor cursor = db.query(
//                FeedReaderContract.TaskEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                null,              // The columns for the WHERE clause
//                null,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                sortOrder               // The sort order
//        );
//
//        HashMap<Long, List>items = new HashMap<Long, List>();
////        HashMap<Long, String>items = new HashMap<Long, String>();
//        while(cursor.moveToNext()) {
//            List values = new ArrayList<>();
//            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.TaskEntry._ID));
//            values.add(cursor.getInt(cursor.getColumnIndex("fk_Items")));
//            values.add(cursor.getString(cursor.getColumnIndex("wording")));
//            values.add(cursor.getInt(cursor.getColumnIndex("done")));
//            //items.put(itemId, values);
////            String value = cursor.getString(cursor.getColumnIndex("fk_Items"));
//            items.put(itemId, values);
//        }
//
//        return items;
//
//        //return items.toString();
//    }

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

    public Integer readDone(Integer id) {
        String s = "";
        Integer bDone = 0;
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.TaskEntry.COLUMN_NAME_DONE,
        };

        String selection = FeedReaderContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = { id.toString() };

        String sortOrder =
                FeedReaderContract.TaskEntry._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.TaskEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        HashMap<Long, String> items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            bDone = cursor.getInt(cursor.getColumnIndex("done"));
        }
        return bDone;
    }

    public void changeDone(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        Integer done;
        Integer test = 1;

        if(readDone(id) == 0) {
            done = 1;
        } else {
            done = 0;
        }

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.TaskEntry.COLUMN_NAME_DONE, done);

        String selection = FeedReaderContract.TaskEntry._ID + " =  "+test.toString();
        String[] selectionArgs = {test.toString() };

        int count = db.update(
                FeedReaderContract.TaskEntry.TABLE_NAME,
                values,
                selection,
                null);

    }

    public void updateItem(Item i) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, i.getTitle());
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, i.getDeadline().toString());
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, i.getImage());
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR, i.getBackground_color());

        String selection = FeedReaderContract.ItemsEntry._ID + " =  "+i.getId().toString();

        int count = db.update(
                FeedReaderContract.ItemsEntry.TABLE_NAME,
                values,
                selection,
                null);
    }

    public String testDone (Integer id) {
        String txt = "AVANT :";
        txt += " "+readDone(id).toString();
        changeDone(id);
        txt += " | APRES : "+readDone(id).toString();

        return txt;

    }

    public void deleteTask(Integer id){
        SQLiteDatabase db = getWritableDatabase();

        String selection = FeedReaderContract.TaskEntry._ID + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteItemById(Integer id) {

        SQLiteDatabase db = getWritableDatabase();

        List<Integer> l = new ArrayList<Integer>(getTasksIdFromItem(id));

//        deleteTask(25);

        for(Integer i : l) {
            deleteTask(i);
        }

        String selection = FeedReaderContract.ItemsEntry._ID + " LIKE ?";
        String[] selectionArgs = { id.toString() };
        int deletedRows = db.delete(FeedReaderContract.ItemsEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void testDelete(Integer id) {
        deleteItemById(4);
    }

    public void testDate(){
        SQLiteDatabase db = getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues valuesItems = new ContentValues();
//        String date =
//        valuesItems.put(, dateFormat.format(lsd));


        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_TITLE, "TEST");
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_DEADLINE, "skuksuksusksu");
        valuesItems.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_IMAGE, "test.jpg");

        long itemsRow1 = db.insert(FeedReaderContract.ItemsEntry.TABLE_NAME, null, valuesItems);
    }

    public void updateBackgroundColorFromItem(Integer id, Integer color) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR, color.toString());

        String selection = FeedReaderContract.ItemsEntry._ID + " =  "+id.toString();

        int count = db.update(
                FeedReaderContract.ItemsEntry.TABLE_NAME,
                values,
                selection,
                null);
    }

    public Integer getBackgroundColorFromItem(Integer id) {
        SQLiteDatabase db = getReadableDatabase();
        Integer color = 0;

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.ItemsEntry.COLUMN_NAME_BGCOLOR,
        };

        String selection = FeedReaderContract.ItemsEntry._ID + " = ?";
        String[] selectionArgs = { id.toString() };

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

        HashMap<Long, String> items = new HashMap<Long, String>();
        while(cursor.moveToNext()) {
            color = cursor.getInt(cursor.getColumnIndex("background_color"));
        }
        return color;
    }

}



