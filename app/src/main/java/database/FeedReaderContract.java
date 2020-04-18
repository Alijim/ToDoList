package database;

import android.provider.BaseColumns;

public final class FeedReaderContract {
     // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private FeedReaderContract() {}

        /* Inner class that defines the table contents */
        public static class TaskEntry implements BaseColumns {
            public static final String TABLE_NAME = "Tasks";
            public static final String COLUMN_NAME_WORDING = "wording";
            public static final String COLUMN_NAME_FK = "fk_Items";
            public static final String COLUMN_NAME_DONE = "done";
        }

        public static class ItemsEntry implements BaseColumns {
            public static final String TABLE_NAME = "Items";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_DEADLINE = "deadLine";
            public static final String COLUMN_NAME_IMAGE  = "image";
            public static final String COLUMN_NAME_BGCOLOR  = "background_color";
        }

        public static class TagsEntry implements BaseColumns {
            public static final String TABLE_NAME = "Tags";
            public static final String COLUMN_NAME_WORDING = "wording";
        }

        public static class TagsItemsEntry implements BaseColumns {
            public static final String TABLE_NAME = "TagsItem";
            public static final String COLUMN_NAME_FK_ITEMS = "fk_items";
            public static final String COLUMN_NAME_FK_TAGS = "fk_tags";
        }





}
