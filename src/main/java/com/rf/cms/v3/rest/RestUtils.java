package com.rf.cms.v3.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * com.rf.cms.v3.rest.RestUtils
 * 工具类（仅供程序内部使用的工具类）
 */
@Component("RestUtils")
public class RestUtils {
    @Autowired
    public RestTemplate restTemplate;
    @Value("${attachmenturl}")
    public String attachmenturl;
    @Value("${sso.web.url}")
    public String ssoWebUrl;
    @Value("${sso.web.urljk}")
    public String ssoWebUrlJk;
    @Value("${sso.web.token}")
    public String ssoWebToken;
    @Value("${paasid}")
    public String paasid;
    @Value("${paastoken}")
    public String paastoken;
    @Value("${apiurl}")
    public String apiurl;
    @Value("${agentid}")
    public String agentid;

    public static final String NUMBERCHAR = "0123456789";

    //添加CI
    /**
     * 功能描述:向数据库添加表格数据函数
     *
     * @param clsName  String 1 表格名称
     * @param addData  Map 2 表格字段数据
     * @return  CMSResponse
     */
    public CMSResponse addCi(String clsName, Map<String, Object> addData) {
        CMSResponse response = getClsByName(clsName);
        if (response.getContent() == null) return response;
        Map<String, Object> content = (Map<String, Object>) response.getContent();
        String id = (String) content.get("id");

        String url = "/cms/v1/ci/add?domain=egfbank";
        Map body = new HashMap();
        body.put("dataFieldMap", addData);
        body.put("ciClassID", id);
        body.put("source", "外部接口");
        return postForObject(url, body);
    }

    //更新CI

    /**
     * 功能描述:更新数据库里面数据
     *
     * @param clsName  String 1 表格名称
     * @param queryData  Map 2 根据条件查询表格里面的数据
     * @param updateData  Map 3 表格里面需要更新的数据
     * @return  CMSResponse
     */
    public CMSResponse updateCi(String clsName, Map<String, Object> queryData, Map<String, Object> updateData) {
        CMSResponse response = getCiByAttr(clsName, queryData);
        if (response.getContent() == null) return response;
        Map<String, Object> content = (Map<String, Object>) response.getContent();
        ArrayList<HashMap> results = (ArrayList<HashMap>) content.get("results");
        if (results.isEmpty()) return response;

        String url = "/cms/v1/ci/update?domain=egfbank";
        Map body = new HashMap();
        body.put("dataFieldMap", updateData);
        body.put("id", results.get(0).get("id"));
        body.put("ciClassID", results.get(0).get("ciClassID"));
        return postForObject(url, body);
    }

    //删除CI
    /**
     * 功能描述:删除数据库中表格数据
     *
     * @param id String 1 表格的id
     * @return  CMSResponse
     */
    public CMSResponse delCi(String id) {
        List<String> list = new ArrayList<String>();
        list.add(id);
        String url = "/cms/v1/ci/del";
        Object[] ids = list.toArray();
        return postForObject(url, ids);
    }

    //按CI的属性和值查询CI数据接口
    //输入CI类名、要查询的CI属性键值对参数，支持模糊查询，分页返回CI数据。
    /**
     * 功能描述:根据条件向数据库中查询数据
     *
     * @param clsName String 1   表格名称
     * @param queryData Map 2   查询条件
     * @param pageNo String 3   第几页
     * @param pageSize String 4  页面数据数量
     * @return  CMSResponse
     */
    public CMSResponse getCiByAttr(String clsName, Map<String, Object> queryData, String pageNo, String pageSize) {
        String url = "/cms/v1/ts/ci/byAttributes";
        Map body = new HashMap();
        body.put("attributes", convertData(queryData));
        body.put("clsName", clsName);
        body.put("pageNo", pageNo);
        body.put("pageSize", pageSize);
        return postForObject(url, body);
    }


