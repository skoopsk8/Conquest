package com.nasser.poulet.conquest.model;

/**
 * Created by Lord on 15/12/13.
 */

public class Event {
    private int occurrence;     // Occurrence before auto-destroy MI style
    private String name;
    private Callback callback;

    public Event( int occurrence, Callback callback ){
        this.occurrence = occurrence;
        this.callback = callback;
    }

    public boolean call(){
        this.callback.methodCallback();
        this.occurrence--;

        if(this.occurrence <= 0) return true;  // Auto destroy
        return false;
    }
}
