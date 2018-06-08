package com.sdg.cmdb.util.prometheus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 16/10/14.
 */
public abstract class AbsTools {

    protected String prometheusHome = "/usr/local/prometheus";

    protected abstract String getBinPath();

    /**
     * 参数集合
     */
    protected List<String> cmdParams;

    public void putParam(String param) {
        this.cmdParams.add(param);
    }

    public AbsTools() {
        this.cmdParams = new ArrayList<String>();
    }


    /**
     * cmd路径
     *
     * @return
     */
    protected String getCmdPath(){
        return prometheusHome + "/" + getBinPath();
    }


    protected abstract void makeParams();

    /**
     * 取命令行
     *
     * @return
     */
    public String getCmdLine() {
        String params = "";
        makeParams();
        for (String p : this.cmdParams) params = params + p + " ";
        return this.getCmdPath() + " " + params;
    }


}
