package com.rf.cms.v3.Utils;

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
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * com.rf.cms.v3.Utils 于2020/8/20 由Administrator 创建 .
 */
public class AposeUtils {
    private Document doc;
    private DocumentBuilder docBuilder;
    private java.util.List<Map<String,String>> datas=new ArrayList<Map<String,String>>();

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
        System.out.println("文件路径:"+AposeUtils.class.getClassLoader().getResource("mod.docx").getPath());
        InputStream inputStream=AposeUtils.class.getClassLoader().getResourceAsStream("mod.docx");
        doc =  new Document(inputStream);
//        docBuilder = new DocumentBuilder(doc);
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
        dataTable.getColumns().add("评估内容"); // 1
        dataTable.getColumns().add("检查地址"); // 2
        dataTable.getColumns().add("检查情况"); // 2
        dataTable.getColumns().add("整改情况"); // 2
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
        docBuilder.moveToCell(0,2,0,0);
        Shape img1 = docBuilder.insertImage(AposeUtils.class.getClassLoader().getResourceAsStream("1.jpg"),
                RelativeHorizontalPosition.MARGIN, 1, RelativeVerticalPosition.MARGIN,
                1, 100, 125, WrapType.SQUARE);
        img1.setWidth(100);
        img1.setHeight(30);
        img1.setHorizontalAlignment(HorizontalAlignment.CENTER);
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
