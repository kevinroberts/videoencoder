package com.vinberts.videoconverter.utils;

import com.github.kokorin.jaffree.ffprobe.FFprobeResult;

import java.io.File;

/**
 *
 */
public interface FFMpegProber {

    FFprobeResult getProbeResultForVideoFile(final File file);

}
