package fileFunctions.imageScramble;

import javax.swing.*;
import java.awt.*;

public class ScrambleProgress {

    private JDialog dialog;
    private JProgressBar progressBar;

    public void start(String input, String output,
                      int w, int h, int fps,
                      long seed, int choose,
                      boolean scramble) {

        dialog = new JDialog((Frame) null, "视频处理中", true);

        progressBar = new JProgressBar(0,100);
        progressBar.setStringPainted(true);

        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(progressBar,BorderLayout.CENTER);
        dialog.add(new JLabel("视频混淆与解混淆需要较长时间，请耐心等待", SwingConstants.CENTER), BorderLayout.SOUTH);
        dialog.setSize(350,90);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        SwingWorker<Void,Integer> worker = new SwingWorker<>() {

            long totalFrames;

            @Override
            protected Void doInBackground() throws Exception {

                totalFrames = VideoScrambler.probeFrameCount(input);

                if(scramble){
                    VideoScrambler.scramble(input, output,
                            w,h,fps,seed,choose,
                            this::updateProgress);
                }else{
                    VideoScrambler.descramble(input, output,
                            w,h,fps,seed,choose,
                            this::updateProgress);
                }

                return null;
            }

            private void updateProgress(long processed){
                int progress = (int)((processed * 100) / totalFrames);
                setProgress(progress);
            }

            @Override
            protected void done() {
                dialog.dispose();
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if("progress".equals(evt.getPropertyName())){
                int value = (Integer) evt.getNewValue();
                progressBar.setValue(value);
                progressBar.setString(value + "%");
            }
        });

        worker.execute();
        dialog.setVisible(true);
    }
}