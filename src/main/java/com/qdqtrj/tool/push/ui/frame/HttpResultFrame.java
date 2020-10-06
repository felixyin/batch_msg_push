package com.qdqtrj.tool.push.ui.frame;

import com.apple.eawt.Application;
import com.qdqtrj.tool.push.ui.Consts;
import com.qdqtrj.tool.push.ui.form.HttpResultForm;
import com.qdqtrj.tool.push.util.ComponentUtil;
import com.qdqtrj.tool.push.util.SystemUtil;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * <pre>
 * Http请求响应结果展示frame
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class HttpResultFrame extends JFrame {

    private static final long serialVersionUID = 5950950940687769444L;

    private static HttpResultFrame httpResultFrame;

    public void init() {
        String title = "Http请求结果";
        this.setName(title);
        this.setTitle(title);
        List<Image> images = Lists.newArrayList();
        images.add(Consts.IMAGE_LOGO_1024);
        images.add(Consts.IMAGE_LOGO_512);
        images.add(Consts.IMAGE_LOGO_256);
        images.add(Consts.IMAGE_LOGO_128);
        images.add(Consts.IMAGE_LOGO_64);
        images.add(Consts.IMAGE_LOGO_48);
        images.add(Consts.IMAGE_LOGO_32);
        images.add(Consts.IMAGE_LOGO_24);
        images.add(Consts.IMAGE_LOGO_16);
        this.setIconImages(images);
        // Mac系统Dock图标
        if (SystemUtil.isMacOs()) {
            Application application = Application.getApplication();
            application.setDockIconImage(Consts.IMAGE_LOGO_1024);
        }

        ComponentUtil.setPreferSizeAndLocateToCenter(this, 0.6, 0.66);
    }

    public static HttpResultFrame getInstance() {
        if (httpResultFrame == null) {
            httpResultFrame = new HttpResultFrame();
            httpResultFrame.init();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (screenSize.getWidth() <= 1366) {
                // 低分辨率下自动最大化窗口
                httpResultFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            httpResultFrame.setContentPane(HttpResultForm.getInstance().getHttpResultPanel());
            httpResultFrame.pack();
        }

        return httpResultFrame;
    }

    public static void showResultWindow() {
        getInstance().setVisible(true);
    }
}
