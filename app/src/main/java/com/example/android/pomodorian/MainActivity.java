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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

// The main activity for the Pomodorian App
public class MainActivity extends AppCompatActivity {

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

    // ListView to be used with the Navigation Drawer
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private PomodoroAppDBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //set status to 0 on create
        status = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the Navigation Drawer
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Show the toggle for the Navigation drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Get reference to the drawer layout and current activity
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        dbHelper = new PomodoroAppDBHelper(this, "pomodorian", null, 1);

        // Instantiate the entries in the nav drawer
        addNavDrawerItems();
        setupDrawer();
    }

    // Sync the Nav Drawer icon with the current activity
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    // The format to be used in the countdown timer
    private static final String FORMAT = "%02d:%02d";

    public void destroyDB(){
        dbHelper.deleteAll();
    }

    /*
     * This method handles the logic for starting/stopping the timer as controlled by the button
     * "toggle," which is named so due to the fact that its text changes depending on the given
     * scenario. Additionally, when a period in the given cycle are finished, this method handles the
     * changing of the timer to the next period.
     *
     * This method is triggered when the user clicks the toggle button. It returns no values and
     * takes no inputs.
     */
    public void startTimer(View view) {
        // Obtain a reference to the timer TextView, toggle Button, statusText textView, and
        // streakCounterTextView TextView
        final TextView timer = (TextView) findViewById(R.id.timer_text_view);
        final Button toggle = (Button) findViewById(R.id.toggle_button);
        final TextView statusText = (TextView) findViewById(R.id.status_text_view);
        final TextView streakCounterTextView = (TextView) findViewById(R.id.streak_counter);

        // Update the streakCounterTextView to depict the most recent streak count
        streakCounterTextView.setText(Integer.toString(streakCounter));
        DialogFragment saveSessionFragment;


        // When the toggle button is pressed, an action will be performed based on the value of "status"
        // This switch statements handles the logic in order to perform the appropriate action.
        switch (status) {
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

                new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.save_session))
                    .setMessage("Would you like to save this session?")
                    .setPositiveButton(R.string.save_literal, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Session sesh = new Session(
                                    new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()),
                                    new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()),
                                    (25*streakCounter+5*streakCounter),
                                    (25*streakCounter),
                                    (5*streakCounter),
                                    streakCounter
                            );
                            dbHelper.addSession(sesh);

                            Toast.makeText(MainActivity.this,
                                    "Session saved; streaks: "+Integer.toString(streakCounter) +
                                            " date " + new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime())
                                    +" time: "
                                    + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())
                                    , Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.dont_save_literal, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Session not saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
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
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.save_session))
                        .setMessage("Would you like to save this session?")
                        .setPositiveButton(R.string.save_literal, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Session sesh = new Session(
                                        new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()),
                                        new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()),
                                        (25*streakCounter+5*streakCounter),
                                        (25*streakCounter),
                                        (5*streakCounter),
                                        streakCounter
                                );
                                dbHelper.addSession(sesh);

                                Toast.makeText(MainActivity.this, "Session saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.dont_save_literal, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "Session not saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
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


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void addNavDrawerItems() {
        // ArrayAdapter to be used with the Navigation Drawer
        ArrayAdapter<String> mAdapter;
        final String[] activitiesArray = {getString(R.string.session), getString(R.string.history)};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activitiesArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String activity = activitiesArray[position];
                if (position == 1) {
                    Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(historyIntent);
                }
                /*
                if (position == 0 && (!parentList.equals(listNames.get(0)))) {
                    // Open an intent to the first list
                    Intent list1Intent = new Intent(MainActivity.this, classesArray[0]);
                    new Utils().putListExtrasInIntentBundle(list1Intent, listNames);
                    list1Intent.putExtra("parentList", listNames.get(0));
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(list1Intent);
                }
                */
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item click
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

