package com.sdg.cmdb.domain.ansibleTask;


import java.io.Serializable;

public class TaskResult implements Serializable {

    public TaskResult() {

    }

    public TaskResult(String cmd, String argument, String result) {
        this.cmd = cmd;
        this.result = result;
        this.argument = argument;
    }

    public TaskResult(String argument, String result) {
        this.result = result;
        this.argument = argument;
    }

    @Override
    public String toString() {
        return argument;
    }

    private String result;

    private String cmd;

    private String argument;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
