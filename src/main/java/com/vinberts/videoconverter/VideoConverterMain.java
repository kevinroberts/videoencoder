package com.vinberts.videoconverter;

import com.vinberts.videoconverter.gui.CelsiusConverterGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 *
 */
public class VideoConverterMain {
    private static final Logger LOG = LoggerFactory.getLogger(VideoConverterMain.class);
    public static void main(String[] args) {
        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean IS_MAC = lcOSName.startsWith("mac os x");
        if (IS_MAC) {
            LOG.info("video converter running on mac osx: " + System.getProperty("os.name"));
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        JFrame frame = new CelsiusConverterGUI("My Celsius Converter");
        frame.setVisible(true);
    }
}
