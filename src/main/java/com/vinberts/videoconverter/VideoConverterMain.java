package com.vinberts.videoconverter;

import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.github.kokorin.jaffree.ffprobe.Stream;
import com.vinberts.videoconverter.gui.VideoConverterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

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
            //System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        FFprobe ffprobe;
        final String ffmpegBin = "/usr/local/bin";
        final String video = "/Users/kevinroberts/Movies/Juicy-J-Let-Me-See-ft-Kevin-Gates-Lil-Skies.mp4";
        ffprobe = FFprobe.atPath(Paths.get(ffmpegBin));

        FFprobeResult result = ffprobe
                .setShowStreams(true)
                .setInput(video)
                .execute();

        for (Stream stream : result.getStreams()) {
            LOG.info("Stream " + stream.getIndex() + " type " + stream.getCodecType() + " duration " + stream.getDuration(TimeUnit.SECONDS));
        }

        JFrame frame = new VideoConverterForm("Video Converter");
        frame.setMinimumSize(new Dimension(800,600));
        frame.setVisible(true);

    }
}
