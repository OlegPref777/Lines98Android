package ru.ls.lines98.game.common;

import android.os.Handler;

public class Timer {
    Handler h;
    Runnable TimerTicker;
    Runnable IncomingRunnable;
    int Period = 0;
    boolean IsRunning = false;
    public Timer(int period, Runnable mTimerTicker){
        IncomingRunnable = mTimerTicker;
        TimerTicker = new Runnable() {
            @Override
            public void run() {
                if (IsRunning){
                    h.removeCallbacks(TimerTicker);
                    h.postDelayed(TimerTicker, Period);
                    IncomingRunnable.run();
                }
            }
        };
        h = new Handler();
    }
    public void start(){
        if (h != null && Period > 0) {
            h.removeCallbacks(TimerTicker);
            h.postDelayed(TimerTicker, Period);
            IsRunning = true;
        }
    }
    public void stop(){
        if (h != null) {
            h.removeCallbacks(TimerTicker);
        }
        IsRunning = false;
    }

    public boolean isRunning() {
        return IsRunning;
    }
}