    /**
     * 功能描述:根据条件向数据库中查询数据
     *
     * @param clsName String 1   表格名称
     * @param queryData Map 2   查询条件
     * @param pageNo String 3   第几页
     * @param pageSize String 4  页面数据数量
     * @param sortFields JSONArray 5  排序的条件
     * @return  CMSResponse
     */
    public CMSResponse getCiByAttr(String clsName, Map<String, Object> queryData, String pageNo, String pageSize, JSONArray sortFields) {
        String url = "/cms/v1/ts/ci/byAttributes";
        Map body = new HashMap();
        body.put("attributes", convertData(queryData));
        body.put("clsName", clsName);
        body.put("pageNo", pageNo);
        body.put("pageSize", pageSize);
        body.put("sortFields", sortFields);
        return postForObject(url, body);
    }

    //按CI的属性和值查询CI数据接口
    //输入CI类名、要查询的CI属性键值对参数，支持模糊查询，分页返回CI数据。
    /**
     * 功能描述:根据条件向数据库中查询数据
     *
     * @param clsName String 1   表格名称
     * @param queryData Map 2   查询条件
     * @return  CMSResponse
     */
    public CMSResponse getCiByAttr(String clsName, Map<String, Object> queryData) {
        String url = "/cms/v1/ts/ci/byAttributes";
        Map body = new HashMap();
        body.put("attributes", convertData(queryData));
        body.put("clsName", clsName);
        body.put("pageNo", "0");
        body.put("pageSize", "10000");
//        System.out.println(JSON.toJSONString(body));
        return postForObject(url, body);
    }
    /**
     * 功能描述:根据条件向数据库中查询数据
     *
     * @param clsName String 1   表格名称
     * @param queryData Map 2   查询条件
     * @param sortFields JSONArray 3  排序的条件
     * @return  CMSResponse
     */
    public CMSResponse getCiByAttr(String clsName, Map<String, Object> queryData, JSONArray sortFields) {
        String url = "/cms/v1/ts/ci/byAttributes";
        Map body = new HashMap();
        body.put("attributes", convertData(queryData));
        body.put("clsName", clsName);
        body.put("pageNo", "0");
        body.put("pageSize", "10000");
        body.put("sortFields", sortFields);
        return postForObject(url, body);
    }

    //根据名称获得CI类接口
    //根据CI类的名称查询对应的CI类信息。
    /**
     * 功能描述:根据表格名称查询出表格信息
     *
     * @param clsName  String 1  表格名称
     * @return : CMSResponse
     */
    public CMSResponse getClsByName(String clsName) {
        String url = "/cms/v1/ts/ciclass/getByName/" + clsName + "?domain={domain}";
        return getForObject(url, "egfbank");
    }

    /**
     * 功能描述:根据id查询表格中数据
     *
     * @param id  String 1 数据id
     * @return : CMSResponse
     */
    public CMSResponse getCiByID(String id) {
        String url = "/cms/v1/ts/ci/getByID/" + id;
        return getForObject(url, "egfbank");
    }

    /**
     * 功能描述:根据id集合查询表格中数据
     *
     * @param ids Object[] 1 id集合
     * @return : CMSResponse
     */
    public CMSResponse getCiByIDs(Object[] ids) {
        String url = "/cms/v1/ts/ci/getByIDs/";
        return postForObject(url, ids);
    }

    /**
     * 功能描述:根据id查询出人员信息
     *
     * @param id  String 1
     * @return : CMSResponse
     */
    public CMSResponse getAccountByID(String id) {
        String url = "cms/v1/sys/account/get/" + id;
        return getForObject(url, id);
    }

    /**
     * 功能描述:
     *
     * @param ScirptCode  String 1
     * @param args  Map 2
     * @return : CMSResponse
     */
    public CMSResponse execScript(String ScirptCode, Map<String, Object> args) {
        String url = "/cms/v1/module/business_script/" + ScirptCode + "/run";
        return postForObject(url, args);
    }

    /**
     * 功能描述:get方式请求数据的函数
     *
     * @param url String 1  请求的地址
     * @param uriVariables Object 2
     * @return : CMSResponse
     */
    public CMSResponse getForObject(String url, Object... uriVariables) {
        return exchange(ssoWebUrlJk + url, HttpMethod.GET, ssoWebToken, null, uriVariables);
    }

