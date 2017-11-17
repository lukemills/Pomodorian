package com.example.android.pomodorian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lukem on 11/8/2017.
 */

public class SessionAdapter extends ArrayAdapter<Session> {
    // Instantiate dbHandler
    PomodoroAppDBHelper dbHandler;

    private Context mContext;

    public SessionAdapter(Context context, ArrayList<Session> sessions){
        super(context, 0, sessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the Product object located at this position in the list
        final Session currentSession = getItem(position);

        TextView sessionDateTextView = (TextView) listItemView.findViewById(R.id.session_date);
        TextView streaksTextView = (TextView) listItemView.findViewById(R.id.streaks_list);
        TextView durationTextView = (TextView) listItemView.findViewById(R.id.duration_list);

        sessionDateTextView.setText(currentSession.getDate());
        streaksTextView.setText(String.valueOf(currentSession.getStreaks()));
        durationTextView.setText(String.valueOf(currentSession.getDuration()));

        return listItemView;
    }
}
