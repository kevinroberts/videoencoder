package com.vinberts.videoconverter;

import com.vinberts.videoconverter.gui.VideoConverterForm;
import com.vinberts.videoconverter.utils.GetPropertyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * FFMPEG options list:
 * https://gist.github.com/tayvano/6e2d456a9897f55025e25035478a3a50
 */
public class VideoConverterMain {
    private static final Logger LOG = LoggerFactory.getLogger(VideoConverterMain.class);

    public static void main(String[] args) throws IOException {
        // load system properties - FFMPEG path
        GetPropertyValues properties = new GetPropertyValues();
        properties.getPropValues();

        try {
            URL iconURL = VideoConverterMain.class.getResource("/play-circle.png");
            Image image = new ImageIcon(iconURL).getImage();
            Taskbar.getTaskbar().setIconImage(image);
        } catch (Exception e) {
            LOG.error("Could not set APP icon", e);
        }

        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean IS_MAC = lcOSName.startsWith("mac os x");
        if (IS_MAC) {
            LOG.info("video converter running on mac osx: " + System.getProperty("os.name"));
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        JFrame frame = new VideoConverterForm("Video Converter");
        frame.setMinimumSize(new Dimension(950,600));
        frame.setVisible(true);

    }
}
