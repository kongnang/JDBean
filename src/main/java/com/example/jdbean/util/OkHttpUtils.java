package com.example.jdbean.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiu
 * @create 2022-10-22 16:28
 */
@Slf4j
public class OkHttpUtils {

    private static final String SEND_KEY = "SCT177921TBS56jkOpfjngwQ3uhWdDSawG";

    private static final String USER_AGENT = "okhttp/3.12.1;jdmall;android;version/10.3.4;build/92451;";

    public static String post(String url, String cookie, RequestBody requestBody, Map<String, String> header) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(Headers.of(header))
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("post请求, result: {}", result);
        return result;
    }

    public static String get(String url, String cookie, Map<String, String> header) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(header))
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("get请求, result: {}", result);
        return result;
    }

    public static String get(String url) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("get请求, result: {}", result);
        return result;
    }

    public static String weChatPost(String title, String desp) throws IOException {
        String url = "https://sctapi.ftqq.com/" + SEND_KEY + ".send?title=" + title + "&desp=" + desp;


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("微信推送get请求, result: {}", result);
        return result;
    }
}
