package com.rf.cms.v3.RunThread;

import com.rf.cms.v3.rest.RestUtils;

import java.util.Map;

/**
 * com.rf.cms.v3.RunThread 于2019/7/2 由Administrator 创建 .
 */
public class RunThread extends Thread {


    private String scriptname;
    private Map<String,Object> data;
    private RestUtils restUtils;
    public RunThread(String scriptname, Map<String, Object> data,RestUtils restUtils){
        this.scriptname=scriptname;
        this.data=data;
        this.restUtils=restUtils;
    }

    public void run(){
        getRestUtils().execScript(getScriptname(),getData());
    }


    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    public RestUtils getRestUtils() {
        return restUtils;
    }

    public void setRestUtils(RestUtils restUtils) {
        this.restUtils = restUtils;
    }
}
