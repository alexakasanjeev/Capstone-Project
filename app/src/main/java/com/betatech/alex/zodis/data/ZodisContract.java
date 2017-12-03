package com.betatech.alex.zodis.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lenovo on 11/29/2017.
 */

public class ZodisContract {

    public static final String CONTENT_AUTHORITY = "com.betatech.alex.zodis";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class RootEntry implements CommonColumns {

        public static final String TABLE_NAME="root_words";

        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_LESSON = "lesson";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_FAVOURITE = "favourite";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();

        public static Uri buildRootUriWithId(long _id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(_id))
                    .build();
        }

    }

    public static final class DerivedEntry implements CommonColumns{

        public static final String TABLE_NAME = "derived_words";

        public static final String COLUMN_ROOT_NAME = "root_name";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();

        public static Uri buildDerivedUriWithId(Long _id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(_id))
                    .build();
        }

    }

    private interface CommonColumns extends BaseColumns{
        String COLUMN_NAME = "name";
        String COLUMN_DESCRIPTION = "description";
    }

}
