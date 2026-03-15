package kk3twt.abnormal.tools.otherFunctions.loliconImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageLoader extends SwingWorker<ImageIcon, Void> {
    protected String imageUrl;
    protected JLabel imageLabel;
    protected JFrame imageFrame;
    protected BufferedImage originalImage;

    public ImageLoader(String imageUrl, JLabel imageLabel, JFrame imageFrame) {
        this.imageUrl = imageUrl;
        this.imageLabel = imageLabel;
        this.imageFrame = imageFrame;
    }

    @Override
    protected ImageIcon doInBackground() {
        try {
            // 从URL读取原始图片
            URL url = new URL(imageUrl);
            originalImage = ImageIO.read(url);
            if (originalImage == null) {
                throw new IOException("无法加载图片");
            }

            // 获取窗口目标尺寸
            int targetWidth = imageFrame.getContentPane().getWidth();
            int targetHeight = imageFrame.getContentPane().getHeight();

            // 计算等比缩放后的尺寸
            Dimension scaledSize = getScaledDimension(
                    new Dimension(originalImage.getWidth(), originalImage.getHeight()),
                    new Dimension(targetWidth, targetHeight));

            // 缩放图片（使用高质量缩放）
            Image scaledImage = originalImage.getScaledInstance(
                    scaledSize.width, scaledSize.height, Image.SCALE_SMOOTH);

            // 转换为BufferedImage以便可能进一步处理（可选）
            BufferedImage bufferedScaled = new BufferedImage(
                    scaledSize.width, scaledSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedScaled.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            return new ImageIcon(bufferedScaled);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void done() {
        try {
            ImageIcon icon = get();
            if (icon != null) {
                imageLabel.setIcon(icon);
                imageLabel.setText(""); // 清除文字
            } else {
                imageLabel.setText("图片加载失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setText("加载异常");
        }
    }

    // 计算保持原比例的尺寸
    private Dimension getScaledDimension(Dimension imgSize, Dimension targetSize) {
        int originalWidth = imgSize.width;
        int originalHeight = imgSize.height;
        int targetWidth = targetSize.width;
        int targetHeight = targetSize.height;

        double widthRatio = (double) targetWidth / originalWidth;
        double heightRatio = (double) targetHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        return new Dimension(newWidth, newHeight);
    }
}