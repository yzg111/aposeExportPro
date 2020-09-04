package com.rf.cms.v3.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.slides.*;
import com.aspose.slides.Chart;
import com.aspose.slides.DataTable;
import com.aspose.words.*;
import com.aspose.words.License;
import com.aspose.words.LineStyle;
import com.aspose.words.SaveFormat;
import com.aspose.words.Shape;
import com.aspose.words.Table;
import com.aspose.words.net.System.Data.*;
import com.rf.cms.v3.RunThread.DownLoadFileThread;
import com.rf.cms.v3.rest.CMSResponse;
import com.rf.cms.v3.rest.RestUtils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

/**
 * com.rf.cms.v3.Utils 于2020/8/20 由Administrator 创建 .
 */
public class AposeUtils {

    private Document doc;
    private DocumentBuilder docBuilder;
    private java.util.List<Map<String,String>> datas=new ArrayList<Map<String,String>>();
    private List<String> paths=new ArrayList<>();

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = AposeUtils.class.getClassLoader().getResourceAsStream("license.xml"); // license.xml应放在…\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void OpenWithTemplate(String fileName) throws Exception           //不覆盖word里已有的Table
    {
        if (StringUtils.isNotEmpty(fileName))
        {
            doc = new Document(fileName);
            docBuilder = new DocumentBuilder(doc);
        }
    }

    public void Open() throws Exception                               //覆盖word里已有的Table
    {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return ;
        }
        doc = new Document();
        docBuilder = new DocumentBuilder(doc);

        for(int i=0;i<10;i++){
            Map<String,String> map=new HashedMap();
            map.put("name","姓名"+i);
            map.put("value","值"+i);
            if (i==2){
                map.put("img","D:\\0.jpg");
            }else {
                map.put("img","D:\\yasuo.jpg");
            }

            datas.add(map);
        }
    }

    public void OpenWithModFile() throws Exception                               //覆盖word里已有的Table
    {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return ;
        }
        System.out.println("文件路径:"+AposeUtils.class.getClassLoader().getResource("mod1.docx").getPath());
        InputStream inputStream=AposeUtils.class.getClassLoader().getResourceAsStream("mod1.docx");
        doc =  new Document(inputStream);
