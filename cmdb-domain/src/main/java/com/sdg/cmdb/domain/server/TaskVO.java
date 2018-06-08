package com.sdg.cmdb.domain.server;

import java.io.Serializable;

public class TaskVO implements Serializable {
    private String beginTime;

    private boolean running;


    public TaskVO(){

    }

    public TaskVO(String beginTime,boolean running){
        this.beginTime=beginTime;
        this.running = running;

    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
