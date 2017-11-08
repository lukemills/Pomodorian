package com.example.android.pomodorian;

import android.app.Activity;

/*
 * A Note on Pomodoro Technique Jargon:
 *  In documenting the various methods and procedures in this code, it is necessary to use various
 *  Pomodoro technique jargon. Here I define various keywords used in the comments below.
 *      - (Work or break) Period: The time wherein the user is working/taking a break
   *        i.e. The work period is the 25 minute time period in which the user works
   *             The break period is the 5 minute time period in which the user takes a break
 *      - Cycle: The time including both the work and break periods
 *          i.e. The total 30 minute time consisting of a work/break pair
 *      - Session: A unbound (by the program) time in which the user completes many cycles
  *         e.g. An 8 hour pomodoro session would conists of many cycles and result in much progress!
 */

// Dependencies
import android.support.v7.app.AppCompatActivity;
import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

// The main activity for the Pomodorian App
public class MainActivity extends Activity {

    /* status is an integer signifying the status of the countdown timer. It can have one of the
       following six values:
        0: No timer is running; waiting for user to start a session and enter work period
        1: Work countdown is running
        2: Work countdown timer complete; waiting for user to start break period
        3: Break countdown is running
        4: Break countdown is complete; waiting for user to start work period
        5: User pressed the stop button during work countdown
        6: User pressed the stop button during break countdown
     */
    int status;

    // A counter to keep track of the number of cycles completed
    int streakCounter = 0;

    // The countdown timers to be used
    private CountDownTimer workCountdown;
    private CountDownTimer breakCountdown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //set status to 0 on create
        status = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // The format to be used in the countdown timer
    private static final String FORMAT = "%02d:%02d";

    /*
     * This method handles the logic for starting/stopping the timer as controlled by the button
     * "toggle," which is named so due to the fact that its text changes depending on the given
     * scenario. Additionally, when a period in the given cycle are finished, this method handles the
     * changing of the timer to the next period.
     *
     * This method is triggered when the user clicks the toggle button. It returns no values and
     * takes no inputs.
     */
    public void startTimer(View view){
        // Obtain a reference to the timer TextView, toggle Button, statusText textView, and
        // streakCounterTextView TextView
        final TextView timer = (TextView) findViewById(R.id.timer_text_view);
        final Button toggle = (Button) findViewById(R.id.toggle_button);
        final TextView statusText = (TextView) findViewById(R.id.status_text_view);
        final TextView streakCounterTextView = (TextView) findViewById(R.id.streak_counter);

        // Update the streakCounterTextView to depict the most recent streak count
        streakCounterTextView.setText(Integer.toString(streakCounter));


        // When the toggle button is pressed, an action will be performed based on the value of "status"
        // This switch statements handles the logic in order to perform the appropriate action.
        switch(status){
            // When status == 0, countdown timer has not been started; not currently in pomodoro session.
            // Since user pressed the button, he/she wants to start a session; hence a work period
            // will begin. status, statusText, and the toggle button are updated accordingly.
            case 0:
                status = 1;
                toggle.setText(getString(R.string.stop_literal));
                statusText.setText(getString(R.string.work_status));
                // Initialize and start a countdown timer for the work period
                workCountdown = new CountDownTimer(1500000, 1000) {
                    //Perform an update to the timer TextView on each tick
                    public void onTick(long millisUntilFinished) {
                        timer.setText("" + String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    //When finished, update status and statusText accordingly and set statusText to let the
                    // user know it's break time
                    public void onFinish() {
                        timer.setText(getString(R.string.time_over));
                        status = 2;
                        statusText.setText(getString(R.string.break_status));
                        toggle.setText(getString(R.string.start_literal));
                    }
                }.start();
                break;
            // When status == 1, user is in work period (workCountdown timer has been started),
            // and if button is pressed, user wants to stop session
            case 1:
                status = 5;
                toggle.setText(getString(R.string.reset_literal));
                statusText.setText(getString(R.string.stopped_literal));
                workCountdown.cancel();
                break;
            // When status == 2, the work session has ended and the app prompts for the user to confirm
            // that he/she is ready to begin the break period, and thus the breakCountdown timer is started
            case 2:
                status = 3;
                toggle.setText(getString(R.string.stop_literal));
                statusText.setText(getString(R.string.relax_status));
                breakCountdown = new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer.setText("" + String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    // When the break timer is finished, the user will enter into another work period.
                    // First, the timer TextView's text is reset to '00:00,' status is reset,
                    // the streakCounter is updated, and the new streak count is displayed. Then the
                    // toggle button is updated to depict "Begin" prompting a user to confirm the starting
                    // of a work period. Status is also updated to display a playful message.
                    public void onFinish() {
                        timer.setText(getString(R.string.time_over));
                        status = 0;
                        streakCounter += 1;
                        streakCounterTextView.setText(Integer.toString(streakCounter));
                        toggle.setText(getString(R.string.begin_literal));
                        statusText.setText(getString(R.string.start_work));
                    }
                }.start();
                break;

            // When status == 3, the user is in the break period (breakCountdown timer started),
            // and when the button is pressed, the user wants to stop this countdown.
            case 3:
                breakCountdown.cancel();
                status = 6;
                toggle.setText(getString(R.string.reset_literal));
                statusText.setText(getString(R.string.stopped_literal));
                break;

            // When status == 5, the user has paused the workCountdown timer, and if the button is pressed,
            // then the user wants to resume his/her work session. The streakCounter is set to zero and the
            // streakCounterTextView is updated accordingly. All other elements are reset in a method similar
            // to the beginning of a work cycle; the difference being the lack of incrementing streakCounter.
            case 5:
                status = 1;
                toggle.setText(R.string.stop_literal);
                streakCounter = 0;
                streakCounterTextView.setText(Integer.toString(streakCounter));
                statusText.setText(getString(R.string.work_status));
                workCountdown.start();
                break;

            // When status == 6, the user has paused the breakCountdown timer, and if the button is pressed,
            // then the user wants to restart a work session. The streakCounter is set to zero and the
            // streakCounterTextView is updated accordingly. All other elements are reset in a method similar
            // to the beginning of a work cycle; the difference being the lack of incrementing streakCounter.
            case 6:
                status = 1;
                toggle.setText(getString(R.string.stop_literal));
                statusText.setText(getString(R.string.work_status));
                streakCounter = 0;
                streakCounterTextView.setText(Integer.toString(streakCounter));
                workCountdown.start();
                break;
            default:
                break;
        }
    }

}