//        docBuilder = new DocumentBuilder(doc);
    }

    public void OpenWithTestFile() throws Exception {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return ;
        }
        System.out.println("文件路径:"+AposeUtils.class.getClassLoader().getResource("test.docx").getPath());
        InputStream inputStream=AposeUtils.class.getClassLoader().getResourceAsStream("test.docx");
        doc =  new Document(inputStream);
    }

    /// <summary>
    /// 保存文件
    /// </summary>
    /// <param name="fileName"></param>
    public void SaveAs(String fileName) throws Exception {
        doc.save(fileName, SaveFormat.DOC);
    }
    /// <summary>
    /// 保存数据流信息
    /// </summary>
    /// <param name="fileName"></param>
    public  byte[] SaveAsStearm() throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        doc.save(outStream, SaveFormat.DOC);
        return outStream.toByteArray();
    }

    //根据模板生成文件
    public void downloadmodfile(HttpServletResponse response) throws Exception {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return ;
        }
        //3.1 填充单个文本域
        String[] Flds = new String[]{"Title"}; //文本域
        Object[] Vals = new Object[]{"标题"}; //值
        doc.getMailMerge().execute(Flds,Vals);
        com.aspose.words.net.System.Data.DataTable dataTable=
                new com.aspose.words.net.System.Data.DataTable("All");
        dataTable.getColumns().add("排序号"); // 0 增加三个列
        dataTable.getColumns().add("备注"); // 1
        dataTable.getColumns().add("详细地址"); // 2
        dataTable.getColumns().add("检查情况"); // 2
        dataTable.getColumns().add("整改反馈"); // 2
        dataTable.getColumns().add("检查图片"); // 2
        dataTable.getColumns().add("整改图片"); // 2
        // 向表格中填充数据
        for (int i = 1; i < 15; i++) {
            DataRow row = dataTable.newRow(); // 新增一行
            row.set(0, i); // 根据列顺序填入数值
            row.set(1, "评估内容"+i);
            row.set(2, "检查地址"+i);
            row.set(3, "检查情况"+i); // 根据列顺序填入数值
            row.set(4, "整改情况"+i);
//            Shape img = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("1.jpg"),
//                    RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
//                    1, 100, 125, WrapType.SQUARE);
//            img.setWidth(100);
//            img.setHeight(30);
//            img.setHorizontalAlignment(HorizontalAlignment.CENTER);
            row.set(5, "");
//            Shape img1 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("1.jpg"),
//                    RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
//                    1, 100, 125, WrapType.SQUARE);
//            img1.setWidth(100);
//            img1.setHeight(30);
//            img1.setHorizontalAlignment(HorizontalAlignment.CENTER);
            row.set(6, "");
            dataTable.getRows().add(row); // 加入此行数据
        }
        doc.getMailMerge().executeWithRegions(dataTable);
        docBuilder = new DocumentBuilder(doc);
        for (int i=1;i<15;i++){
            docBuilder.moveToCell(i-1,2,0,0);
            Shape img1 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("1.jpg"),
                    RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                    1, 100, 125, WrapType.SQUARE);
            img1.setWidth(370);
            img1.setHeight(300);
            img1.setHorizontalAlignment(HorizontalAlignment.CENTER);
            docBuilder.moveToCell(i-1,2,1,0);
            Shape img2 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("2.jpg"),
                    RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                    1, 100, 125, WrapType.SQUARE);
            img2.setWidth(370);
            img2.setHeight(300);
            img2.setHorizontalAlignment(HorizontalAlignment.CENTER);
        }

//        docBuilder.moveToCell(0,2,0,0);
//        Shape img1 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("1.jpg"),
//                RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
//                1, 100, 125, WrapType.SQUARE);
//        img1.setWidth(370);
//        img1.setHeight(300);
//        img1.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        docBuilder.moveToCell(0,2,1,0);
//        Shape img2 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("2.jpg"),
//                RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
//                1, 100, 125, WrapType.SQUARE);
//        img2.setWidth(370);
//        img2.setHeight(300);
//        img2.setHorizontalAlignment(HorizontalAlignment.CENTER);

            response.setHeader("Content-Disposition", "attachment; filename=1234.docx");
        response.setContentType("application/octet-stream;charset=UTF-8");

        OutputStream output = response.getOutputStream();
        doc.save(output, SaveFormat.DOC);

        output.flush();
        output.close();

    }

    public  void downloadRealDataBymod(HttpServletResponse response,
                                       RestUtils restUtils,Map<String,Object>map,
                                       String ssoWebUrlJk,String ssoWebToken) throws Exception {
        JSONArray pxjsonArray=new JSONArray();
        JSONObject pxjsonObject=new JSONObject();
        pxjsonObject.put("attribute","排序号");
        pxjsonObject.put("direction",-1);
        pxjsonArray.add(pxjsonObject);
        CMSResponse res=restUtils.getCiByAttr("报表数据",map,pxjsonArray);
//        CMSResponse res=restUtils.getCiByAttr("报表数据",map);
        System.out.println(res);
        JSONObject jsonObject=new JSONObject((Map<String, Object>) res.getContent());
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            // You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "排序号";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                int opx1=10000;
                int opx2=10000;
                    // 这里是a、b需要处理的业务，需要根据你的规则进行修改。
                    JSONObject obj1=a.getJSONObject("dataFieldMap");
                    JSONObject obj2=b.getJSONObject("dataFieldMap");
                    if (obj1.get(KEY_NAME)!=null&&!"".equals(obj1.get(KEY_NAME))){
                        opx1=Integer.parseInt(obj1.getString(KEY_NAME));
                    }
                    if (obj2.get(KEY_NAME)!=null&&!"".equals(obj2.get(KEY_NAME))){
                        opx2=Integer.parseInt(obj2.getString(KEY_NAME));
                    }
                    if(opx1>opx2){
                        return 1;
                    }
                    if(opx1<opx2){
                        return -1;
                    }
                    return 0;

                // if you want to change the sort order, simply use the following:
                // return -valA.compareTo(valB);
            }
        });
        JSONArray sortedJsonArray = new JSONArray();
        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        System.out.println(sortedJsonArray.toJSONString());
        com.aspose.words.net.System.Data.DataTable dataTable=
                new com.aspose.words.net.System.Data.DataTable("All");
        dataTable.getColumns().add("自然村"); // 0 增加三个列
        dataTable.getColumns().add("备注"); // 1
        dataTable.getColumns().add("详细地址"); // 2
        dataTable.getColumns().add("检查情况"); // 2
        dataTable.getColumns().add("整改反馈"); // 2
        dataTable.getColumns().add("检查图片"); // 2
        dataTable.getColumns().add("整改图片"); // 2
        dataTable.getColumns().add("排序号"); // 2
        for (int i=0;i<sortedJsonArray.size();i++){
            JSONObject jobject=sortedJsonArray.getJSONObject(i).getJSONObject("dataFieldMap");
            DataRow row = dataTable.newRow(); // 新增一行
            row.set(0, jobject.get("自然村")); // 根据列顺序填入数值
            row.set(1, jobject.get("备注"));
            row.set(2, jobject.get("详细地址"));
            row.set(3, jobject.get("检查情况")); // 根据列顺序填入数值
            row.set(4, jobject.get("整改反馈"));
            row.set(5, "");
            row.set(6, "");
            row.set(7, jobject.get("排序号"));
            dataTable.getRows().add(row); // 加入此行数据
        }
        doc.getMailMerge().executeWithRegions(dataTable);
        docBuilder = new DocumentBuilder(doc);
