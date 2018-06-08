package com.sdg.cmdb.template.format.configurationitem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 16/10/13.
 * <p>
 * JAVA_PROJECTS_OPT=(
 * # projectName         groupHead          env                          conf
 * # 物流项目
 * 'logistics'          'logistics'         'daily:gray:production'      '-'
 * 'logistics-job'      'logistics-job'     'daily:gray:production'      '-'
 * # 新版交易
 * 'trade'              'trade'             'daily:gray:production'      '-'
 * # 中央银行
 * 'centralbank'        'centralbank'       'daily:gray:production'      'centralbank.properties:server.properties:logback.groovy'
 * 'centralbank-task'   'centralbank-task'  'daily:gray:production'      '-'
 * )
 */
public class JavaProjectsOpt {

    private List<JavaProjectsOpt> jpos;

    private String projectName;

    private String groupHead;

    private String env;

    private String conf = "-";

    public void setProjectName(String projectName) {
        if (projectName != null) {
            this.projectName = projectName;
        }
    }

    public void setGroupHead(String groupHead) {
        if (groupHead != null) {
            this.groupHead = groupHead;
        }
    }

    public void setEnv(String env) {
        if (env != null) {
            this.env = env;
        }
    }

    public void setConf(String conf) {
        if (conf != null) {
            this.conf = conf;
        }
    }

    public JavaProjectsOpt() {
        this.jpos = new ArrayList<JavaProjectsOpt>();
    }

    public JavaProjectsOpt(String projectName, String groupHead, String env) {
        this.setProjectName(projectName);
        this.setGroupHead(groupHead);
        this.setEnv(env);
    }

    public JavaProjectsOpt(String projectName, String groupHead, String env, String conf) {
        this.setProjectName(projectName);
        this.setGroupHead(groupHead);
        this.setEnv(env);
        this.setConf(conf);
    }

    public static JavaProjectsOpt buider(String projectName, String groupHead, String env){
        JavaProjectsOpt jpo= new JavaProjectsOpt(projectName,groupHead,env);
        return jpo;
    }

    public static JavaProjectsOpt buider(String projectName, String groupHead, String env,String conf){
        JavaProjectsOpt jpo= new JavaProjectsOpt(projectName,groupHead,env,conf);
        return jpo;
    }


    public void put(JavaProjectsOpt jpo) {
        if(jpo ==null) return ;
        this.jpos.add(jpo);
    }

    public String getLine() {
        String result;
        result="'"+this.projectName+"' '"+this.groupHead+"' '"+this.env+"' '"+this.conf+"' \n";
        return result;
    }

    public String toBody() {
        String body= new String();
        body="JAVA_PROJECTS_OPT=( \n";
        for(JavaProjectsOpt j:this.jpos){
            body=body+j.getLine();
        }
        body=body+")\n";
        return body;
    }




}
