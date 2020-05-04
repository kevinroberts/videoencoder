package com.vinberts.videoconverter.gui.actions;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.Stream;
import com.vinberts.videoconverter.utils.impl.FFMpegProperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.vinberts.videoconverter.utils.Constants.H265_MKV_CONTAINER;
import static com.vinberts.videoconverter.utils.TableUtils.humanReadableDuration;

/**
 *
 */
public class FileOpenAction implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(FileOpenAction.class);
    private JTable fileListTable;
    private JLabel fileLoadingIndicator;
    private FFMpegProperImpl ffMpegProper = new FFMpegProperImpl();

    public FileOpenAction(final JTable fileListTable, final JLabel fileLoadingIndicator) {
        this.fileListTable = fileListTable;
        this.fileLoadingIndicator = fileLoadingIndicator;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LOG.debug("File open action called...");
        // create an object of JFileChooser class
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        // allow multiple file selection
        j.setMultiSelectionEnabled(true);
        j.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // only allow files of .mkv or .mp4 extension
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .mkv or .mp4 files", "mkv", "mp4");
        j.addChoosableFileFilter(restrict);

        j.setAcceptAllFileFilterUsed(false);

        int r = j.showOpenDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            // get the selected files
            File[] files = j.getSelectedFiles();

            fileLoadingIndicator.setVisible(true);
            new SwingWorker<Void, String>() {
                @Override
                protected Void doInBackground() throws Exception {
                    StringBuilder filesChosen = new StringBuilder();

                    DefaultTableModel model = (DefaultTableModel) fileListTable.getModel();
                    int t = 0;
                    // set the label to the path of the selected files
                    while (t++ < files.length) {
                        File file = files[t - 1];
                        filesChosen.append(file.getAbsolutePath()).append(",");
                        FFprobeResult result = ffMpegProper.getProbeResultForVideoFile(file);
                        String humanDuration = "Unknown";
                        Long duration = 0L;
                        String codecName = "UNKNOWN";
                        for (Stream stream : result.getStreams()) {
                            if (stream.getCodecType().equals(StreamType.VIDEO)) {
                                codecName = stream.getCodecName();
                                duration = stream.getDuration(TimeUnit.SECONDS);
                            }
                        }
                        if (Objects.isNull(duration) || duration == 0L) {
                            for (Stream stream : result.getStreams()) {
                                if (Objects.nonNull(stream.getDuration())) {
                                    duration = stream.getDuration(TimeUnit.SECONDS);
                                }
                            }
                        }
                        if (Objects.nonNull(duration)) {
                            humanDuration = humanReadableDuration(Duration.ofSeconds(duration));
                        }

                        model.addRow(new Object[]{file.getName(), humanDuration, codecName, H265_MKV_CONTAINER, 0, "Remove", file});
                    }

                    LOG.debug("User chose files: " + filesChosen.toString());
                    return null;
                }

                @Override
                protected void done() {
                    fileLoadingIndicator.setVisible(false);
                }
            }.execute();

        }
        // if the user cancelled the operation
        else {
            LOG.info("user canceled");
        }
    }
}