//        DownLoadFile.getInputStream(ssoWebUrlJk+"/cms/v1/files/transform?uri=/2020/07/5f0c0ae7e4b0293d5cfe16c1.jpg&access-token="+ssoWebToken);
        for (int i=0;i<sortedJsonArray.size();i++){
            JSONObject jobject=sortedJsonArray.getJSONObject(i).getJSONObject("dataFieldMap");
            if(jobject.get("检查图片")!=null){
                String[] tmp=jobject.get("检查图片").toString().split("/");
                String filePath=AposeUtils.class.getClassLoader().getResource("").getPath()+tmp[1]+"/"+tmp[2];
                File file = new File(filePath+"/"+tmp[3]);
                if(file.exists()&&file.length()==0){
                    file.delete();
                }
                DownLoadFileThread downLoadFileThread=new DownLoadFileThread(ssoWebUrlJk+"/cms/v1/files/transform?uri="
                        +jobject.get("检查图片")+"&access-token=Bearer%20"+ssoWebToken,
                        filePath,tmp[3]);
                downLoadFileThread.start();
            }
            if(jobject.get("整改图片")!=null){
                String[] tmp=jobject.get("整改图片").toString().split("/");
                String filePath=AposeUtils.class.getClassLoader().getResource("").getPath()+tmp[1]+"/"+tmp[2];
                File file = new File(filePath+"/"+tmp[3]);
                if(file.exists()&&file.length()==0){
                    file.delete();
                }
                DownLoadFileThread downLoadFileThread=new DownLoadFileThread(ssoWebUrlJk+"/cms/v1/files/transform?uri="
                        +jobject.get("整改图片")+"&access-token=Bearer%20"+ssoWebToken,
                        filePath,tmp[3]);
                downLoadFileThread.start();
            }
//            Thread.sleep(1000);
        }
        if(sortedJsonArray.size()>50){
            Thread.sleep(1000*100);
        }else {
            Thread.sleep(1000*40);
        }
