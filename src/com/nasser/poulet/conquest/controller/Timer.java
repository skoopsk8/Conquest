package com.nasser.poulet.conquest.controller;

import java.util.ArrayList;

/**
 * Created by Lord on 14/12/13.
 */
public class Timer {
    private ArrayList<Long> snapshotList;
    public Long start;  // Program Uptime

    public Timer() {
        snapshotList = new ArrayList<Long>();
        start = System.currentTimeMillis();
    }

    public Long duration( int a, int b ){
        return snapshotList.get(b) - snapshotList.get(a);
    }

    public int addSnapshot(){
        snapshotList.add(System.currentTimeMillis());
        return snapshotList.size()-1;
    }

    public void updateSnapshot( int a ){
        snapshotList.set(a, System.currentTimeMillis());
    }
}
