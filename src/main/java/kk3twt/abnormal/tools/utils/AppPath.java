package kk3twt.abnormal.tools.utils;

import java.nio.file.*;

/**
 * 工具类，用于获取应用程序运行时的各种路径。
 * <p>
 * 根据运行环境（开发期或发布期）自动调整路径，提供应用根目录、资源目录等方法。
 */
public final class AppPath {

    private AppPath() {}

    /**
     * 返回应用程序的根目录路径。
     * <p>
     * 路径判断规则：
     * <ul>
     *     <li>开发期（在 build/classes 下运行时）：返回项目根目录</li>
     *     <li>发布期（作为 .jar 或 .exe 运行时）：返回 jar/exe 所在目录</li>
     *     <li>其他情况：抛出 IllegalStateException</li>
     * </ul>
     *
     * @return 应用根目录的 Path 对象
     * @throws RuntimeException 如果无法定位根目录
     */
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
                        .getParent()    // main
                        .getParent()    // java
                        .getParent()    // classes
                        .getParent();   // build
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

    /**
     * 返回资源目录（默认为应用根目录下的 "resources" 文件夹）。
     * 如果目录不存在，会自动创建。
     *
     * @return 资源目录的 Path 对象
     * @throws RuntimeException 如果目录创建失败
     */
    public static Path libDir() {
        Path lib = appHome().resolve("resources");
        try {
            Files.createDirectories(lib);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lib;
    }

    /**
     * 返回资源目录下的指定资源路径。
     *
     * @param name 资源文件名
     * @return 资源文件的完整 Path 对象
     */
    public static Path resourcePath(String name) {
        return libDir().resolve(name);
    }

    /**
     * 测试方法，打印应用根目录。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        System.out.println(appHome());
    }
}