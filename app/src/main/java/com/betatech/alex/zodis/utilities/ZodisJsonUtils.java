package com.betatech.alex.zodis.utilities;

import android.content.ContentProviderOperation;
import android.content.ContentValues;

import com.betatech.alex.zodis.data.ZodisContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Logic to parse Json string according to schema defined for our database.
 */

public class ZodisJsonUtils {

    private static final String ZJ_LEVEL = "level";
    private static final String ZJ_LESSONS = "lessons";
    private static final String ZJ_LESSON_ID = "_id";
    private static final String ZJ_ROOT_WORDS = "root_words";
    private static final String ZJ_DERIVED_WORDS = "derived_words";
    private static final String ZJ_NAME = "name";
    private static final String ZJ_DESCRIPTION = "description";


    public static ArrayList<ContentProviderOperation> getZodisContentProviderOperationsFromJson(String zodisJsonStr)
            throws JSONException {

        ArrayList<ContentProviderOperation> contentProviderOperations=new ArrayList<>();

        JSONArray zodisJsonArray = new JSONArray(zodisJsonStr);

        for (int i = 0; i < zodisJsonArray.length(); i++) {
            JSONObject levelJsonObject = zodisJsonArray.getJSONObject(i);

            String levelName = levelJsonObject.getString(ZJ_LEVEL);
            JSONArray lessonsJsonArray = levelJsonObject.getJSONArray(ZJ_LESSONS);

            for (int i1 = 0; i1 < lessonsJsonArray.length(); i1++) {
                JSONObject lessonJsonObject = lessonsJsonArray.getJSONObject(i1);
                int lessonId = lessonJsonObject.getInt(ZJ_LESSON_ID);
                JSONArray rootWordsJsonArray = lessonJsonObject.getJSONArray(ZJ_ROOT_WORDS);

                for (int i2 = 0; i2 < rootWordsJsonArray.length(); i2++) {

                    JSONObject rootWordJsonObject = rootWordsJsonArray.getJSONObject(i2);
                    JSONArray derivedWordsJsonArray = rootWordJsonObject.getJSONArray(ZJ_DERIVED_WORDS);

                    String rootName = rootWordJsonObject.getString(ZJ_NAME).trim();
                    String rootDescription = rootWordJsonObject.getString(ZJ_DESCRIPTION);

                    ContentValues rootValues = new ContentValues();
                    rootValues.put(ZodisContract.RootEntry.COLUMN_NAME,rootName);
                    rootValues.put(ZodisContract.RootEntry.COLUMN_DESCRIPTION,rootDescription);
                    rootValues.put(ZodisContract.RootEntry.COLUMN_LEVEL,levelName);
                    rootValues.put(ZodisContract.RootEntry.COLUMN_LESSON,lessonId);

                    contentProviderOperations.add(ContentProviderOperation.newInsert(ZodisContract.RootEntry.CONTENT_URI).withValues(rootValues).withYieldAllowed(true).build());
                    contentProviderOperations.addAll(parseDerivedWords(derivedWordsJsonArray, rootName));
                }

                ContentValues levelValues = new ContentValues();
                levelValues.put(ZodisContract.LevelEntry.COLUMN_LESSON_ID,lessonId);
                levelValues.put(ZodisContract.LevelEntry.COLUMN_LEVEL_NAME,levelName);
                contentProviderOperations.add(ContentProviderOperation.newInsert(ZodisContract.LevelEntry.CONTENT_URI).withValues(levelValues).withYieldAllowed(true).build());
            }

        }
        return contentProviderOperations;
    }

    private static ArrayList<ContentProviderOperation> parseDerivedWords(JSONArray derivedWordsJsonArray, String rootName) throws JSONException {

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (int i3 = 0; i3 < derivedWordsJsonArray.length(); i3++) {

            JSONObject derivedWordJsonObject = derivedWordsJsonArray.getJSONObject(i3);
            String derivedName = derivedWordJsonObject.getString(ZJ_NAME).trim();
            String derivedDescription = derivedWordJsonObject.getString(ZJ_DESCRIPTION);

            ContentValues derivedValues =new ContentValues();
            derivedValues.put(ZodisContract.DerivedEntry.COLUMN_NAME,derivedName);
            derivedValues.put(ZodisContract.DerivedEntry.COLUMN_DESCRIPTION,derivedDescription);
            derivedValues.put(ZodisContract.DerivedEntry.COLUMN_ROOT_NAME,rootName);

            operations.add(ContentProviderOperation.newInsert(ZodisContract.DerivedEntry.CONTENT_URI).withValues(derivedValues).withYieldAllowed(true).build());
        }

        return operations;
    }
}
