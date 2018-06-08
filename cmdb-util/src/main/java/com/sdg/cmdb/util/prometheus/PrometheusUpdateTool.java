package com.sdg.cmdb.util.prometheus;

/**
 * Created by liangjian on 16/10/18.
 */
public class PrometheusUpdateTool extends AbsTools {


    @Override
    protected String getBinPath() {
        return "bin/prometheus_update";
    }


    public PrometheusUpdateTool() {
        super();
    }

    @Override
    protected void makeParams() {

    }

}
