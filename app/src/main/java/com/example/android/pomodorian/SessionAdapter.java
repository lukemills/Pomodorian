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

        // Find the TextView in the list_item.xml layout with the ID product_name_list
        TextView productNameTextView = (TextView) listItemView.findViewById(R.id.session_date);
        // Get the product name from the currentProduct object and set this text on
        // the name TextView.
        // productNameTextView.setText(currentSession.getDate());
        productNameTextView.setText("09/20/1995");
        return listItemView;
    }
}
