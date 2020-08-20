package com.rf.cms.v3.RunThread;

import com.rf.cms.v3.rest.RestUtils;

/**
 * com.rf.cms.v3.RunThread 于2019/7/15 由Administrator 创建 .
 */
public class HttpThread extends Thread {


    private RestUtils restUtils;
    private String postPath, postRequest;

    public HttpThread(String postPath, String postRequest, RestUtils restUtils) {
    this.postPath=postPath;
    this.postRequest=postRequest;
    this.restUtils=restUtils;
    }

    @Override
    public void run() {
        getRestUtils().ApiPost(this.getPostPath(),this.getPostRequest());
    }

    public RestUtils getRestUtils() {
        return restUtils;
    }

    public void setRestUtils(RestUtils restUtils) {
        this.restUtils = restUtils;
    }

    public String getPostPath() {
        return postPath;
    }

    public void setPostPath(String postPath) {
        this.postPath = postPath;
    }

    public String getPostRequest() {
        return postRequest;
    }

    public void setPostRequest(String postRequest) {
        this.postRequest = postRequest;
    }

}
