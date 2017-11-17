package com.example.android.pomodorian;

/**
 * Created by lukem on 11/8/2017.
 */

public class Session {
    private String mDate;
    private String mTime;
    private long mDuration;
    private int mWordDuration;
    private int mBreakDuration;
    private int mStreaks;
    private int mKey;

    public Session(String date, String time, long duration,
                   int work_duration, int break_duration, int streaks){
       mDate = date;
       mTime = time;
       mDuration = duration;
       mWordDuration = work_duration;
       mBreakDuration = break_duration;
       mStreaks = streaks;
    }

    public Session(String date, String time, int duration,
                   int work_duration, int break_duration, int streaks, int key){
        mDate = date;
        mTime = time;
        mDuration = duration;
        mWordDuration = work_duration;
        mBreakDuration = break_duration;
        mStreaks = streaks;
        mKey = key;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public long getDuration() {
        return mDuration;
    }

    public long getDurationInMinutes() {
        return mDuration/1000/60;
    }

    public long getDurationInSeconds() {
        return mDuration/1000/60;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getWork_duration() {
        return mWordDuration;
    }

    public void setWork_duration(int work_duration) {
        this.mWordDuration = work_duration;
    }

    public int getBreak_duration() {
        return mBreakDuration;
    }

    public void setBreak_duration(int break_duration) {
        this.mBreakDuration = break_duration;
    }

    public int getStreaks() {
        return mStreaks;
    }

    public void setStreaks(int streaks) {
        this.mStreaks = streaks;
    }

    public int getKey(){return mKey;}

    public void setKey(int key){mKey = key;}
}
