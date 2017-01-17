package com.ymatou.product.infrastructure;/*
 *
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * All rights reserved.
 *
 */

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    // 超时时间豪秒
    public static Integer CONN_TIME_OUT = 3000;
    public static Integer SOCKET_TIME_OUT = 10000;
    public static Integer DEFAULT_MAX_PER_ROUTE = 40;
    public static Integer MAX_TOTAL = 400;

    public static RequestConfig requestConfig;
    public static HttpClient httpClient;

    static {
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONN_TIME_OUT)
                .setSocketTimeout(SOCKET_TIME_OUT).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(MAX_TOTAL);

        httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
    }


    public static String requestGet(String url, Map<String, String> paramsMap) throws Exception {
        logger.info("GET request  url:{} params:{}", url, paramsMap);

        Long start = System.currentTimeMillis();

        List<NameValuePair> params = initParams(url, paramsMap);
        // Get请求
        HttpGet httpGet = new HttpGet(url);

        try {
            // 设置参数
            String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + str));
            // 发送请求
            HttpResponse response = httpClient.execute(httpGet);
            logger.info("GET request  url:{} response:{} time:{}",
                    url, response, System.currentTimeMillis() - start);
            // 获取返回数据
            String retStr = getSuccessRetFromResp(response, url, JSON.toJSONString(paramsMap));

            return retStr;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public static String requestPost(String url, Map<String, String> paramsMap) throws Exception {
        logger.info("POST request  url:{} params:{}", url, paramsMap);
        Long start = System.currentTimeMillis();

        List<NameValuePair> params = initParams(url, paramsMap);

        HttpPost httpPost = new HttpPost(url);

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));

            HttpResponse response = httpClient.execute(httpPost);

            logger.info("POST request  url:{} response:{}  time:{}",
                    url, response, System.currentTimeMillis() - start);

            String retStr = getSuccessRetFromResp(response, url, JSON.toJSONString(paramsMap));


            return retStr;
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * post json 格式数据
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String requestPostJsonStr(String url, String params) throws Exception {

        logger.info("POST request  url:{} params:{}", url, params);

        Long start = System.currentTimeMillis();

        HttpPost httpPost = new HttpPost(url);

        try {
            StringEntity entity = new StringEntity(params, Consts.UTF_8);
            entity.setContentType(MediaType.APPLICATION_JSON);

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            logger.info("POST request  url:{} response:{}  time:{}",
                    url, response, System.currentTimeMillis() - start);

            String retStr = getSuccessRetFromResp(response, url, params);

            return retStr;
        } finally {
            httpPost.releaseConnection();
        }

    }

    /**
     * post json 格式数据
     * 
     * @param url
     * @param obj
     * @return
     * @throws Exception
     */
    public static String requestPostJson(String url, Object obj) throws Exception {
        String params = JSON.toJSONString(obj);
        return requestPostJsonStr(url, params);
    }

    public static String requestPostDotNetJson(String url, Object obj) throws Exception {
        String params =  Utils.toDotNetCompatibleJson(obj);
        return requestPostJsonStr(url, params);
    }

    private static String getSuccessRetFromResp(HttpResponse response, String url, String params) throws Exception {
        String retStr = "";
        // 检验状态码，如果成功接收数据
        int code = response.getStatusLine().getStatusCode();

        if (code == 200) {
            retStr = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } else {
            throw new RuntimeException(String.format("Http request error:%s, url:%s, params:%s", response, url, params));
        }

        logger.info("Http request retStr:{}. url:{}", retStr, url);
        return retStr;
    }



    private static List<NameValuePair> initParams(String url, Map<String, String> paramsMap) {

        List<NameValuePair> params = new ArrayList<>();
        if (paramsMap == null)
            return params;
        Iterator<String> iterator = paramsMap.keySet().iterator();


        while (iterator.hasNext()) {
            String key = iterator.next();
            params.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }
        return params;
    }

}