//        Thread.sleep(2000*jsonArray.size());

        for (int i=0;i<sortedJsonArray.size();i++){
            JSONObject jobject=sortedJsonArray.getJSONObject(i).getJSONObject("dataFieldMap");
            if(jobject.get("检查图片")!=null){
                String[] tmp=jobject.get("检查图片").toString().split("/");
                String filePath=AposeUtils.class.getClassLoader().getResource("").getPath()+tmp[1]+"/"+tmp[2];
//                String imgflie=DownLoadFile.downloadFileFromUrlByname(ssoWebUrlJk+"/cms/v1/files/transform?uri="+jobject.get("检查图片")+"&access-token=Bearer%20"+ssoWebToken
//                ,filePath,tmp[3]);


//            File targetFile = new File(filePath);
//            if (!targetFile.exists()) {
//                targetFile.mkdirs();
//
//            }
//                File file = new File(filePath+"/"+tmp[3]);
//                if(!file.exists()){
//                    FileOutputStream out = new FileOutputStream(filePath+"/"+tmp[3]);
//
//                    ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//                    byte[] buffer = new byte[1024];
//                    int length;
//
//                    while ((length = imgs.read(buffer)) > 0) {
//                        output.write(buffer, 0, length);
//                    }
//                    byte[] context=output.toByteArray();
//                    out.write(output.toByteArray());
//                    imgs.close();
//                    out.close();
//                } else {
//                    imgs.close();
//                }
//                Shape img1 = docBuilder.insertImage(imgs,
//                        RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
//                        1, 100, 125, WrapType.SQUARE);
                File file = new File(filePath+"/"+tmp[3]);
                if(file.exists()&&file.length()>0){
                        docBuilder.moveToCell(i,3,0,0);
                        Shape img1 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream(jobject.get("检查图片").toString().substring(1)),
                                RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                                1, 100, 125, WrapType.SQUARE);
                        img1.setWidth(370);
                        img1.setHeight(300);
                        img1.setHorizontalAlignment(HorizontalAlignment.CENTER);

                }else {
//                    paths.add(filePath+"/"+tmp[3]);
                    file.delete();
                }
            }

            if(jobject.get("整改图片")!=null){
                String[] tmp=jobject.get("整改图片").toString().split("/");
                String filePath=AposeUtils.class.getClassLoader().getResource("").getPath()+tmp[1]+"/"+tmp[2];
//                String imgzg=DownLoadFile.downloadFileFromUrlByname(ssoWebUrlJk+"/cms/v1/files/transform?uri="+jobject.get("整改图片")+"&access-token=Bearer%20"+ssoWebToken,
//                        filePath,tmp[3]);

//                File targetFile = new File(filePath);
//                if (!targetFile.exists()) {
//                    targetFile.mkdirs();
//
//                }
//                File file = new File(filePath+"/"+tmp[3]);
//                if(!file.exists()){
//                    FileOutputStream out = new FileOutputStream(filePath+"/"+tmp[3]);
//
//                    ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//                    byte[] buffer = new byte[1024];
//                    int length;
//
//                    while ((length = imgzg.read(buffer)) > 0) {
//                        output.write(buffer, 0, length);
//                    }
//                    byte[] context=output.toByteArray();
//                    out.write(output.toByteArray());
//                    imgzg.close();
//                    out.close();
//                } else {
//                    imgzg.close();
//                }
                File file = new File(filePath+"/"+tmp[3]);
//                System.out.println(filePath+"/"+tmp[3]+":"+file.length());
                if(file.exists()&&file.length()>0) {
                        docBuilder.moveToCell(i,3,1,0);
                        Shape img2 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream(jobject.get("整改图片").toString().substring(1)),
                                RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                                1, 100, 125, WrapType.SQUARE);
//                Shape img2 = docBuilder.insertImage(imgzg,
//                        RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
//                        1, 100, 125, WrapType.SQUARE);
                        img2.setWidth(370);
                        img2.setHeight(300);
                        img2.setHorizontalAlignment(HorizontalAlignment.CENTER);
                }else {
//                    paths.add(filePath+"/"+tmp[3]);
                    file.delete();
                }
            }

        }
        response.setHeader("Content-Disposition", "attachment; filename=1234.docx");
        response.setContentType("application/octet-stream;charset=UTF-8");

        OutputStream output = response.getOutputStream();
        doc.save(output, SaveFormat.DOC);

        output.flush();
        output.close();

    }

    public  void downloadRealDataBymodLocalImg(HttpServletResponse response,
                                       RestUtils restUtils,Map<String,Object>map,
                                       String ssoWebUrlJk,String ssoWebToken) throws Exception {
        JSONArray pxjsonArray=new JSONArray();
        JSONObject pxjsonObject=new JSONObject();
        pxjsonObject.put("attribute","排序号");
        pxjsonObject.put("direction",-1);
        pxjsonArray.add(pxjsonObject);
        CMSResponse res=restUtils.getCiByAttr("报表数据",map,pxjsonArray);
//        CMSResponse res=restUtils.getCiByAttr("报表数据",map);
        System.out.println(res);
        JSONObject jsonObject=new JSONObject((Map<String, Object>) res.getContent());
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "排序号";
            @Override
            public int compare(JSONObject a, JSONObject b) {
                int opx1=1000000;
                int opx2=1000000;
                // 这里是a、b需要处理的业务，需要根据你的规则进行修改。
                JSONObject obj1=a.getJSONObject("dataFieldMap");
                JSONObject obj2=b.getJSONObject("dataFieldMap");
                if (obj1.get(KEY_NAME)!=null&&!"".equals(obj1.get(KEY_NAME))){
                    opx1=Integer.parseInt(obj1.getString(KEY_NAME));
                }
                if (obj2.get(KEY_NAME)!=null&&!"".equals(obj2.get(KEY_NAME))){
                    opx2=Integer.parseInt(obj2.getString(KEY_NAME));
                }
                if(opx1>opx2){
                    return 1;
                }
                if(opx1<opx2){
                    return -1;
                }
                return 0;
            }
        });
        JSONArray sortedJsonArray = new JSONArray();
        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        System.out.println(sortedJsonArray.toJSONString());
        com.aspose.words.net.System.Data.DataTable dataTable=
                new com.aspose.words.net.System.Data.DataTable("All");
        dataTable.getColumns().add("自然村"); // 0 增加三个列
        dataTable.getColumns().add("备注"); // 1
        dataTable.getColumns().add("详细地址"); // 2
        dataTable.getColumns().add("检查情况"); // 2
        dataTable.getColumns().add("整改反馈"); // 2
        dataTable.getColumns().add("检查图片"); // 2
        dataTable.getColumns().add("整改图片"); // 2
        dataTable.getColumns().add("排序号"); // 2
        for (int i=0;i<sortedJsonArray.size();i++){
            JSONObject jobject=sortedJsonArray.getJSONObject(i).getJSONObject("dataFieldMap");
            DataRow row = dataTable.newRow(); // 新增一行
            row.set(0, jobject.get("自然村")); // 根据列顺序填入数值
            row.set(1, jobject.get("备注"));
            row.set(2, jobject.get("详细地址"));
            row.set(3, jobject.get("检查情况")); // 根据列顺序填入数值
            row.set(4, jobject.get("整改反馈"));
            row.set(5, "");
            row.set(6, "");
            row.set(7, jobject.get("排序号"));
            dataTable.getRows().add(row); // 加入此行数据
        }
        doc.getMailMerge().executeWithRegions(dataTable);
        docBuilder = new DocumentBuilder(doc);

        for (int i=0;i<sortedJsonArray.size();i++){
            JSONObject jobject=sortedJsonArray.getJSONObject(i).getJSONObject("dataFieldMap");
            if(jobject.get("检查图片")!=null){
                String[] tmp=jobject.get("检查图片").toString().split("/");
                String filePath="/data1/upload"+tmp[1]+"/"+tmp[2];
                File file = new File(filePath+"/"+tmp[3]);
                if(file.exists()){
                    docBuilder.moveToCell(i,3,0,0);
                    Shape img1 = docBuilder.insertImage(filePath+"/"+tmp[3],
                            RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                            1, 100, 125, WrapType.SQUARE);
                    img1.setWidth(370);
                    img1.setHeight(300);
                    img1.setHorizontalAlignment(HorizontalAlignment.CENTER);
                }
            }

            if(jobject.get("整改图片")!=null){
                String[] tmp=jobject.get("整改图片").toString().split("/");
                String filePath="/data1/upload"+tmp[1]+"/"+tmp[2];
                File file = new File(filePath+"/"+tmp[3]);
                if(file.exists()) {
                    docBuilder.moveToCell(i,3,1,0);
                    Shape img2 = docBuilder.insertImage(filePath+"/"+tmp[3],
                            RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                            1, 100, 125, WrapType.SQUARE);
                    img2.setWidth(370);
                    img2.setHeight(300);
                    img2.setHorizontalAlignment(HorizontalAlignment.CENTER);
                }
            }

        }
        response.setHeader("Content-Disposition", "attachment; filename=1234.docx");
        response.setContentType("application/octet-stream;charset=UTF-8");

        OutputStream output = response.getOutputStream();
        doc.save(output, SaveFormat.DOC);

        output.flush();
        output.close();

    }



    public void downloadnotmod(HttpServletResponse response) throws Exception {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return ;
        }
        docBuilder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
        docBuilder.getFont().setColor(java.awt.Color.RED);
        docBuilder.writeln("联丰社区人居环境台账");
        docBuilder.writeln("");
        Table table = docBuilder.startTable();     //开始画Table
        docBuilder.getRowFormat().setHeight(25);
        docBuilder.insertCell();
        docBuilder.getCellFormat().setVerticalAlignment(ParagraphAlignment.LEFT);
        docBuilder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
        docBuilder.write("1");
        docBuilder.endRow();
        docBuilder.insertCell();
        docBuilder.write("类别：评价内容");
        docBuilder.endRow();
        docBuilder.insertCell();
        Shape img = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("1.jpg")
                , RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                1, 100, 125, WrapType.SQUARE);
        img.setWidth(100);
        img.setHeight(30);
        img.setHorizontalAlignment(HorizontalAlignment.CENTER);
        docBuilder.insertCell();
        Shape img1 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("2.jpg")
                , RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                1, 100, 125, WrapType.SQUARE);
        img1.setWidth(100);
        img1.setHeight(30);
        img1.setHorizontalAlignment(HorizontalAlignment.CENTER);
        docBuilder.endRow();
        docBuilder.insertCell();
        docBuilder.write("检查地址：检查地址2 ");
        docBuilder.endRow();
        docBuilder.insertCell();
        docBuilder.write("整改前");
        docBuilder.insertCell();
        docBuilder.write("整改后");
        docBuilder.endRow();
        docBuilder.insertCell();
        docBuilder.write("检查情况：检查情况2");
        docBuilder.insertCell();
        docBuilder.write("整改情况：整改情况2");
        docBuilder.endRow();
        docBuilder.endTable();

        response.setHeader("Content-Disposition", "attachment; filename=1234.docx");
        response.setContentType("application/octet-stream;charset=UTF-8");

        OutputStream output = response.getOutputStream();
        doc.save(output, SaveFormat.DOC);

        output.flush();
        output.close();

    }


    /// <summary>
    /// 插入表格
    /// </summary>
    /// <param name="data"></param>
    /// <param name="haveBorder"></param>
    /// <returns></returns>
    public boolean InsertTable(DataTable data, boolean haveBorder) throws Exception {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return false;
        }
        docBuilder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
        docBuilder.getFont().setColor(java.awt.Color.RED);
        docBuilder.writeln("联丰社区人居环境台账");
        docBuilder.writeln("");
        Table table = docBuilder.startTable();     //开始画Table

