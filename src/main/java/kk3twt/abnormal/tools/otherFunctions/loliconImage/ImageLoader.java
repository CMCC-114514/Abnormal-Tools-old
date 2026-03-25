package kk3twt.abnormal.tools.otherFunctions.loliconImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * 后台加载图片的 SwingWorker。
 * 从指定 URL 下载图片，缩放至适应窗口尺寸后更新到 JLabel 中。
 */
public class ImageLoader extends SwingWorker<ImageIcon, Void> {

    /** 图片的 URL 地址 */
    protected String imageUrl;

    /** 显示图片的标签 */
    protected JLabel imageLabel;

    /** 所属的窗口（用于获取目标尺寸） */
    protected JFrame imageFrame;

    /** 原始图片（未缩放），可用于后续保存 */
    protected BufferedImage originalImage;

    /**
     * 构造图片加载器。
     *
     * @param imageUrl   图片 URL
     * @param imageLabel 显示图片的标签
     * @param imageFrame 所属窗口（用于获取窗口内容面板的尺寸）
     */
    public ImageLoader(String imageUrl, JLabel imageLabel, JFrame imageFrame) {
        this.imageUrl = imageUrl;
        this.imageLabel = imageLabel;
        this.imageFrame = imageFrame;
    }

    /**
     * 在后台线程中执行图片加载和缩放。
     *
     * @return 缩放后的 ImageIcon，若加载失败则返回 null
     */
    @Override
    protected ImageIcon doInBackground() {
        System.setProperty("java.net.useSystemProxies", "true");
        try {
            // 从 URL 读取原始图片
            URL url = new URL(imageUrl);
            originalImage = ImageIO.read(url);
            if (originalImage == null) {
                throw new IOException("无法加载图片");
            }

            // 获取窗口内容面板的目标尺寸
            int targetWidth = imageFrame.getContentPane().getWidth();
            int targetHeight = imageFrame.getContentPane().getHeight();

            // 计算等比缩放后的尺寸
            Dimension scaledSize = getScaledDimension(
                    new Dimension(originalImage.getWidth(), originalImage.getHeight()),
                    new Dimension(targetWidth, targetHeight));

            // 缩放图片（使用高质量缩放）
            Image scaledImage = originalImage.getScaledInstance(
                    scaledSize.width, scaledSize.height, Image.SCALE_SMOOTH);

            // 转换为 BufferedImage 以便进一步处理（可选）
            BufferedImage bufferedScaled = new BufferedImage(
                    scaledSize.width, scaledSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedScaled.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            return new ImageIcon(bufferedScaled);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "加载图片时发生错误：" + e.getMessage());
            return null;
        }
    }

    /**
     * 在 EDT 中更新 UI，将加载后的图标设置到标签上。
     */
    @Override
    protected void done() {
        try {
            ImageIcon icon = get();
            if (icon != null) {
                imageLabel.setIcon(icon);
                imageLabel.setText(""); // 清除占位文字
            } else {
                imageLabel.setText("图片加载失败");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "加载图片异常：" + e.getMessage());
            imageLabel.setText("加载异常" + e.getMessage());
        }
    }

    /**
     * 计算保持原比例的尺寸，使图片完全适应目标区域。
     *
     * @param imgSize    原始图片尺寸
     * @param targetSize 目标区域尺寸
     * @return 缩放后的尺寸
     */
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