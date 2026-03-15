package kk3twt.abnormal.tools.otherFunctions.loliconImage;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ImageInfo {

    private final JSONArray data;

    public ImageInfo(boolean random, boolean usePid, int pid, int r18, String[] tags) throws Exception {
        // API地址
        String tag = processTag(tags);
        String lolicon = "https://api.lolicon.app/setu/v2?";

        if (!random)                        // tag检索模式（非随机）
            lolicon += tag;
        else if (usePid)                    // pid检索模式
            lolicon += "pid=" + pid;

        lolicon += "r18=" + r18;

        // 创建 HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // 构建Get请求，期望json响应
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(lolicon))
                .header("Accept", "application/json")
                .GET()
                .build();

        // 发送请求，获取响应体
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 检查响应状态码
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();

            // 使用fastjson解析
            JSONObject jsonObject = JSON.parseObject(jsonResponse);
            data = jsonObject.getJSONArray("data");

        } else {
            throw new Exception("图片获取失败，statusCode = " + response.statusCode());
        }
    }

    private String processTag(String[] tags) {
        StringBuilder builder = new StringBuilder();

        for (String tag : tags) {
            builder.append("tag=").append(tag).append("&");
        }

        return builder.toString();
    }

    public String getUrl() {
        String imageUrl = "";

        // 提取url字段
        // url字段在数组字段data内
        if (data != null && !data.isEmpty()) {
            JSONObject info = data.getJSONObject(0);
            if (info != null) {
                JSONObject urls = info.getJSONObject("urls");
                imageUrl = urls.getString("original");
            }
        }

        return imageUrl;
    }

    public String[] getData() {

        String title = "标题：";
        String author = "作者：";
        String isAI = "AI 作品：";
        String[] AIList = {"未知", "否", "是"};
        String pid = "pid：";
        String uid = "uid：";
        String pixels = "图片分辨率：";

        if (data != null && !data.isEmpty()) {
            JSONObject info = data.getJSONObject(0);
            if (info != null) {
                pid += info.getString("pid");
                uid += info.getString("uid");
                title += info.getString("title");
                author += info.getString("author");
                pixels += info.getString("width") + "*" + info.getString("height");
                isAI += AIList[info.getInteger("aiType")];
            }
        }

        return new String[]{title, author, isAI, pid, uid, pixels};
    }

    public String[] getTags() {
        String[] tags = {""};
        if (data != null && !data.isEmpty()) {
            JSONObject info = data.getJSONObject(0);
            if (info != null) {
                tags = info.getObject("tags", String[].class);
            }
        }
        return tags;
    }
}
