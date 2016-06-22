package com.mcgowan.timetable.itsligotimetables;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TimetableAdapter extends CursorAdapter {
    private static final String LOG_TAG = TimeTableFragment.class.getSimpleName();
    public TimetableAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private final int VIEW_TYPE_NEXT = 0;
    private final int VIEW_TYPE_ALL = 1;


    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        String result = String.format("%s : %s : %s : %s : %s : %s : %s",
                cursor.getString(TimeTableFragment.COL_TIMETABLE_DAY),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_TIME),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_SUBJECT),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_LECTURER),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_ROOM),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_ID),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_DAY_ID)
        );

        return result;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_NEXT : VIEW_TYPE_ALL;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /*
            Remember that these views are reused as needed.
         */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if(viewType == VIEW_TYPE_NEXT){
            layoutId = R.layout.list_item_next_class;
        }
        else if (viewType == VIEW_TYPE_ALL){
            layoutId = R.layout.list_item_class;
        }

        layoutId = R.layout.list_item_class;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.iconView.setImageResource(R.drawable.friday_icon);

        String day = cursor.getString(TimeTableFragment.COL_TIMETABLE_DAY) + "_icon";
//        ImageView imageView = (ImageView) view.findViewById(R.id.list_item_icon);

        String subject = cursor.getString(TimeTableFragment.COL_TIMETABLE_SUBJECT);
        String room = cursor.getString(TimeTableFragment.COL_TIMETABLE_ROOM);
        String time = cursor.getString(TimeTableFragment.COL_TIMETABLE_TIME);
        String lecturer = cursor.getString(TimeTableFragment.COL_TIMETABLE_LECTURER);

        String timeAndSubject = Utility.createTimeAndSubject(time, subject);
        viewHolder.timeAndSubject.setText(timeAndSubject);

        String roomAndLecturer = Utility.createRoomAndLecturerString(room, lecturer);
        viewHolder.roomAndLecturer.setText(roomAndLecturer);
    }

    public static class ViewHolder{
        public final ImageView iconView;
        public final TextView timeAndSubject;
        public final TextView roomAndLecturer;

        public ViewHolder(View view) {
            this.iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            this.timeAndSubject = (TextView) view.findViewById( R.id.list_item_time_and_subject);
            this.roomAndLecturer = (TextView) view.findViewById(R.id.list_item_room_and_lecturer);
        }
    }


}
