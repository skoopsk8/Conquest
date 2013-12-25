package com.nasser.poulet.conquest.model;

import com.nasser.poulet.conquest.controller.Timer;
import com.nasser.poulet.conquest.controller.Turn;

import java.util.Objects;

/**
 * Created by Lord on 15/12/13.
 */

public class Event {
    private int occurrence;     // Occurrence before auto-destroy MI style
    private String name;
    private Callback callback;
    int lastCall;   // Last call snapshot id
    int interval;   // Interval between two successive call
    Object context; // For callback re context

    public Event( int occurrence, int interval, Object context, Callback callback ){
        this.occurrence = occurrence;
        this.callback = callback;
        this.interval = interval;
        this.lastCall = Timer.addSnapshot();
        this.context = context;
    }

    public int getLastCall() {
        return lastCall;
    }

    public int getInterval() {
        return interval;
    }

    public boolean call(){
        Timer.updateSnapshot(lastCall);

        this.callback.methodCallback(this.context);
        if(this.occurrence!=-1){    // -1 = infinity
            this.occurrence--;

            if(this.occurrence <= 0) return true;  // Auto destroy
        }
        return false;
    }
}
