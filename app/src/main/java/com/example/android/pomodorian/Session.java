package com.example.android.pomodorian;

/**
 * Created by lukem on 11/8/2017.
 */

public class Session {
    private String date;
    private String time;
    private int duration;
    private int work_duration;
    private int break_duration;
    private int streaks;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWork_duration() {
        return work_duration;
    }

    public void setWork_duration(int work_duration) {
        this.work_duration = work_duration;
    }

    public int getBreak_duration() {
        return break_duration;
    }

    public void setBreak_duration(int break_duration) {
        this.break_duration = break_duration;
    }

    public int getStreaks() {
        return streaks;
    }

    public void setStreaks(int streaks) {
        this.streaks = streaks;
    }
}
