package com.rf.cms.v3.Utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Comparator;

/**
 * Created by Administrator on 2020/8/30.
 */
public class ArrayComprator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        JSONObject obj1=((JSONObject) o1).getJSONObject("dataFieldMap");
        JSONObject obj2=((JSONObject) o2).getJSONObject("dataFieldMap");
        int opx1=10000;
        int opx2=10000;
        if (obj1.get("排序号")!=null&&!"".equals(obj1.get("排序号"))){
            opx1=Integer.parseInt(obj1.getString("排序号"));
        }
        if (obj2.get("排序号")!=null&&!"".equals(obj2.get("排序号"))){
            opx2=Integer.parseInt(obj2.getString("排序号"));
        }
        if(opx1>opx2){
        return -1;
        }
        if(opx1<opx2){
        return 1;
        }
        return 0;
    }
}
