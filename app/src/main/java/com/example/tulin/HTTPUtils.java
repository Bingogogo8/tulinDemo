package com.example.tulin;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;

import javax.xml.transform.Result;

public class HTTPUtils {


    /*发送消息到服务器
      return：消息对象
     */
    public static ChatMessage sendMessage(String message) {

  // 请求json，里面包含reqType，perception，userInfo
        ChatMessage chatMessage = new ChatMessage();
        String gsonResult = doGet(message);
        if (gsonResult != null) {
            try {
                JSONObject jsonObject = new JSONObject(gsonResult);
                JSONArray jsonArray = jsonObject.optJSONArray("results");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONObject jsonValue = jsonObject1.getJSONObject("values");
                String text = jsonValue.get("text").toString();
                chatMessage.setMessage(text);
            } catch (Exception e) {
                e.printStackTrace();
                chatMessage.setMessage("服务器繁忙，请稍候再试");
            }
        }
        chatMessage.setData(new Date());
        chatMessage.setType(ChatMessage.Type.Robot);
        return chatMessage;
    }

    public static String getInputUrl(String message) {
        JSONObject reqJson = new JSONObject();
        // 输入类型:0-文本(默认)、1-图片、2-音频
        int reqType = 0;
        String reqStr = "";
        try {
            reqJson.put("reqType", reqType);
            JSONObject perception = new JSONObject();
            // 输入信息,里面包含inputText，inputImage，selfInfo
            // 输入的文本信息
            JSONObject inputText = new JSONObject();
            inputText.put("text", message);
            perception.put("inputText", inputText);
            JSONObject userInfo = new JSONObject();
            userInfo.put("apiKey", Config.getInstance().getAppKey());
            userInfo.put("userId", Config.getInstance().getUserId());
            reqJson.put("perception", perception);
            reqJson.put("userInfo", userInfo);
            reqStr = reqJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reqStr;

    }

    /*
      get请求
      return：数据
     */
    public static String doGet(String message) {
        String reqStr = getInputUrl(message);
        BufferedReader in = null;
        PrintWriter out = null;   //该类可用来创建一个文件并向文本文件写入数据
        String responseStr = "";

        try {
            URL url1 = new URL(Config.getInstance().getUrlKey());
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            //   connection.setRequestMethod("GET");

            // 设置请求属性
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestProperty("Content-Type", "application/json");//设定 请求格式 json
            connection.setRequestProperty("x-adviewrtb-version", "2.1");
            // 发送POST请求的必要设定
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流并发送
            out = new PrintWriter(connection.getOutputStream());
            out.write(reqStr);
            // 刷新输出流的缓冲
            out.flush();
            connection.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                responseStr += line;    //返回的json数据
            }
        } catch (Exception e) {
            Log.d("HTTPUtils", "发送 POST 请求出现异常！" + e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseStr;
    }

   /*
    private static String setParmat(String message) {
        String url = "";
        try {
            url = Config.getInstance().getUrlKey() + "?" + "key=" + Config.getInstance().getAppKey() + "&userId=" + Config.getInstance().getUserId() + "&info="
                    + URLEncoder.encode(message, "UTF-8");        //URLEncoder 可将字符串以URL编码，用于编码处理

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }*/

}


