package com.qdqtrj.tool.push.ui.listener;

import com.qdqtrj.tool.push.ui.form.HelpForm;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <pre>
 * HelpPanel Listener
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class HelpListener {

    public static void addListeners() {
        HelpForm.getInstance().getLabelOnlineHelp().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI("https://gitee.com/zhoubochina/BatchMsgPush/wikis/help"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
                super.mousePressed(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                HelpForm.getInstance().getLabelOnlineHelp().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

        });
    }
}
