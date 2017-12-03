package com.betatech.alex.zodis.data;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by lenovo on 11/29/2017.
 */

public class ZodisProvider extends ContentProvider {

    public static final int CODE_ROOT = 100;
    public static final int CODE_DERIVED = 200;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ZodisDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ZodisContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,ZodisContract.RootEntry.TABLE_NAME, CODE_ROOT);
        matcher.addURI(authority,ZodisContract.DerivedEntry.TABLE_NAME, CODE_DERIVED);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new ZodisDbHelper(getContext());
        return mOpenHelper != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_ROOT:
                cursor = db.query(ZodisContract.RootEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CODE_DERIVED:
                cursor = db.query(ZodisContract.DerivedEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();

            getContext().getContentResolver().notifyChange(ZodisContract.DerivedEntry.CONTENT_URI,null);

            return results;
        } finally {
            db.endTransaction();
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id = -1;
        switch (sUriMatcher.match(uri)) {
            case CODE_ROOT:
                _id = db.insert(ZodisContract.RootEntry.TABLE_NAME,null,values);
                return ZodisContract.RootEntry.buildRootUriWithId(_id);
            case CODE_DERIVED:
                _id = db.insert(ZodisContract.DerivedEntry.TABLE_NAME,null,values);
                return ZodisContract.DerivedEntry.buildDerivedUriWithId(_id);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("We are not implementing delete in Zodis");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in Zodis");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Zodis");
    }
}
