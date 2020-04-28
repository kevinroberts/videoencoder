package com.vinberts.videoconverter.utils;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegProgress;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.NullOutput;
import com.github.kokorin.jaffree.ffmpeg.ProgressListener;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class FFMpegEncoder {

    private final String ffmpegBin;
    private final Path input;
    private final Path output;

    public FFMpegEncoder(final String ffmpegBin, final Path input, final Path output) {
        this.ffmpegBin = ffmpegBin;
        this.input = input;
        this.output = output;
    }

    public void execute() {
        // The most reliable way to get video duration
        // ffprobe for some formats can't detect duration
        final AtomicLong duration = new AtomicLong();
        final FFmpegResult nullResult = FFmpeg.atPath(Paths.get(ffmpegBin))
                .addInput(UrlInput.fromPath(input))
                .addOutput(new NullOutput())
                .setOverwriteOutput(true)
                .setProgressListener(new ProgressListener() {
                    @Override
                    public void onProgress(FFmpegProgress progress) {
                        duration.set(progress.getTimeMillis());
                    }
                })
                .execute();

        ProgressListener listener = new ProgressListener() {
            private long lastReportTs = System.currentTimeMillis();

            @Override
            public void onProgress(FFmpegProgress progress) {
                long now = System.currentTimeMillis();
                if (lastReportTs + 1000 < now) {
                    long percent = 100 * progress.getTimeMillis() / duration.get();
                    System.out.println("Progress: " + percent + "%");
                }
            }
        };

        FFmpegResult result = FFmpeg.atPath(Paths.get(ffmpegBin))
                .addInput(UrlInput.fromPath(input))
                .addOutput(UrlOutput.toPath(output)
                        .addArguments("-vcodec", "libx265")
                        .addArguments("-threads", "10")
                        .addArguments("-acodec", "copy"))
                .setProgressListener(listener)
                .setOverwriteOutput(true)
                .execute();
    }

}
