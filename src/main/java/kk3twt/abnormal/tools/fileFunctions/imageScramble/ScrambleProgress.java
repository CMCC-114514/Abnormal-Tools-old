package kk3twt.abnormal.tools.fileFunctions.imageScramble;

import javax.swing.*;
import java.awt.*;

/**
 * 视频混淆/解混淆的进度显示对话框。
 * 在后台执行视频处理任务，并通过进度条实时反馈进度。
 */
public class ScrambleProgress {

    /** 模态对话框，用于显示进度并阻塞用户操作 */
    private JDialog dialog;

    /** 显示处理进度的进度条 */
    private JProgressBar progressBar;

    /**
     * 启动视频处理并显示进度对话框。
     *
     * @param input     输入视频路径
     * @param output    输出视频路径
     * @param w         视频宽度
     * @param h         视频高度
     * @param fps       视频帧率
     * @param seed      随机种子（用于密码混淆）
     * @param choose    混淆算法类型：0=密码混淆，1=希尔伯特混淆
     * @param scramble  true=混淆，false=解混淆
     */
    public void start(String input, String output,
                      int w, int h, int fps,
                      long seed, int choose,
                      boolean scramble) {

        dialog = new JDialog((Frame) null, "视频处理中", true);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(progressBar, BorderLayout.CENTER);
        dialog.add(new JLabel("视频混淆与解混淆需要较长时间，请耐心等待", SwingConstants.CENTER), BorderLayout.SOUTH);
        dialog.setSize(350, 90);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {

            long totalFrames;

            @Override
            protected Void doInBackground() throws Exception {
                // 获取视频总帧数用于进度计算
                totalFrames = VideoScrambler.probeFrameCount(input);

                if (scramble) {
                    VideoScrambler.scramble(input, output,
                            w, h, fps, seed, choose,
                            this::updateProgress);
                } else {
                    VideoScrambler.descramble(input, output,
                            w, h, fps, seed, choose,
                            this::updateProgress);
                }

                return null;
            }

            /**
             * 更新当前处理进度（由 VideoScrambler 回调）。
             * @param processed 已处理的帧数
             */
            private void updateProgress(long processed) {
                int progress = (int) ((processed * 100) / totalFrames);
                setProgress(progress);
            }

            @Override
            protected void done() {
                dialog.dispose();
            }
        };

        // 监听进度变化并更新进度条
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int value = (Integer) evt.getNewValue();
                progressBar.setValue(value);
                progressBar.setString(value + "%");
            }
        });

        worker.execute();
        dialog.setVisible(true);
    }
}