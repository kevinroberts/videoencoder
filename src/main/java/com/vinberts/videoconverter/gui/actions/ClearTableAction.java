package com.vinberts.videoconverter.gui.actions;

import com.vinberts.videoconverter.gui.helpers.MessageWithLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class ClearTableAction implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(ClearTableAction.class);
    private JTable fileListTable;

    public ClearTableAction(final JTable fileListTable) {
        this.fileListTable = fileListTable;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LOG.debug("Clear table action called...");
        DefaultTableModel model = (DefaultTableModel) fileListTable.getModel();
        if (model.getRowCount() > 0) {
            LOG.debug("table cleared");
            model.setRowCount(0);
        } else {
            JOptionPane.showMessageDialog(fileListTable, new MessageWithLink("Please add some files first"));
        }
    }
}
