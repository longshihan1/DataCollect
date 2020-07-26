package com.longshihan.collect.http;

import com.google.gson.Gson;
import com.longshihan.collect.model.APP;
import com.longshihan.collect.model.SaveTrace;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * @author longshihan
 * @time 2020/7/26
 */
public class HttpUtils {
    static String testConnectUrl = "http://192.168.0.105:8080/testConnect";
    static String saveDataUrl = "http://192.168.0.105:8080/saveAppInfo";
    public static APP testConnect() {
        try {
            URL url = new URL(testConnectUrl);
            URLConnection connection = url.openConnection();
            //使用输入流
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();
            APP bean = gson.fromJson(sb.toString(), APP.class);
            sb.delete(0, sb.length());
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return new APP(e.getMessage());
        }
    }

    public static APP saveTraceInfo(SaveTrace saveTrace) {
        try {
            Gson gson = new Gson();
            String json=gson.toJson(saveTrace);
            URL url = new URL(saveDataUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            // POST请求
            DataOutputStream out = new
                    DataOutputStream(connection.getOutputStream());
            out.writeBytes(json);
            out.flush();
            out.close();
            // 读取响应
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = URLDecoder.decode(lines, "utf-8");
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            APP bean = gson.fromJson(sb.toString(), APP.class);
            sb.delete(0, sb.length());
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return new APP(e.getMessage());
        }
    }
}
