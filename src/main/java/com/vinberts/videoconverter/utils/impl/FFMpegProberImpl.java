package com.vinberts.videoconverter.utils.impl;

import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.vinberts.videoconverter.utils.FFMpegProber;

import java.io.File;
import java.nio.file.Paths;

/**
 *
 */
public class FFMpegProberImpl implements FFMpegProber {

    @Override
    public FFprobeResult getProbeResultForVideoFile(final File file) {
        FFprobe ffprobe = FFprobe.atPath(Paths.get(System.getProperty("FFMPEG_PATH")));

        FFprobeResult result = ffprobe
                .setShowStreams(true)
                .setInput(file.getAbsolutePath())
                .execute();

        return result;
    }
}
