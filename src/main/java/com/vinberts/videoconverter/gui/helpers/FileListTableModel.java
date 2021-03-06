package com.vinberts.videoconverter.gui.helpers;

import javax.swing.table.DefaultTableModel;
import java.io.File;

/**
 *
 */
public class FileListTableModel extends DefaultTableModel {
    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class, String.class, Integer.class, String.class, File.class
    };

    public FileListTableModel(final Object[][] data, final Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 3 || column == 5;
    }
}
