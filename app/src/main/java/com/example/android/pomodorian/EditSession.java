package com.example.android.pomodorian;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditSession extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);

        Button save = (Button) findViewById(R.id.save_edit_session);
        final TextView workMinuteTextView = (TextView) findViewById(R.id.edit_minutes_work);
        final TextView breakMinuteTextView = (TextView) findViewById(R.id.edit_minutes_break);
        final TextView workSecondsTextView = (TextView) findViewById(R.id.edit_seconds_work);
        final TextView breakSecondsTextView = (TextView) findViewById(R.id.edit_seconds_break);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long workSeconds = 0;
                long breakSeconds = 0;
                long newWorkTime = 0;
                long newBreakTime = 0;
                boolean edited = false;
                boolean workEdited = false;
                boolean breakEdited = false;

                /*
                Toast.makeText(EditSession.this,
                        "Work time set to " + workMinuteTextView.getText()
                                + ":" + workSecondsTextView.getText()
                                + "; break time set to " + breakMinuteTextView.getText()
                                + ":" + breakSecondsTextView.getText()
                        ,
                        Toast.LENGTH_SHORT).show();
                */
                Intent returnIntent = new Intent();
                if (!(workMinuteTextView.getText().toString().isEmpty())) {
                    workEdited = true;
                    edited = true;
                    workSeconds = Integer.parseInt(workMinuteTextView.getText().toString()) * 60;
                    Toast.makeText(EditSession.this, "Work minutes: " + workSeconds, Toast.LENGTH_SHORT).show();
                }

                if (!(workSecondsTextView.getText().toString().isEmpty())) {
                    workEdited = true;
                    edited = true;
                    workSeconds += Integer.parseInt(workSecondsTextView.getText().toString());
                    Toast.makeText(EditSession.this, "Work seconds: " + newWorkTime, Toast.LENGTH_SHORT).show();
                }

                if(workEdited){
                    newWorkTime = workSeconds * 1000;
                    Toast.makeText(EditSession.this, "New work time" + newWorkTime, Toast.LENGTH_SHORT).show();
                    returnIntent.putExtra("newWorkTime", newWorkTime);
                }

                if (!(breakMinuteTextView.getText().toString().isEmpty())) {
                    breakEdited = true;
                    edited = true;
                    breakSeconds = Integer.parseInt(breakMinuteTextView.getText().toString()) * 60;
                }

                if (!(breakSecondsTextView.getText().toString().isEmpty())) {
                    breakEdited = true;
                    edited = true;
                    breakSeconds += Integer.parseInt(breakSecondsTextView.getText().toString());
                }

                if(breakEdited){
                    newBreakTime = breakSeconds * 1000;
                    returnIntent.putExtra("newBreakTime", newBreakTime);
                }

                if (edited) {
                    setResult(Activity.RESULT_OK, returnIntent);
                } else {
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
    }
}