//        ParagraphAlignment paragraphAlignmentValue = docBuilder.ParagraphFormat.Alignment;
        docBuilder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);

        //这里需要for循环map
        for (Map<String,String> map :datas){
            docBuilder.getRowFormat().setHeight(25);


            for (String key :map.keySet()){
                docBuilder.insertCell();
                if("img".equals(key)){
                    //插入一张图片
//                    InsertBookMark("BarCode");
//                    GotoBookMark("BarCode");
                    Shape img = docBuilder.insertImage(map.get(key), RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                            1, 100, 125, WrapType.SQUARE);
                    img.setWidth(100);
                    img.setHeight(30);
                    img.setHorizontalAlignment(HorizontalAlignment.CENTER);
                }else {
                    docBuilder.getFont().setSize(10.5);
                    docBuilder.getFont().setName("宋体");
                    docBuilder.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);//垂直居中对齐
                    docBuilder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT); //水平居中对齐
                    docBuilder.getCellFormat().setWidth(100.0);
                    docBuilder.getCellFormat().setPreferredWidth(PreferredWidth.fromPoints(50));
                    if (haveBorder)
                    {
                        //设置外框样式
                        docBuilder.getCellFormat().getBorders().setLineStyle(LineStyle.SINGLE);
                        //样式设置结束
                    }

                    docBuilder.write(map.get(key));
                }
            }
            docBuilder.endRow();
        }

