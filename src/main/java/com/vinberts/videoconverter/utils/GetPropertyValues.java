package com.vinberts.videoconverter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

/**
 *
 */
public class GetPropertyValues {
    private static final Logger LOG = LoggerFactory.getLogger(GetPropertyValues.class);
    private String result = "";
    private InputStream inputStream;

    public void getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            result = prop.getProperty("FFMPEG_PATH");

            for (String name : prop.stringPropertyNames()) {
                String value = prop.getProperty(name);
                System.setProperty(name, value);
            }

            if (Objects.isNull(result) || result.equals("")) {
                LOG.error("FFMPEG_PATH is not set - please set one in system properties");
                System.exit(1);
            }
            if (Objects.isNull(prop.getProperty("ENCODER_PATH"))) {
                LOG.error("ENCODER_PATH is not set - please set one in system properties");
                System.exit(1);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

}
