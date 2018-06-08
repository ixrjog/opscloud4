package com.sdg.cmdb.domain.logCleanup;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/3/30.
 */
public class LogCleanupConfigurationDO implements Serializable {
    private static final long serialVersionUID = 3541473473286489112L;

    private String envTypeName;

    private int envType;

    private int history;

    private int min;

    private int max;

    public String getEnvTypeName() {
        return envTypeName;
    }

    public void setEnvTypeName(String envTypeName) {
        this.envTypeName = envTypeName;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }


    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "LogCleanupConfigurationDO{" +
                "envTypeName='" + envTypeName + '\'' +
                ", envType=" + envType +
                ", history='" + history +
                ", min=" + min +
                ", max='" + max +
                '}';
    }

}
