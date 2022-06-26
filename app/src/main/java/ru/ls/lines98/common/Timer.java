package ru.ls.lines98.common;

import android.os.Handler;

public class Timer {
    Handler h;
    Runnable TimerTicker;
    Runnable IncomingRunnable;
    Thread workThread;
    int Period = 0;
    boolean IsRunning = false;
    public Timer(int period, Runnable mTimerTicker){
        IncomingRunnable = mTimerTicker;
        Period = period;
        TimerTicker = new Runnable() {
            @Override
            public void run() {
                while (IsRunning){
                    IncomingRunnable.run();
                    try {
                        Thread.sleep(Period);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }
    public void start(){
        IsRunning = true;
        workThread = new Thread(TimerTicker);
        workThread.start();
    }
    public void stop(){
        IsRunning = false;
    }

    public boolean isRunning() {
        return IsRunning;
    }
}
