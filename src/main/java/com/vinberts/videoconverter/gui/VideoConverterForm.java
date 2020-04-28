package com.vinberts.videoconverter.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.vinberts.videoconverter.gui.actions.ClearTableAction;
import com.vinberts.videoconverter.gui.actions.FileEncodeAction;
import com.vinberts.videoconverter.gui.actions.FileOpenAction;
import com.vinberts.videoconverter.gui.actions.OpenPreferencesAction;
import com.vinberts.videoconverter.gui.helpers.ButtonColumn;
import com.vinberts.videoconverter.gui.helpers.FileListTableModel;
import com.vinberts.videoconverter.gui.helpers.MessageWithLink;
import com.vinberts.videoconverter.gui.helpers.ProgressRenderer;
import com.vinberts.videoconverter.utils.TableUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static com.vinberts.videoconverter.utils.Constants.ABOUT_TEXT;
import static com.vinberts.videoconverter.utils.Constants.H265_MKV_1080P;
import static com.vinberts.videoconverter.utils.Constants.H265_MKV_720P;
import static com.vinberts.videoconverter.utils.Constants.MAX_PROGRESS;

/**
 *
 */
public class VideoConverterForm extends JFrame {

    private JPanel mainPanel;
    private JMenuBar mainMenu;
    private JButton startEncodeButton;
    private JButton pauseButton;
    private JButton openButton;
    private JTable fileListTable;
    private JScrollPane fileListPane;


    public VideoConverterForm(final String title) throws HeadlessException {
        super(title);
        $$$setupUI$$$();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        JFrame preferencesFrame = new VideoConverterPreferences("Video Converter Preferences");
        preferencesFrame.setMinimumSize(new Dimension(583, 400));

        mainMenu = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        // Create menu items
        JMenuItem aboutMenuItem = new JMenuItem("About Video Converter");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
        JMenuItem clearMenuItem = new JMenuItem("Clear all");
        JMenuItem exitMenuItem = new JMenuItem("Quit");

        aboutMenuItem.addActionListener((event) -> JOptionPane.showMessageDialog(this, new MessageWithLink(ABOUT_TEXT)));

        openMenuItem.setToolTipText("Open video source files");
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        // change to file opener
        openMenuItem.addActionListener(new FileOpenAction(fileListTable));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        // Quit menu actions
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.addActionListener((event) -> System.exit(0));
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        clearMenuItem.setToolTipText("Clear all videos from processing list");
        clearMenuItem.setMnemonic(KeyEvent.VK_C);
        clearMenuItem.addActionListener(new ClearTableAction(fileListTable));
        clearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        preferencesMenuItem.setToolTipText("Change video converter preferences");
        preferencesMenuItem.addActionListener(new OpenPreferencesAction(preferencesFrame));
        preferencesMenuItem.setMnemonic(KeyEvent.VK_COMMA);
        preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        fileMenu.add(aboutMenuItem);
        fileMenu.add(preferencesMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(clearMenuItem);
        fileMenu.add(exitMenuItem);


        mainMenu.add(fileMenu);

        // button actions
        openButton.addActionListener(new FileOpenAction(fileListTable));
        startEncodeButton.addActionListener(new FileEncodeAction(fileListTable));

        // table actions

        //set encoding options
        Object options[] = {H265_MKV_1080P, H265_MKV_720P};
        JComboBox comboBox = new JComboBox(options);
        comboBox.setMaximumRowCount(4);
        TableCellEditor editor = new DefaultCellEditor(comboBox);
        fileListTable.getColumnModel().getColumn(3).setCellEditor(editor);

        // add delete row button action
        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int modelRow = Integer.parseInt(e.getActionCommand());
                ((DefaultTableModel) table.getModel()).removeRow(modelRow);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(fileListTable, delete, 5);
        buttonColumn.setMnemonic(KeyEvent.VK_R);

        fileListTable.setDefaultRenderer(Integer.class, new ProgressRenderer(0, MAX_PROGRESS));

        this.setJMenuBar(mainMenu);

        this.pack();
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(""));
        startEncodeButton = new JButton();
        startEncodeButton.setIcon(new ImageIcon(getClass().getResource("/play-circle.png")));
        startEncodeButton.setText("Start Encode");
        mainPanel.add(startEncodeButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, new Dimension(50, -1), 1, false));
        pauseButton = new JButton();
        pauseButton.setIcon(new ImageIcon(getClass().getResource("/pause-circle.png")));
        pauseButton.setText("Pause");
        mainPanel.add(pauseButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        openButton = new JButton();
        openButton.setIcon(new ImageIcon(getClass().getResource("/plus-circle.png")));
        openButton.setText("Open");
        mainPanel.add(openButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileListPane.setEnabled(true);
        Font fileListPaneFont = this.$$$getFont$$$(null, -1, -1, fileListPane.getFont());
        if (fileListPaneFont != null) fileListPane.setFont(fileListPaneFont);
        mainPanel.add(fileListPane, new GridConstraints(1, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    private void createUIComponents() {

        String[] columns = new String[]{
                "File name", "Duration (sec)", "Video Codec", "Re-Encode Option", "Encode Progress", "Actions", ""
        };
        Object[][] data = new Object[][]{};

        fileListTable = new JTable(new FileListTableModel(data, columns));
        fileListTable.setFont(new Font("Serif", Font.PLAIN, 16));
        JTableHeader tableHeader = fileListTable.getTableHeader();
        tableHeader.setFont(new Font("Serif", Font.PLAIN, 16));
        fileListTable.setRowHeight(30);
        TableUtils.setJTableColumnsWidth(fileListTable, 950, 30, 11, 10, 19, 20, 10, 0);
        // hide video file col
        fileListTable.getColumnModel().getColumn(6).setWidth(0);
        fileListTable.getColumnModel().getColumn(6).setMinWidth(0);
        fileListTable.getColumnModel().getColumn(6).setMaxWidth(0);
        fileListPane = new JScrollPane(fileListTable);
    }
}
