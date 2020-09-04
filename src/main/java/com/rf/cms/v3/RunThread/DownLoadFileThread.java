package com.rf.cms.v3.RunThread;

import com.rf.cms.v3.Utils.DownLoadFile;

/**
 * Created by Administrator on 2020/8/23.
 */
public class DownLoadFileThread extends Thread{

    private String filepath;
    private String filename;
    private String url;

    public DownLoadFileThread(String url,String filepath,String filename){
    this.url=url;
    this.filepath=filepath;
    this.filename=filename;
    }

    @Override
    public void run() {
        try {
            DownLoadFile.downloadFileFromUrlByname(this.url,this.filepath,this.filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
