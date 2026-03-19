package kk3twt.abnormal.tools.otherFunctions.loliconImage;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * 调用 Lolicon API 获取图片信息的工具类。
 * 支持按标签检索、按 PID 检索、随机获取图片，并可选择是否包含 R18 内容。
 */
public class ImageInfo {

    /** API 返回的 JSON 数据中的 "data" 数组，包含图片详细信息 */
    private final JSONArray data;

    /**
     * 根据指定参数构造并发送 API 请求，解析返回的 JSON 数据。
     *
     * @param random  是否为随机模式（true 时忽略标签和 PID，随机返回图片）
     * @param usePid  是否使用 PID 模式（true 时按 PID 获取图片）
     * @param pid     目标图片的 PID（usePid 为 true 时有效）
     * @param r18     是否包含 R18 图片：0=禁用，1=仅 R18，2=混合（API 默认值）
     * @param tags    标签数组（random 为 false 且 usePid 为 false 时有效）
     * @throws Exception 请求失败、状态码非 200 或解析异常时抛出
     */
    public ImageInfo(boolean random, boolean usePid, int pid, int r18, String[] tags) throws Exception {
        // 构建 API 请求 URL
        String tag = processTag(tags);
        String lolicon = "https://api.lolicon.app/setu/v2?";

        if (!random)                        // tag检索模式（非随机）
            lolicon += tag;
        else if (usePid)                    // pid检索模式
            lolicon += "pid=" + pid;

        lolicon += "r18=" + r18;

        // 创建 HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // 构建 Get 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(lolicon))
                .header("Accept", "application/json")
                .GET()
                .build();

        // 发送请求
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 检查响应状态码
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();

            // 使用 fastjson 解析
            JSONObject jsonObject = JSON.parseObject(jsonResponse);
            data = jsonObject.getJSONArray("data");
        } else {
            throw new Exception("图片获取失败，statusCode = " + response.statusCode());
        }
    }

    /**
     * 将标签数组拼接为 URL 查询参数格式。
     *
     * @param tags 标签数组
     * @return 形如 "tag=标签1&tag=标签2&" 的字符串
     */
    private String processTag(String[] tags) {
        StringBuilder builder = new StringBuilder();

        for (String tag : tags) {
            builder.append("tag=").append(tag).append("&");
        }

        return builder.toString();
    }

    /**
     * 获取第一张图片的原图 URL。
     *
     * @return 图片 URL 字符串，若无数据则返回空字符串
     */
    public String getUrl() {
        String imageUrl = "";

        if (data != null && !data.isEmpty()) {
            JSONObject info = data.getJSONObject(0);
            if (info != null) {
                JSONObject urls = info.getJSONObject("urls");
                imageUrl = urls.getString("original");
            }
        }

        return imageUrl;
    }

    /**
     * 获取第一张图片的详细信息，包括标题、作者、是否为 AI 作品、PID、UID、分辨率。
     *
     * @return 字符串数组，按顺序包含上述信息
     */
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

    /**
     * 获取第一张图片的标签数组。
     *
     * @return 字符串数组，每个元素为一个标签
     */
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