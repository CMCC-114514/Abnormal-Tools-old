package utils;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class Downloader extends SwingWorker<Void, Integer> {

    private final Path target;
    private final String url;

    public Downloader(Path target, String url) {
        this.target = target;
        this.url = url;
    }

    @Override
    protected Void doInBackground() throws Exception {

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        // ===== 1. 读取本地已下载大小 =====
        long existingSize = 0;
        if (Files.exists(target)) {
            existingSize = Files.size(target);
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "*/*")
                .header("Accept-Encoding", "identity");

        // ===== 2. 如果存在文件则请求 Range =====
        if (existingSize > 0) {
            requestBuilder.header("Range", "bytes=" + existingSize + "-");
        }

        HttpRequest request = requestBuilder.build();

        HttpResponse<InputStream> response =
                client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        int status = response.statusCode();

        // ===== 3. 校验服务器是否支持断点 =====
        if (existingSize > 0 && status != 206) {
            // 服务器不支持断点，重新下载
            existingSize = 0;
        }

        if (status != 200 && status != 206) {
            throw new Exception("下载失败，HTTP=" + status);
        }

        long contentLength = response.headers()
                .firstValueAsLong("Content-Length")
                .orElse(-1);

        // ===== 4. 总大小 = 已下载 + 本次 =====
        long totalSize = contentLength > 0 ? contentLength + existingSize : -1;

        // 创建父目录
        Files.createDirectories(target.getParent());

        try (InputStream in = response.body();
             OutputStream out = new BufferedOutputStream(
                     new FileOutputStream(target.toFile(), existingSize > 0))) {

            byte[] buffer = new byte[8192];
            long totalBytesRead = existingSize;
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {

                if (isCancelled()) break;

                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                if (totalSize > 0) {
                    int percent = (int) (totalBytesRead * 100 / totalSize);
                    setProgress(Math.min(percent, 100));
                }
            }
        }

        return null;
    }
}
