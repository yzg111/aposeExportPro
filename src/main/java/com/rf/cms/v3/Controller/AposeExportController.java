package com.rf.cms.v3.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rf.cms.v3.Utils.AposeUtils;
import com.rf.cms.v3.Utils.DownLoadFile;
import com.rf.cms.v3.rest.CMSResponse;
import com.rf.cms.v3.rest.RestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.doc.controller.Login 于2020/8/19 由Administrator 创建 .
 */
@RestController
@Api(description = "导出文件的接口", tags = "导出文件的接口")
@RequestMapping("/export")
public class AposeExportController {

    @Autowired
    private RestUtils restUtils;
    @Value("${sso.web.urljk}")
    public String ssoWebUrlJk;

    @RequestMapping(value = "/ExportWord", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "Apose导出word文档！", notes = "Apose导出word文档！")
    public void ExportWord(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody String params) throws Exception {
        JSONObject params1=JSONObject.parseObject(params);
        String xmselect=params1.getString("xmselect");
        String zqselect=params1.getString("zqselect");
        String cjseclect=params1.getString("cjseclect");
        String zrcselect=params1.getString("zrcselect");
        String lbselect=params1.getString("lbselect");
        String xhselect=params1.getString("xhselect");
        String token=params1.getString("token");
        String ztselect=params1.getString("ztselect");
//        @RequestParam(required = false) String xmselect,
//        @RequestParam(required = false) String zqselect,
//        @RequestParam(required = false) String cjseclect,
//        @RequestParam(required = false) String zrcselect
//                            ,@RequestParam(required = false) String lbselect,
//        @RequestParam(required = false) String xhselect,
//        @RequestParam String token

//        AposeUtils aposeUtils=new AposeUtils();
//        aposeUtils.Open();
//        boolean isok=aposeUtils.InsertTable(null,false);
//        if(isok){
////            aposeUtils.SaveAs("D:\\MyWord.doc");
//            byte[] data=aposeUtils.SaveAsStearm();
//            DownLoadFile.downfile(response,data);
//        }

        Map<String,Object> map=new HashMap<>();
        if (StringUtils.isNotEmpty(xmselect)){
            map.put("项目名称",xmselect);

        }
        if (StringUtils.isNotEmpty(zqselect)){
            map.put("镇区",zqselect);
            map.put("镇区operator",0);
        }
        if (StringUtils.isNotEmpty(cjseclect)){
            map.put("村（居）",cjseclect);
            map.put("村（居）operator",0);
        }
        if (StringUtils.isNotEmpty(zrcselect)){
            map.put("自然村",zrcselect);
            map.put("自然村operator",0);
        }
        if (StringUtils.isNotEmpty(lbselect)){
            map.put("类别",lbselect);
            map.put("类别operator",0);
        }
        if (StringUtils.isNotEmpty(xhselect)){
            map.put("序号",xhselect);
            map.put("序号operator",0);
        }
        if (StringUtils.isNoneEmpty(ztselect)){
            map.put("状态",ztselect);
            map.put("状态operator",0);
        }
        AposeUtils aposeUtils=new AposeUtils();
        aposeUtils.OpenWithModFile();
//        aposeUtils.downloadmodfile(response);
        aposeUtils.downloadRealDataBymod(response,restUtils,map,ssoWebUrlJk,token);

//        AposeUtils aposeUtils=new AposeUtils();
//        aposeUtils.Open();
//        aposeUtils.downloadnotmod(response);

    }

}