    /**
     * 功能描述:post方式请求数据的函数
     *
     * @param url String 1 请求的地址
     * @param body Map 2 请求的参数
     * @param uriVariables Object 3
     * @return : CMSResponse
     */
    public CMSResponse postForObject(String url, Map body, Object... uriVariables) {
        return postForObject(ssoWebUrlJk + url, ssoWebToken, body, uriVariables);
    }
    /**
     * 功能描述:post方式请求数据的函数
     *
     * @param url String 1 请求的地址
     * @param body Object[] 2 请求的参数
     * @param uriVariables Object 3
     * @return : CMSResponse
     */
    public CMSResponse postForObject(String url, Object[] body, Object... uriVariables) {
        return postForObject(ssoWebUrlJk + url, ssoWebToken, body, uriVariables);
    }
    /**
     * 功能描述:post方式请求数据的函数
     *
     * @param url String 1 请求的地址
     * @param body String 2 请求的参数
     * @param uriVariables Object 3
     * @return : CMSResponse
     */
    public CMSResponse postForObject(String url, String body, Object... uriVariables) {
        return postForObject(ssoWebUrlJk + url, ssoWebToken, body, uriVariables);
    }
    /**
     * 功能描述:post方式请求数据的函数
     *
     * @param url String 1 请求的地址
     * @param token String 2 请求的令牌
     * @param body String 3 请求的参数
     * @param uriVariables Object 4
     * @return : CMSResponse
     */
    public CMSResponse postForObject(String url, String token, Map body, Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json;charset=UTF-8");
        headers.add("access-token", token);
        HttpEntity<Map> request = new HttpEntity<Map>(body, headers);
        return restTemplate.postForObject(url, request, CMSResponse.class, uriVariables);
    }
    /**
     * 功能描述:post方式请求数据的函数
     *
     * @param url String 1 请求的地址
     * @param token String 2 请求的令牌
     * @param body String 3 请求的参数
     * @param uriVariables Object 4
     * @return : CMSResponse
     */
    public CMSResponse postForObject(String url, String token, Object[] body, Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json;charset=UTF-8");
        headers.add("access-token", token);
        HttpEntity<Object[]> request = new HttpEntity<Object[]>(body, headers);
        return restTemplate.postForObject(url, request, CMSResponse.class, uriVariables);
    }
    /**
     * 功能描述:通过url请求数据的函数
     *
     * @param url String 1 请求的地址
     * @param method HttpMethod 2 请求的数据的方式
     * @param token String 3 请求的令牌
     * @param body String 4 请求的参数
     * @param uriVariables Object 5
     * @return : CMSResponse
     */
    public CMSResponse exchange(String url, HttpMethod method, String token, Map body, Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json;charset=UTF-8");
        headers.add("access-token", token);
        HttpEntity<Map> request = new HttpEntity<Map>(body, headers);
        return restTemplate.exchange(url, method, request, CMSResponse.class, uriVariables).getBody();
    }

    /**
     * 功能描述:返回文件地址的函数
     *
     * @param fileName String 1  文件名
     * @param fileAddress String 2  文件地址
     * @return : String
     */
    public String getFileAddress(String fileName, String fileAddress) {
        return ssoWebUrl + attachmenturl + "/" + fileName + "?uri=" + fileAddress + "&access-token=" + ssoWebToken;
//        + "/" + fileName
//        + "&access-token=" + ssoWebToken
//        return ssoWebUrl + attachmenturl + "?uri=" + fileAddress ;
    }

    /**
     * 功能描述:转换请求参数的函数（仅程序内部使用）
     *
     * @param queryData Map 1
     * @return : ArrayList
     */
    public ArrayList<HashMap> convertData(Map<String, Object> queryData) {
        ArrayList list = new ArrayList();
        for (String name : queryData.keySet()) {
            HashMap map = new HashMap();
            if (!name.contains("operator")) {
                map.put("name", name);
                map.put("value", queryData.get(name));
                //为支持模糊查询而编写
                //operator  状态值，标示这个是的比较方式 -1:不等于，0:等于，
                // 1:大于，2:大于等于，3:小于，4:小于等于，5:以,|;分割，做包含处理，
                // "6:以,分割做不包含处理，7:做正则匹配，默认正则匹配
                if (queryData.get(name + "operator") != null) {
                    map.put("operator", queryData.get(name + "operator").toString());
//                    queryData.remove(name + "operator");
                }

                list.add(map);
            }
        }
        return list;
    }

