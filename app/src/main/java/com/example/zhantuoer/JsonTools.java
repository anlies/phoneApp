package com.example.zhantuoer;

import android.text.TextUtils;
import android.util.Log;

import com.example.zhantuoer.Fragment.FriendBase;
import com.example.zhantuoer.Fragment.JiLu_structor;
import com.example.zhantuoer.Fragment.RecyclerGoal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.zhantuoer.Fragment.RecyclerFragment.LOG_TAG;

public final class JsonTools {

    //服务器URL网址
    public static final String CONSTURL = "http://47.106.224.20:8080/";

    //创建私有构造防止创建对象
    private JsonTools() {
    }

    /**
     * 返回通过解析给定 JSON 响应构建的RecyclerGoal对象列表。
     */
    private static List<RecyclerGoal> extractGoals(String recyclerJSON) {
        //在搜索功能中如果搜不到相关内容会返回"[]"
        if(recyclerJSON.equals("[]")){
            return new ArrayList<RecyclerGoal>();
        }
        // 如果 JSON 字符串为空或 null，将提早返回。
        if (TextUtils.isEmpty(recyclerJSON)) {
            return null;
        }
        List<RecyclerGoal> goalList = new ArrayList<RecyclerGoal>();
        // 尝试解析 JSON 响应字符串。如果格式化 JSON 的方式存在问题，
        // 则将抛出 JSONException 异常对象。
        // 捕获该异常以便应用不会崩溃，并将错误消息打印到日志中。
        try {
            JSONArray jsonArray = new JSONArray(recyclerJSON);
            // 使用姓名、标题、图片
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String sid = jsonObject.getString("sid");
                //String photo = jsonObject.getString("photo");
                String name = jsonObject.getString("nickName");
                String tittle = jsonObject.getString("goalName");
                String content = jsonObject.getString("signText");
                String tel = jsonObject.getString("tel");
                String first_img="";
                String sec_img ="";
                String thi_img = "";
                switch (i%4){
                    case 0:
                        break;
                    case 1:
                        first_img="http://47.106.224.20:8080/zhantuoer/t1.jpg";
                        break;
                    case 2:
                        first_img="http://47.106.224.20:8080/zhantuoer/t1.jpg";
                        sec_img="http://47.106.224.20:8080/zhantuoer/t2.jpg";
                        break;
                    case 3:
                        first_img="http://47.106.224.20:8080/zhantuoer/t1.jpg";
                        sec_img="http://47.106.224.20:8080/zhantuoer/t2.jpg";
                        thi_img="http://47.106.224.20:8080/zhantuoer/t3.jpg";
                        break;
                }
                //String first_img = jsonObject.getString("one");
                //String sec_img = jsonObject.getString("two");
                //String thi_img = jsonObject.getString("three");
                String good_number = jsonObject.getString("like");
                String time = jsonObject.getString("signTime");
                Log.w(TAG, "extractGoals: "+sid+name+tittle+content+tel );
                goalList.add(new RecyclerGoal(sid, "", name, tittle, content, first_img, sec_img, thi_img, good_number, time,tel));
            }
        } catch (JSONException e) {
            Log.e(TAG, "extractGoals");
        }
        return goalList;
    }


    private static List<FriendBase> extractFriends(String jiluJSON) {
        if (TextUtils.isEmpty(jiluJSON)) {
            return null;
        }
        List<FriendBase> goalList = new ArrayList<FriendBase>();
        // 尝试解析 JSON 响应字符串。如果格式化 JSON 的方式存在问题，
        // 则将抛出 JSONException 异常对象。
        // 捕获该异常以便应用不会崩溃，并将错误消息打印到日志中。
        try {
            JSONArray jsonArray = new JSONArray(jiluJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                String friendName = jsonArray.getString(i);
                int beijing = 0;
                switch (i%4){
                    case 0:
                        beijing = R.color.md_light_blue_200;
                        break;
                    case 1:
                        beijing  = R.color.md_yellow_200;
                        break;
                    case 2:
                        beijing = R.color.md_pink_200;
                        break;
                    case 3:
                        beijing = R.color.md_purple_200;
                        break;
                }
                goalList.add(new FriendBase("",friendName,3,beijing));
            }
        } catch (JSONException e) {
            Log.e(TAG, "extractGoals");
        }
        return goalList;
    }

    private static List<JiLu_structor> extractJilus(String jiluJSON) {
        if (TextUtils.isEmpty(jiluJSON)) {
            return null;
        }
        List<JiLu_structor> goalList = new ArrayList<JiLu_structor>();
        // 尝试解析 JSON 响应字符串。如果格式化 JSON 的方式存在问题，
        // 则将抛出 JSONException 异常对象。
        // 捕获该异常以便应用不会崩溃，并将错误消息打印到日志中。
        try {
            JiLu_structor.qiandao_fail=0;
            JiLu_structor.doneAll=0;
            JiLu_structor.qiandao_done=0;
            JiLu_structor.work_count =0;
            JiLu_structor.long_day =0;
            JiLu_structor.qiandao_wei = 0;
            JiLu_structor.long_tittle="";
            JSONArray jsonArray = new JSONArray(jiluJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String tid = jsonObject.getString("tid");
                String tittle = jsonObject.getString("goalName");
                String sumSign = jsonObject.getString("sumSign");
                String goalTime = jsonObject.getString("goalTime");
                int active = jsonObject.getInt("active");
                switch (active){
                    case 0:
                        JiLu_structor.qiandao_wei++;
                        break;
                    case 1:
                        JiLu_structor.qiandao_done++;
                        break;
                    case 2:
                        JiLu_structor.qiandao_fail++;
                        break;
                    case 3:
                        JiLu_structor.doneAll++;
                        break;
                }
                if(Integer.parseInt(goalTime)>JiLu_structor.long_day){
                    JiLu_structor.long_day=Integer.parseInt(goalTime);
                    JiLu_structor.long_tittle =tittle;
                }
                Log.w(TAG, "extractJilus: "+tid+tittle+active );
                goalList.add(new JiLu_structor(tid,tittle,sumSign+"/"+goalTime,active));
            }
            JiLu_structor.work_count = jsonArray.length();
        } catch (JSONException e) {
            Log.e(TAG, "extractGoals");
        }
        return goalList;
    }

    private static List<Map_structor> extractMaps(String jiluJSON) {
        if (TextUtils.isEmpty(jiluJSON)) {
            return null;
        }
        List<Map_structor> mapList = new ArrayList<Map_structor>();
        // 尝试解析 JSON 响应字符串。如果格式化 JSON 的方式存在问题，
        // 则将抛出 JSONException 异常对象。
        // 捕获该异常以便应用不会崩溃，并将错误消息打印到日志中。
        try {
            JSONArray jsonArray = new JSONArray(jiluJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String phoneNumber = jsonObject.getString("phoneNumber");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("Latitude");
                Log.w(TAG, "extractJilus: "+phoneNumber+longitude+latitude);
                mapList.add(new Map_structor(phoneNumber,Double.parseDouble(longitude),Double.parseDouble(latitude)));
            }
        } catch (JSONException e) {
            Log.e(TAG, "extractGoals");
        }
        return mapList;
    }

    private static List<GeRenData> extractGeRenDatas(String gerenDataJSON) {
        if (TextUtils.isEmpty(gerenDataJSON)) {
            return null;
        }
        List<GeRenData> dataList = new ArrayList<GeRenData>();
        // 尝试解析 JSON 响应字符串。如果格式化 JSON 的方式存在问题，
        // 则将抛出 JSONException 异常对象。
        // 捕获该异常以便应用不会崩溃，并将错误消息打印到日志中。
        try {
            JSONObject jsonObject = new JSONObject(gerenDataJSON);
            String phoneNumber = jsonObject.getString("phoneNumber");
            String headPhoto = jsonObject.getString("headPhoto");
            String nickName = jsonObject.getString("nickName");
            String introduce = jsonObject.getString("introduce");
            String sex = jsonObject.getString("sex");
            String age = jsonObject.getString("age");
            String education = jsonObject.getString("education");
            String workspace = jsonObject.getString("workplace");
            String city = jsonObject.getString("city");
            Log.w(TAG, "extractGeRenDatas: "+phoneNumber+nickName+introduce+sex+age+education+workspace+city );
            dataList.add(new GeRenData(nickName,headPhoto, sex, age, phoneNumber, introduce, education, workspace, city));
        } catch (JSONException e) {
            Log.e(TAG, "extractGeRenDatas");
        }
        return dataList;
    }


    /**
     * 从给定字符串 URL 返回新 URL 对象。
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "返回URL对象问题");
        }
        return url;
    }

    /**
     * 向给定 URL 进行 HTTP 请求，并返回字符串作为响应。
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);//单位毫秒
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // 如果请求成功（响应代码 200），则读取输入流并解析响应。
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // 关闭输入流可能会抛出 IOException，这就是makeHttpRequest(URL url) 方法签名指定可能抛出 IOException 的原因。
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String makeHttpRequest_short(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(3500);//单位毫秒
            urlConnection.setConnectTimeout(3500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // 如果请求成功（响应代码 200），则读取输入流并解析响应。
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // 关闭输入流可能会抛出 IOException，这就是makeHttpRequest(URL url) 方法签名指定可能抛出 IOException 的原因。
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String makeHttpResponse(URL url) throws IOException {
        String response = "";
        // 如果 URL 为空，则提早返回。
        if (url == null) {
            return "";
        }
        InputStream in = null;
        HttpURLConnection connection = null;
        try {
            // 如果请求成功（响应代码 200），
            // 则继续响应。
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            // 第二步：设置HttpURLConnection连接相关属性
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”
            connection.setConnectTimeout(10000); // 设置连接建立的超时时间
            connection.setReadTimeout(10000); // 设置网络报文收发超时时间
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (connection.getResponseCode() == 200) {
                // 第三步：打开连接输入流读取返回报文，*注意*在此步骤才真正开始网络请求
                in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                response = readFromStream(in);
            } else {
                Log.e(LOG_TAG, "Error code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "JSON results.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                in.close();
            }
        }
        return response;
    }


    private static String makeHttpResponse_short(URL url) throws IOException {
        String response = "";
        // 如果 URL 为空，则提早返回。
        if (url == null) {
            return "";
        }
        InputStream in = null;
        HttpURLConnection connection = null;
        try {
            // 如果请求成功（响应代码 200），
            // 则继续响应。
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            // 第二步：设置HttpURLConnection连接相关属性
            connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”
            connection.setConnectTimeout(3000); // 设置连接建立的超时时间
            connection.setReadTimeout(3000); // 设置网络报文收发超时时间
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (connection.getResponseCode() == 200) {
                // 第三步：打开连接输入流读取返回报文，*注意*在此步骤才真正开始网络请求
                in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                response = readFromStream(in);
            } else {
                Log.e(LOG_TAG, "Error code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "JSON results.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                in.close();
            }
        }
        return response;
    }



    /**
     * 将 输入流InputStream转换为包含自服务器的整个 JSON 响应的字符串。
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * 查询首页数据并返回RecyclerGoal对象的列表。
     */
    public static List<RecyclerGoal> fetchData(String requestUrl) {
        // 创建 URL 对象
        URL url = createUrl(requestUrl);

        // 执行 URL 的 HTTP 请求并接收返回的 JSON 响应
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP请求问题");
        }

        // 从 JSON 响应提取相关域并创建List<RecyclerGoal>的列表
        List<RecyclerGoal> goalList = extractGoals(jsonResponse);

        // 返回列表
        return goalList;
    }

    public static List<JiLu_structor> jiluHelp(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP请求问题");
        }

        List<JiLu_structor> goalList = extractJilus(jsonResponse);

        return goalList;
    }

    public static List<FriendBase> friendHelp(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP请求问题");
        }

        List<FriendBase> goalList = extractFriends(jsonResponse);

        return goalList;
    }

    public static List<Map_structor> dituHelp(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest_short(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP请求问题");
        }

        List<Map_structor> goalList = extractMaps(jsonResponse);

        return goalList;
    }

    protected static List<GeRenData> gerendataHelp(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "HTTP请求问题");
        }

        Log.w(TAG, "gerendataHelp: "+jsonResponse );

        List<GeRenData> dataList = extractGeRenDatas(jsonResponse);

        return dataList;
    }

    protected static String loginhelp(String data) {

        URL url = createUrl(data);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpResponse(url);
        } catch (IOException e) {
            Log.e(TAG, "HTTP请求问题");
        }

        return jsonResponse;
    }

    protected static String send_short_help(String data) {

        URL url = createUrl(data);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpResponse_short(url);
        } catch (IOException e) {
            Log.e(TAG, "HTTP请求问题");
        }

        return jsonResponse;
    }
}
