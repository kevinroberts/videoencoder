package com.vinberts.videoconverter.gui.actions;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegProgress;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.NullOutput;
import com.github.kokorin.jaffree.ffmpeg.ProgressListener;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import com.vinberts.videoconverter.gui.helpers.MessageWithLink;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class FileEncodeAction implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(FileEncodeAction.class);
    private JTable fileListTable;
    private Thread encodeThread;
    private final int percentCol = 4;

    public FileEncodeAction(final JTable fileListTable) {
        this.fileListTable = fileListTable;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LOG.debug("Encode process action called");
        DefaultTableModel model = (DefaultTableModel) fileListTable.getModel();
        if (model.getRowCount() > 0) {
            JButton source = (JButton) e.getSource();

            if (source.getText().startsWith("Pause")) {
                LOG.info("Interrupting encoding thread...");
                encodeThread.interrupt();
                source.setText("Resume encode");
                source.setIcon(new ImageIcon(getClass().getResource("/play-circle.png")));
            } else if (source.getText().startsWith("Start") || source.getText().startsWith("Resume")) {
                source.setText("Pause encode");
                source.setIcon(new ImageIcon(getClass().getResource("/pause-circle.png")));

                encodeThread = new Thread(() -> {
                    Object waiter = new Object();
                    synchronized (waiter) {
                        int fileCol = 6;
                        int rows = model.getRowCount();
                        boolean done = false;
                        while (!done) {
                            for (int row = 0; row <= rows - 1; row++) {
                                if (Thread.interrupted()) {
                                    break;
                                }
                                Integer encodePercentage = (Integer) model.getValueAt(row, percentCol);
                                File videoFile = (File) model.getValueAt(row, fileCol);

                                if (encodePercentage < 100) {
                                    final AtomicLong duration = new AtomicLong();

                                    final FFmpegResult nullResult = FFmpeg.atPath(Paths.get(System.getProperty("FFMPEG_PATH")))
                                            .addInput(UrlInput.fromPath(Paths.get(videoFile.getAbsolutePath())))
                                            .addOutput(new NullOutput())
                                            .setOverwriteOutput(true)
                                            .setProgressListener(new ProgressListener() {
                                                @Override
                                                public void onProgress(FFmpegProgress progress) {
                                                    duration.set(progress.getTimeMillis());
                                                }
                                            })
                                            .execute();

                                    final int finalRow = row;
                                    ProgressListener listener = new ProgressListener() {
                                        private long lastReportTs = System.currentTimeMillis();

                                        @Override
                                        public void onProgress(FFmpegProgress progress) {
                                            long now = System.currentTimeMillis();
                                            if (lastReportTs + 1000 < now) {
                                                long percent = 100 * progress.getTimeMillis() / duration.get();
                                                model.setValueAt((int)percent, finalRow, percentCol);
                                            }
                                        }
                                    };
                                    String encodedFilePath = System.getProperty("ENCODER_PATH") + "/" +
                                            FilenameUtils.removeExtension(videoFile.getName())
                                            + ".mkv";
                                    try {
                                        FFmpegResult result = FFmpeg.atPath(Paths.get(System.getProperty("FFMPEG_PATH")))
                                                .addInput(UrlInput.fromPath(Paths.get(videoFile.getAbsolutePath())))
                                                .addOutput(UrlOutput.toPath(Paths.get(encodedFilePath))
                                                        .addArguments("-vcodec", "libx265")
                                                        .addArguments("-threads", "0")
                                                        .addArguments("-acodec", "copy"))
                                                .setProgressListener(listener)
                                                .setOverwriteOutput(true)
                                                .execute();
                                    } catch (Exception exception) {
                                        LOG.error("Encoding interrupted occurred", exception);
                                        // reset progress for current item to (0) zero percent
                                        model.setValueAt(0, finalRow, percentCol);
                                        done = true;
                                        break;
                                    }
                                }
                            }
                            if (checkIfAllEncodesAreComplete(model)) {
                                done = true;
                                source.setText("Start encode");
                                source.setIcon(new ImageIcon(getClass().getResource("/play-circle.png")));
                                JOptionPane.showMessageDialog(fileListTable, new MessageWithLink("Encoding is done!"));
                            }
                        }
                    }
                });
                encodeThread.start();
            }

        } else {
            JOptionPane.showMessageDialog(fileListTable, new MessageWithLink("Please add some video files first"));
        }
    }

    boolean checkIfAllEncodesAreComplete(DefaultTableModel model) {
        boolean isDone = true;
        int rows = model.getRowCount();
        for (int row = 0; row <= rows - 1; row++) {
            Integer encodePercentage = (Integer) model.getValueAt(row, percentCol);
            if (encodePercentage < 100) {
                isDone = false;
            }
        }
        return isDone;
    }
}