    /**
     * 功能描述:传入的字符串为空返回"",如果不是则返回字符串的函数
     *
     * @param str  String 1
     * @return : String
     */
    public String SetNullEmptyString(String str) {
        if (str == null || str.trim().toLowerCase().equals("null") || str.trim().equals("")) {
            return "";
        }
        return str;
    }






    /**
     * 功能描述:通过post方法请求外部url数据的方法
     *
     * @param postUrl  String 1 url地址
     * @param postRequest  String 1 请求参数
     * @param map  Map 1 请求头部参数
     * @return :String
     */
    public String getPageContent(String postUrl, String postRequest, Map<String, String> map) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL newUrl = new URL(postUrl);

            HttpURLConnection hConnection = (HttpURLConnection) newUrl.openConnection();
            if (postRequest.length() > 0) {
                hConnection.setDoOutput(true);
                hConnection.setDoInput(true);
                hConnection.setRequestMethod("POST");
//                hConnection.setRequestMethod("GET");
                for (String key : map.keySet()) {
                    hConnection.setRequestProperty(key, map.get(key));
                }

                OutputStreamWriter out = new OutputStreamWriter(hConnection.getOutputStream());
                System.out.println(postRequest);
                out.write(postRequest);
                out.flush();
                out.close();
            }
            System.out.println("开始请求：" + postUrl);
            System.out.println("请求参数：" + postRequest);
            InputStream input = hConnection.getInputStream();
            System.out.println("请求结束：" + postUrl);
            InputStreamReader in = new InputStreamReader(input, "utf-8");
            BufferedReader rd = new BufferedReader(in);
            int ch;
            for (int length = 0; (ch = rd.read()) > -1; length++)
                buffer.append((char) ch);
            String s = buffer.toString();
            s.replaceAll("//&[a-zA-Z]{1,10};", "").replace("<[^>]>", "");
            System.out.println("请求获得：" + s);
            rd.close();
            hConnection.disconnect();
            System.out.println("返回的数据：" + buffer.toString().trim());
            System.out.println("--------------请求结束----------------");
            return buffer.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求错误返回：" + e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * 功能描述:获取随机数的函数
     *
     * @param length int 1 随机数的长度
     * @return : String
     */
    public String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 功能描述：将byte转为16进制
     *
     * @param bytes byte[]
     * @return String
     */
    public String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 功能描述:将string类型的时间格式化成yyyy/MM/dd形式的字符串
     *
     * @param stamp  String 1 字符串形式时间
     * @return : String
     */
    public String stampToDate(String stamp) {

        String res = "";
        if (stamp != null && !stamp.equals("null") && !stamp.equals("")) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            long lt = new Long(stamp);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        }
        return res;
    }

    /**
     * 功能描述:将string类型的时间格式化成yyyy/MM/dd形式的字符串
     *
     * @param stamp  String 1 字符串形式时间
     * @return : String
     */
    public String LongstampToDate(String stamp) {

        String res = "";
        if (stamp != null && !stamp.equals("null") && !stamp.equals("")) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            long lt = new Long(stamp);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        }
        return res;
    }
    /**
     * 功能描述:将string类型的时间格式化成yyyy-MM-dd形式的字符串
     *
     * @param stamp  String 1 字符串形式时间
     * @return : String
     */
    public String stringToDate(String stamp) {

        String res = "";
        if (stamp != null && !stamp.equals("null") && !stamp.equals("")) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = new Long(stamp);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        }
        return res;
    }

    /**
     * 功能描述:将string类型的时间格式化成yyyy-MM-dd HH:mm:ss形式的字符串
     *
     * @param stamp  String 1 字符串形式时间
     * @return : String
     */
    public String stampToLongDate(String stamp) {
        String res = "";
        if (stamp != null && !stamp.equals("null") && !stamp.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(stamp);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        }
        return res;
    }
    /**
     * 功能描述:判断今天昨天还是前几天的函数，并返回相应的数据
     *
     * @param stamp  String 1 字符串形式时间
     * @return : String
     * @throws ParseException 为空返回异常
     */
    public String getyesterdayoldTime(String stamp) throws ParseException {
        String res = "";
        if (stamp != null && !stamp.equals("null") && !stamp.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(stamp);
            Date date = new Date(lt);
            //判断是今天还是昨天还是前几天
            int flag = isYeaterday(date, null);
            if (flag == 0) {
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                //昨天
                res = "昨天" + simpleDateFormat.format(date);
            } else if (flag == -1) {
                //今天
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                res = simpleDateFormat.format(date);
            } else {
                //前几天
                res = simpleDateFormat.format(date);
            }

        }
        return res;
    }

    /**
     * 功能描述:判断两个时间是否是相差一天
     *
     * @param oldTime Date 1
     * @param newTime Date 2
     * @return : int 0代表同一天，1代表至少是前台，-1代表是昨天
     */
    private int isYeaterday(Date oldTime, Date newTime) throws ParseException {
        if (newTime == null) {
            newTime = new Date();
        }
        //将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);
        //昨天 86400000=24*60*60*1000 一天
        if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
            return 0;
        } else if ((today.getTime() - oldTime.getTime()) <= 0) { //至少是今天
            return -1;
        } else { //至少是前天
            return 1;
        }

    }

    /**
     * 功能描述:将long类型的时间类型的数据转换成yyyy-MM-dd HH:mm:ss字符串形式数据
     *
     * @param stamp  long 1 long类型的时间数据
     * @return : String
     */
    public String LongtoStringDate(long stamp) {
        String res = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp);
        res = simpleDateFormat.format(date);
        return res;
    }
    /**
     * 功能描述:将字符串形式的时间类型数据转换成long类型数据
     *
     * @param StrDate  String 1 String类型的时间数据
     * @return : long
     */
    public long strDateToStamp(String StrDate) {
        if (StrDate == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            try {
                date = sdf.parse(StrDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date.getTime();
        }
        return 0;
    }
    /**
     * 功能描述:将字符串形式的时间类型数据转换成long类型数据
     *
     * @param StrDate  String 1 String类型的时间数据
     * @return : long
     */
    public long strlongDateToLong(String StrDate) {
        if (!SetNullEmptyString(StrDate).equals("")) {
            Date date = new Date(Long.parseLong(StrDate));

            return date.getTime();
        }
        return 0;
    }
    /**
     * 功能描述:将字符串形式的时间类型数据转换成long类型数据
     *
     * @param StrDate  String 1 String类型的时间数据
     * @return : long
     */
    public long strDateToLong(String StrDate) {
        if (StrDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(getDateStr(StrDate));
                System.out.println(sdf.parse(StrDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date.getTime();
        }
        return 0;
    }
    /**
     * 功能描述:将字符串形式的时间类型数据转换成long类型数据
     *
     * @param StrDate  String 1 String类型的时间数据
     * @return : long
     */
    public long strDateToLongTime(String StrDate) {
        if (StrDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(StrDate);
                System.out.println(sdf.parse(StrDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date.getTime();
        }
        return 0;
    }

    /**
     * 功能描述:将字符串形式的时间类型数据格式化成标准的字符串时间类型数据
     *
     * @param strdate String 1
     * @return : String
     */
    private String getDateStr(String strdate) {
        String[] a = strdate.split("/");

        if (a.length < 3) {
            a = strdate.split("-");
        }
        if (a[1].length() < 2) {
            if (new Integer(a[1]) < 10) {
                a[1] = "0" + a[1];
            }
        }

        if (a[2].length() < 2) {
            if (new Integer(a[2]) < 10) {
                a[2] = "0" + a[2];
            }
        }

        return StringUtils.join(a, "-");
    }

    public String ApiPostdcdb(String postPath, String postRequest, String SendUserID) {
        String postUrl = postPath;
        String headerTimestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String secret = paastoken;
        String headerNonce = generateString(16);
        System.out.println(headerTimestamp);

        String signature = (headerTimestamp + secret + headerNonce + headerTimestamp);
        MessageDigest messageDigest;
        String encodeStr = "";
        String PostUser = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(signature.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
            System.out.println(encodeStr.toUpperCase());
            //PostUser= httpURLConnectionPOST(encodeStr);
            //System.out.println(PostUser);

            Map<String, String> map = new HashMap<String, String>();
            map.put("Content-type", "application/json");
            map.put("x-tif-paasid", paasid);
            map.put("x-tif-signature", encodeStr.toUpperCase());
            map.put("x-tif-timestamp", headerTimestamp);
            map.put("x-tif-nonce", headerNonce);
//            map.put("x-tif-uid", SendUserID);//与督办交互使用,发送人id
            System.out.println("x-tif-paasid：" + paasid);
            System.out.println("x-tif-signature：" + encodeStr.toUpperCase());
            System.out.println("x-tif-timestamp：" + headerTimestamp);
            System.out.println("x-tif-nonce：" + headerNonce);
//            System.out.println("x-tif-uid：" + SendUserID);

            PostUser = getPageContent(postUrl, postRequest, map);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return PostUser;
    }

    public String ApiPost(String postPath, String postRequest) {
        String postUrl = postPath;
        String headerTimestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String secret = paastoken;
        System.out.println("paastoken:"+paastoken);
        System.out.println("agentid:"+agentid);
        String headerNonce = generateString(16);
        System.out.println(headerTimestamp);

        String signature = (headerTimestamp + secret + headerNonce + headerTimestamp);
        MessageDigest messageDigest;
        String encodeStr = "";
        String PostUser = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(signature.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
            System.out.println(encodeStr.toUpperCase());
            //PostUser= httpURLConnectionPOST(encodeStr);
            //System.out.println(PostUser);

            Map<String, String> map = new HashMap<String, String>();
            map.put("Content-type", "application/json");
            map.put("x-tif-paasid", paasid);
            map.put("x-tif-signature", encodeStr.toUpperCase());
            map.put("x-tif-timestamp", headerTimestamp);
            map.put("x-tif-nonce", headerNonce);
            System.out.println("x-tif-paasid：" + paasid);
            System.out.println("x-tif-signature：" + encodeStr.toUpperCase());
            System.out.println("x-tif-timestamp：" + headerTimestamp);
            System.out.println("x-tif-nonce：" + headerNonce);

            PostUser = getPageContent(postUrl, postRequest, map);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return PostUser;
    }

    //JSONArray中的数字排序

    /**
     * 功能描述:根据数组里面某一个键值进行排序的函数
     *
     * @param jsonArr JSONArray 1  传入的数组
     * @param KEY_NAME String 1  键值
     * @return : String
     */
    public String jsonArraySort(JSONArray jsonArr, String KEY_NAME) {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.size(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject a, JSONObject b) {
                long valA = Long.parseLong(a.getString(KEY_NAME));
                long valB = Long.parseLong(b.getString(KEY_NAME));
                //return valA.compareTo(valB);
                if (valA >= valB) {
                    return -1;
                }
                return 1;
            }
        });
        for (int i = 0; i < jsonArr.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        return sortedJsonArray.toString();
    }

    //JSONArray中的数字排序
    /**
     * 功能描述:根据数组里面某一个键值进行排序的函数
     *
     * @param jsonArr JSONArray 1  传入的数组
     * @param KEY_NAME String 1  键值
     * @return : JSONArray
     */
    public JSONArray ArraySort(JSONArray jsonArr, String KEY_NAME) {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.size(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject a, JSONObject b) {
                long valA = Long.parseLong(a.getString(KEY_NAME));
                long valB = Long.parseLong(b.getString(KEY_NAME));
                //return valA.compareTo(valB);
                if (valA >= valB) {
                    return -1;
                }
                return 1;
            }
        });
        for (int i = 0; i < jsonArr.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        return sortedJsonArray;
    }





}
