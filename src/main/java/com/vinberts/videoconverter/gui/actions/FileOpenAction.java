package com.vinberts.videoconverter.gui.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.vinberts.videoconverter.utils.Constants.H265_MKV_1080P;

/**
 *
 */
public class FileOpenAction implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(FileOpenAction.class);
    private JTable fileListTable;

    public FileOpenAction(final JTable fileListTable) {
        this.fileListTable = fileListTable;
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

            StringBuilder filesChosen = new StringBuilder();

            DefaultTableModel model = (DefaultTableModel) fileListTable.getModel();
            int t = 0;
            // set the label to the path of the selected files
            while (t++ < files.length) {
                File file = files[t - 1];
                filesChosen.append(file.getAbsolutePath()).append(",");
                model.addRow(new Object[]{file.getName(), 10L, "h.264", H265_MKV_1080P, 0, "Remove"});
            }

            LOG.debug("User chose files: " + filesChosen.toString());
        }
        // if the user cancelled the operation
        else {
            LOG.info("user canceled");
        }
    }
}
