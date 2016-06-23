package com.mcgowan.timetable.itsligotimetables;

import android.os.AsyncTask;

import java.util.List;


public class FetchTimetableTask extends AsyncTask<String, Void, List<String>> {


//    private final Context mContext;
//
//    public FetchTimetableTask(Context mContext) {
//
//        this.mContext = mContext;
//    }
//
//    private boolean DEBUG = true;
//
//    private final String LOG_TAG = FetchTimetableTask.class.getSimpleName();
//    private String studentID;
//

    @Override
    protected List<String> doInBackground(String... params) {
//
//
//        String url = MainActivity.TIMETABLE_URL;
//        studentID = params[0];
//
//        try {
//            TimeTable t = new TimeTable(url, studentID);
//            List<String> classes = getClassesAsArray(t);
//
//            return classes;
//
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error connecting to ITS website", e);
//            return null;
//        }
        return null;
    }

//    private List<String> getClassesAsArray(TimeTable t) {
//        List<String> classes = new ArrayList<String>();
//        Map<String, List<Course>> days = t.getDays();
//
//        Vector<ContentValues> cvVector = new Vector<>();
//
//        for (Map.Entry<String, List<Course>> entry : days.entrySet()) {
//            for (Course c : entry.getValue()) {
//
//
//                ContentValues classValues = new ContentValues();
//                classValues.put(TimetableEntry.COLUMN_DAY, c.getDay());
//                classValues.put(TimetableEntry.COLUMN_LECTURER, c.getLecturer());
//                classValues.put(TimetableEntry.COLUMN_START_TIME, c.getStartTime());
//                classValues.put(TimetableEntry.COLUMN_TIME, c.getTime());
//                classValues.put(TimetableEntry.COLUMN_END_TIME, c.getEndTime());
//                classValues.put(TimetableEntry.COLUMN_STUDENT_ID, studentID);
//                classValues.put(TimetableEntry.COLUMN_SUBJECT, c.getSubject());
//                classValues.put(TimetableEntry.COLUMN_ROOM, c.getRoom());
//                classValues.put(TimetableEntry.COLUMN_DAY_ID, Utility.getDayNumberFromDay(c.getDay()));
//                cvVector.add(classValues);
//
//                String line = c.toString();
//                classes.add(line);
//            }
//        }
//        int inserted = 0;
//
//        if (cvVector.size() > 0) {
//            ContentValues[] cvArray = new ContentValues[cvVector.size()];
//            cvVector.toArray(cvArray);
//
//            inserted = mContext.getContentResolver().bulkInsert(
//                    TimetableEntry.CONTENT_URI,
//                    cvArray
//            );
//
//        }
//
//        Uri timeTableUri = TimetableEntry.buildTimetableWithStudentId(studentID);
//
//        Cursor cursor = mContext.getContentResolver().query(timeTableUri, null, null, null, null);
//
//        Log.d(LOG_TAG, "FetchTimetableTask Complete. " + inserted + " records inserted");
//        cvVector = new Vector<ContentValues>(cursor.getCount());
//        if (cursor.moveToFirst()) {
//            do {
//                ContentValues cv = new ContentValues();
//                DatabaseUtils.cursorRowToContentValues(cursor, cv);
//                cvVector.add(cv);
//            } while (cursor.moveToNext());
//        }
//        return classes;
//    }
}
