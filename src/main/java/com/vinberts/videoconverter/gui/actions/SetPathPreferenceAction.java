package com.vinberts.videoconverter.gui.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class SetPathPreferenceAction implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(SetPathPreferenceAction.class);
    private JLabel valueLabel;

    public SetPathPreferenceAction(final JLabel valueLabel) {
        this.valueLabel = valueLabel;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        JButton source = (JButton) e.getSource();
        LOG.debug("Set path preference action called ... " + source.getText());
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        j.setMultiSelectionEnabled(false);

        int r = j.showOpenDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            String selectedPath = j.getSelectedFile().getAbsolutePath();
            LOG.debug("user selected path: " + selectedPath);
            if (source.getText().startsWith("Set FFMPEG")) {
                System.setProperty("FFMPEG_PATH", selectedPath);
            } else {
                System.setProperty("ENCODER_PATH", selectedPath);
            }
            valueLabel.setText(selectedPath);
        }
        // if the user cancelled the operation
        else {
            LOG.info("user canceled");
        }

    }
}
