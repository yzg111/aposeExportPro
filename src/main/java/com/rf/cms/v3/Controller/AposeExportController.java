package com.rf.cms.v3.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rf.cms.v3.Utils.AposeUtils;
import com.rf.cms.v3.Utils.DownLoadFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * com.doc.controller.Login 于2020/8/19 由Administrator 创建 .
 */
@RestController
@Api(description = "导出文件的接口", tags = "导出文件的接口")
@RequestMapping("/api")
public class AposeExportController {


    @RequestMapping(value = "/ExportWord", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Apose导出word文档！", notes = "Apose导出word文档！")
    public void ExportWord(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        AposeUtils aposeUtils=new AposeUtils();
//        aposeUtils.Open();
//        boolean isok=aposeUtils.InsertTable(null,false);
//        if(isok){
////            aposeUtils.SaveAs("D:\\MyWord.doc");
//            byte[] data=aposeUtils.SaveAsStearm();
//            DownLoadFile.downfile(response,data);
//        }


        AposeUtils aposeUtils=new AposeUtils();
        aposeUtils.OpenWithModFile();
        aposeUtils.downloadmodfile(response);

//        AposeUtils aposeUtils=new AposeUtils();
//        aposeUtils.Open();
//        aposeUtils.downloadnotmod(response);

    }

}
