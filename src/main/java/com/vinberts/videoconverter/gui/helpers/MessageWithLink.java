package com.vinberts.videoconverter.gui.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * MessageWithLink
 * for creating a simple HTML dialog box with clickable links
 */
public class MessageWithLink extends JEditorPane {
    private static final Logger LOG = LoggerFactory.getLogger(MessageWithLink.class);
    private static final long serialVersionUID = 1L;

    public MessageWithLink(String htmlBody) {
        super("text/html", "<html><body style=\"" + getStyle() + "\">" + htmlBody + "</body></html>");
        addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    // Process the click event on the link with java.awt.Desktop.getDesktop().browse())
                    LOG.debug(e.getURL() + " was clicked");
                    if (e.getURL().getProtocol().startsWith("http")) {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(e.getURL().toURI());
                            } catch (IOException ex) {
                                LOG.error("IOException occurred", ex);
                            } catch (URISyntaxException ex) {
                                LOG.error("URIException occurred ", ex);
                            }
                        }
                    } else if (e.getURL().getProtocol().startsWith("file")) {
                        LOG.debug("opening file link: " + e.getURL().getPath());
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
                            try {
                                Desktop.getDesktop().open(new File(e.getURL().getPath()));
                            } catch (IOException ex) {
                                LOG.error("IOException occurred", ex);
                            }
                        }
                    }
                }
            }
        });
        setEditable(false);
        setBorder(null);
    }

    static StringBuffer getStyle() {
        // for copying style
        JLabel label = new JLabel();
        Font font = label.getFont();
        Color color = label.getBackground();

        // create some css from the label's font
        StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
        style.append("font-weight:").append(font.isBold() ? "bold" : "normal").append(";");
        style.append("font-size:").append(font.getSize()).append("pt;");
        style.append("background-color: rgb(")
                .append(color.getRed())
                .append(",")
                .append(color.getGreen())
                .append(",")
                .append(color.getBlue())
                .append(");");
        return style;
    }

}
