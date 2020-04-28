package com.vinberts.videoconverter.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class OpenPreferencesAction implements ActionListener {
    private JFrame preferencesFrame;

    public OpenPreferencesAction(final JFrame preferencesFrame) {
        this.preferencesFrame = preferencesFrame;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (!preferencesFrame.isVisible()) {
            preferencesFrame.setVisible(true);
        }
    }
}