//        MergeCell();          //合并单元格
        docBuilder.endTable();
        //TODO:插入图片
//        InsertImage();
//        docBuilder.ParagraphFormat.Alignment = paragraphAlignmentValue;
//        table.Alignment = Aspose.Words.Tables.TableAlignment.Center;
//        table.PreferredWidth = Aspose.Words.Tables.PreferredWidth.Auto;


        return true;
    }

    //设置书签
    public void SetHeade(String strBookmarkName, String text) throws Exception {
        if (doc.getRange().getBookmarks().get(strBookmarkName) != null)
        {
            Bookmark mark = doc.getRange().getBookmarks().get(strBookmarkName);
            mark.setText(text);
        }
    }
    //插入书签
    public void InsertBookMark(String BookMark) throws Exception {
        docBuilder.startBookmark(BookMark);
        docBuilder.endBookmark(BookMark);

    }
    //到哪个书签
    public void GotoBookMark(String strBookMarkName) throws Exception {
        docBuilder.moveToBookmark(strBookMarkName);
    }
    //清除书签
    public void ClearBookMark() throws Exception {
        doc.getRange().getBookmarks().clear();
    }

    /// <summary>
    /// 合并单元格
    /// </summary>
    public void MergeCell() throws Exception {
        //21行4列
        for (int i = 0; i < 21; i++)
        {
            if (i == 0 || i == 2 || i == 6 || i == 11 || i == 15)      //单元格合并成一格
            {
                for (int j = 1; j < 4; j++)
                {
                    docBuilder.moveToCell(0, i, j, 0);
                    docBuilder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
                }
            }
            else if (i == 5 || i == 10 || i == 14 || i >= 17)      //单元格合并成两格
            {
                for (int j = 2; j < 4; j++)
                {
                    docBuilder.moveToCell(0, i, j, 0);
                    docBuilder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
                }
            }
        }

    }
    //插入图片
    public void InsertImage() throws Exception {
        docBuilder.moveToCell(0, 19, 1, 0);     //移动到第20行第2列插入图片
        InsertBookMark("BarCode");
        GotoBookMark("BarCode");
        Shape img = docBuilder.insertImage("D:\\p1.png", RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                1, 100, 125, WrapType.SQUARE);
        img.setWidth(100);
        img.setHeight(30);
        img.setHorizontalAlignment(HorizontalAlignment.CENTER);
    }


}
