package kk3twt.abnormal.tools.fileFunctions.formatConversion;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片格式转换工具类，基于 Java 原生 ImageIO 实现。
 * 提供支持的图片格式列表、转换方法以及文件选择辅助方法。
 * 注意：转换为 JPG/JPEG 时会自动处理透明度，使用白色背景填充。
 */
public class ImageConverter {
    private ImageConverter() {} // 私有构造，防止实例化

    /** 支持的图片输出格式列表（扩展名） */
    public static final String[] IMAGE_FORMATS = {
            "JPG", "TIFF", "GIF", "PNG", "TIF", "JPEG"
    };

    /**
     * 执行图片格式转换。
     *
     * @param inputPath  输入图片路径
     * @param outputPath 输出图片路径（应包含目标扩展名）
     * @param format     目标格式字符串（如 "JPG", "PNG"），大小写不敏感
     * @throws RuntimeException 如果图片读取失败、写入失败或 ImageIO 不支持该格式
     */
    public static void convert(String inputPath, String outputPath, String format) {
        try {
            BufferedImage image = ImageIO.read(new File(inputPath));
            if (image == null) {
                throw new RuntimeException("无法读取图片");
            }

            String fmt = format.toLowerCase();
            BufferedImage target = image;

            // JPG / JPEG 不支持透明度，需将带 alpha 的图片转为 RGB 并填充白色背景
            if ((fmt.equals("jpg") || fmt.equals("jpeg")) && image.getColorModel().hasAlpha()) {
                BufferedImage rgb = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );

                Graphics2D g = rgb.createGraphics();
                g.setColor(Color.WHITE); // 可更换为其他背景色
                g.fillRect(0, 0, image.getWidth(), image.getHeight());
                g.drawImage(image, 0, 0, null);
                g.dispose();

                target = rgb;
            }

            boolean ok = ImageIO.write(target, fmt, new File(outputPath));
            if (!ok) {
                throw new RuntimeException("ImageIO 不支持该格式: " + format);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO 错误: " + e.getMessage(), e);
        }
    }

    /**
     * 弹出文件选择对话框，让用户选择图片输入文件。
     * 选中后自动将输入路径填入 inputField，并在 outputField 中预填同路径无扩展名的基本名称（用于拼接输出格式）。
     *
     * @param inputField  用于显示所选输入文件路径的文本框
     * @param outputField 用于显示输出基本路径的文本框（不含扩展名）
     */
    public static void chooseInputFile(JTextField inputField, JTextField outputField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        for (String format : ImageConverter.IMAGE_FORMATS) {
            chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter(format + " 文件（*." + format.toLowerCase() + "）", format.toLowerCase()));
        }

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(chooser.getSelectedFile().getAbsolutePath());

            String inputPath = inputField.getText();
            int dot = inputPath.lastIndexOf('.');
            String base = (dot > 0) ? inputPath.substring(0, dot + 1) : inputPath + ".";
            outputField.setText(base);
        }
    }
}