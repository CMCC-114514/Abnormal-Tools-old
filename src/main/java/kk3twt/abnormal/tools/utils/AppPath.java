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
     * 返回资源目录（默认为“文档”下的 "Abnormal-Dependencies" 文件夹）。
     * 如果目录不存在，会自动创建。
     *
     * @return 资源目录的 Path 对象
     * @throws RuntimeException 如果目录创建失败
     */
    public static Path resDir() {
        Path lib = Path.of(System.getProperty("user.home")).resolve("Documents").resolve("Abnormal-Dependencies");
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
        return resDir().resolve(name);
    }

    /**
     * 测试方法，打印应用根目录。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        System.out.println(resDir());
    }
}