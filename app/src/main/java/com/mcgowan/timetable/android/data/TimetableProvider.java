package com.mcgowan.timetable.android.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.mcgowan.timetable.android.data.TimetableContract.AvailableLabEntry;
import com.mcgowan.timetable.android.data.TimetableContract.TimetableEntry;

import static android.text.TextUtils.isEmpty;


public class TimetableProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = TimetableProvider.class.getSimpleName();

    public static final int TIMETABLE = 100;
    public static final int TIMETABLE_BY_ID = 101;
    public static final int LABS = 300;

    private static final SQLiteQueryBuilder sTimetableQueryBuilder, sAvailableLabsQueryBuilder;

    private TimetableDbHelper mHelper;

    static {
        sAvailableLabsQueryBuilder = new SQLiteQueryBuilder();
        sAvailableLabsQueryBuilder.setTables(
                TimetableContract.AvailableLabEntry.TABLE_NAME
        );
    }

    static {
        sTimetableQueryBuilder = new SQLiteQueryBuilder();
        //table joins can be created here
        sTimetableQueryBuilder.setTables(
                TimetableContract.TimetableEntry.TABLE_NAME
        );
//        WeatherContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
//                WeatherContract.LocationEntry.TABLE_NAME +
//                " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
//                "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
//                " = " + WeatherContract.LocationEntry.TABLE_NAME +
//                "." + WeatherContract.LocationEntry._ID);
    }


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TimetableContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, TimetableContract.PATH_TIMETABLES + "/*", TIMETABLE_BY_ID);
        matcher.addURI(authority, TimetableContract.PATH_TIMETABLES, TIMETABLE);

        matcher.addURI(authority, TimetableContract.PATH_AVAILABLE_LABS, LABS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new TimetableDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case TIMETABLE_BY_ID:{
                retCursor = getTimeTableById(
                        uri,
                        projection,
                        sortOrder
                );
                break;
            }

            case TIMETABLE: {
                retCursor = getTimeTableByStudentId(
                        uri,
                        projection,
                        sortOrder
                );
                break;
            }
            case LABS: {
                retCursor = getLabsByDay(
                        uri,
                        projection,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    private Cursor getLabsByDay(Uri uri, String[] projection, String sortOrder) {
        return null;
    }

    private static final String sStudentIdSelection =
            TimetableEntry.TABLE_NAME + "." + TimetableEntry.COLUMN_STUDENT_ID + " = ? ";

    private static final String sIdSelection =
            TimetableEntry.TABLE_NAME + "." + TimetableEntry._ID + " =? ";

    private static final String sStudentIdSelectionWithDay =
            TimetableEntry.TABLE_NAME + "." + TimetableEntry.COLUMN_STUDENT_ID + " = ? AND "
                    + TimetableEntry.COLUMN_DAY + " = ?";


    private Cursor getTimeTableByStudentId(Uri uri, String[] projection, String sortOrder) {
        String studentIdSetting = TimetableEntry.getStudentIdFromUri(uri);
        String daySetting = TimetableEntry.getDayFromUri(uri);
        String[] selectionArgs;
        String queryParams;
        boolean selectAll = false;

        //return select all, as no querystring parameters passed
        if (isEmpty(daySetting) && isEmpty(studentIdSetting)) selectAll = true;

        if (isEmpty(daySetting)) {
            queryParams = sStudentIdSelection;
            selectionArgs = new String[]{studentIdSetting};
        } else {
            queryParams = sStudentIdSelectionWithDay;
            selectionArgs = new String[]{studentIdSetting, daySetting};
        }
        return sTimetableQueryBuilder.query(
                mHelper.getReadableDatabase(),
                projection,
                selectAll ? null : queryParams,
                selectAll ? null : selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTimeTableById(Uri uri, String[] projection, String sortOrder) {

        String id = uri.getLastPathSegment();
        String queryParams = sIdSelection;

        Cursor result = sTimetableQueryBuilder.query(
                mHelper.getReadableDatabase(),
                projection,
                queryParams,
                new String[]{id},
                null,
                null,
                sortOrder
        );
        return result;
//        Log.d("FUCK", sTimetableQueryBuilder.toString());
//        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TIMETABLE:
                return TimetableEntry.CONTENT_TYPE;

            case LABS:
                return AvailableLabEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long _id;
        Uri returnUri;

        switch (match) {
            case TIMETABLE:
                _id = db.insert(TimetableEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TimetableEntry.buildTimetableUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            case LABS:
                _id = db.insert(AvailableLabEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = AvailableLabEntry.buildLabsUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        if (null == selection) selection = "1";

        switch (match) {
            case TIMETABLE:
                rowsDeleted = db.delete(TimetableEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case LABS:
                rowsDeleted = db.delete(AvailableLabEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TIMETABLE:
                rowsUpdated = db.update(TimetableEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case LABS:
                rowsUpdated = db.update(AvailableLabEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TIMETABLE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TimetableEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
