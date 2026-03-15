package kk3twt.abnormal.tools.utils;

import java.nio.file.*;

// 工具类：返回运行环境的路径
public final class AppPath {

    private AppPath() {}

    public static Path appHome() {
        try {
            Path location = Paths.get(
                    AppPath.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );

            String path = location.toString().replace("\\", "/");

            // ① 开发期：在 build/classes 下运行
            if (path.contains("/build/classes/")) {
                // 回到项目根目录
                return location
                        .getParent()   // main
                        .getParent()   // java
                        .getParent()   // classes
                        .getParent()   // build
                        .getParent();   // 项目根目录的父目录
            }

            // ② 发布期：jar 在 app 目录
            if (path.endsWith(".exe") || path.endsWith(".jar")) {
                return location.getParent();
            }

            throw new IllegalStateException("无法识别运行环境: " + path);

        } catch (Exception e) {
            throw new RuntimeException("定位 appHome 失败", e);
        }
    }

    public static Path libDir() {
        Path lib = appHome().resolve("resources");
        try {
            Files.createDirectories(lib);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lib;
    }

    public static Path resourcePath(String name) {
        return libDir().resolve(name);
    }

    public static boolean resourceCheck(String name) {
        return Files.exists(AppPath.resourcePath(name));
    }

    public static void main(String[] args) {
        System.out.println(appHome());
    }
}

