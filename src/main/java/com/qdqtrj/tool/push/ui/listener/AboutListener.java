package com.qdqtrj.tool.push.ui.listener;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.qdqtrj.tool.push.ui.form.AboutForm;
import com.qdqtrj.tool.push.util.UpgradeUtil;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <pre>
 * AboutPanel Listener
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class AboutListener {
    private static final Log logger = LogFactory.get();

    public static void addListeners() {
        AboutForm aboutForm = AboutForm.getInstance();
        aboutForm.getPowerby().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI("http://www.qdqtrj.com"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

        });

        // 检查更新
        aboutForm.getCheckUpdateLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                ThreadUtil.execute(() -> UpgradeUtil.checkUpdate(false));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        // 帮助文档
        aboutForm.getHelpDocLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI("https://github.com/rememberber/BatchMsgPush/wiki"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

    /**
     * 初始化二维码
     */
    public static void initQrCode() {
//        String qrCodeContent = HttpUtil.get(Consts.QR_CODE_URL);
//        if (StringUtils.isNotEmpty(qrCodeContent)) {
//            Map<String, String> urlMap = JSONUtil.toBean(qrCodeContent, Map.class);
////            JLabel qrCodeLabel = AboutForm.getInstance().getQrCodeLabel();
//
//            try {
//                URL url = new URL(urlMap.get("url"));
//                BufferedImage image = ImageIO.read(url);
//                qrCodeLabel.setIcon(new ImageIcon(image));
//            } catch (IOException e) {
//                e.printStackTrace();
//                logger.error(e);
//            }
//
//            MainWindow.getInstance().getAboutPanel().updateUI();
//        }
    }
}
