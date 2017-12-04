package com.betatech.alex.zodis.utilities;

import android.database.Cursor;

import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.pojo.DerivedWord;
import com.betatech.alex.zodis.data.pojo.RootWord;

import java.util.ArrayList;

/**
 * Created by lenovo on 12/4/2017.
 */

public class GeneralUtils {

    public static RootWord extractWords(Cursor rootWordCursor,Cursor derivedWordCursor, int position){

        rootWordCursor.moveToPosition(position);

        int idColumnIndex = rootWordCursor.getColumnIndex(ZodisContract.RootEntry._ID);
        int nameColumnIndex = rootWordCursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_NAME);
        int descriptionColumnIndex = rootWordCursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_DESCRIPTION);

        String rootWordName = rootWordCursor.getString(nameColumnIndex);

        ArrayList<DerivedWord> derivedWords = extractDerivedWords(derivedWordCursor,rootWordName);


        return new RootWord(rootWordCursor.getInt(idColumnIndex), rootWordName,rootWordCursor.getString(descriptionColumnIndex),derivedWords);
    }

    private static ArrayList<DerivedWord> extractDerivedWords(Cursor derivedWordCursor, String rootWordName) {

        ArrayList<DerivedWord> list = new ArrayList<>();

        for (int i = 0; i < derivedWordCursor.getCount(); i++) {
            derivedWordCursor.moveToPosition(i);

            if (!derivedWordCursor.getString(derivedWordCursor.getColumnIndex(ZodisContract.DerivedEntry.COLUMN_ROOT_NAME)).equals(rootWordName)) {
                continue;
            }
            int derivedWordColumnIndex = derivedWordCursor.getColumnIndex(ZodisContract.DerivedEntry.COLUMN_NAME);
            int derivedWordDescriptionColumnIndex = derivedWordCursor.getColumnIndex(ZodisContract.DerivedEntry.COLUMN_DESCRIPTION);

            DerivedWord derivedWord = new DerivedWord(derivedWordCursor.getString(derivedWordColumnIndex),derivedWordCursor.getString(derivedWordDescriptionColumnIndex));
            list.add(derivedWord);

        }
        return list;
    }
}
