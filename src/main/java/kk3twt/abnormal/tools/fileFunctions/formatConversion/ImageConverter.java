package kk3twt.abnormal.tools.fileFunctions.formatConversion;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter {
    private ImageConverter(){}

    public static final String[] IMAGE_FORMATS = {
            "JPG", "TIFF", "GIF", "PNG", "TIF", "JPEG"
    };

    public static void convert(String inputPath, String outputPath, String format) {
        try {
            BufferedImage image = ImageIO.read(new File(inputPath));
            if (image == null) {
                throw new RuntimeException("无法读取图片");
            }

            String fmt = format.toLowerCase();
            BufferedImage target = image;

            // JPG / JPEG 不支持 alpha，必须转 RGB
            if ((fmt.equals("jpg") || fmt.equals("jpeg"))
                    && image.getColorModel().hasAlpha()) {

                BufferedImage rgb = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );

                Graphics2D g = rgb.createGraphics();
                g.setColor(Color.WHITE); // 背景色可换
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

    public static void chooseInputFile(JTextField inputField, JTextField outputField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        for (String format : ImageConverter.IMAGE_FORMATS) {
            chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter(format + " 文件（*."  + format.toLowerCase() + "）", format.toLowerCase()));
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

