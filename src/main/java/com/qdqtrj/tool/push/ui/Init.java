package com.qdqtrj.tool.push.ui;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.ui.dialog.FontSizeAdjustDialog;
import com.qdqtrj.tool.push.ui.form.AboutForm;
import com.qdqtrj.tool.push.ui.form.BoostForm;
import com.qdqtrj.tool.push.ui.form.HelpForm;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.ui.form.MemberForm;
import com.qdqtrj.tool.push.ui.form.MessageEditForm;
import com.qdqtrj.tool.push.ui.form.MessageManageForm;
import com.qdqtrj.tool.push.ui.form.MessageTypeForm;
import com.qdqtrj.tool.push.ui.form.PushForm;
import com.qdqtrj.tool.push.ui.form.PushHisForm;
import com.qdqtrj.tool.push.ui.form.ScheduleForm;
import com.qdqtrj.tool.push.ui.form.SettingForm;
import com.qdqtrj.tool.push.ui.listener.AboutListener;
import com.qdqtrj.tool.push.util.SystemUtil;
import com.qdqtrj.tool.push.util.UIUtil;
import com.qdqtrj.tool.push.util.UpgradeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * <pre>
 * 初始化类
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class Init {

    private static final Log logger = LogFactory.get();

    /**
     * 字号初始化KEY
     */
    private static final String FONT_SIZE_INIT_PROP = "fontSizeInit";

    /**
     * 设置全局字体
     */
    public static void initGlobalFont() {
        if (StringUtils.isEmpty(App.config.getProps(FONT_SIZE_INIT_PROP))) {
            // 根据DPI调整字号
            // 得到屏幕的分辨率dpi
            // dell 1920*1080/24寸=96
            // 小米air 1920*1080/13.3寸=144
            // 小米air 1366*768/13.3寸=96
            int fontSize = 12;

            // Mac等高分辨率屏幕字号初始化
            if (SystemUtil.isMacOs()) {
                fontSize = 15;
            } else {
                fontSize = (int) (UIUtil.getScreenScale() * fontSize);
            }
            App.config.setFontSize(fontSize);
        }

        Font font = new Font(App.config.getFont(), Font.PLAIN, App.config.getFontSize());
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }

    }

    /**
     * 其他初始化
     */
    public static void initOthers() {
        // 设置版本
        AboutForm.getInstance().getVersionLabel().setText(Consts.APP_VERSION);
    }

    /**
     * 初始化look and feel
     */
    public static void initTheme() {

        try {
            switch (App.config.getTheme()) {
                case "BeautyEye":
                    BeautyEyeLNFHelper.launchBeautyEyeLNF();
                    UIManager.put("RootPane.setupButtonVisible", false);
                    break;
                case "系统默认":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case "weblaf":
                case "Darcula(推荐)":
                default:
                    UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * 初始化所有tab
     */
    public static void initAllTab() {
        ThreadUtil.execute(AboutForm::init);
        MessageTypeForm.init();
        ThreadUtil.execute(HelpForm::init);
        ThreadUtil.execute(() -> MessageEditForm.init(null));
        ThreadUtil.execute(MessageManageForm::init);
        ThreadUtil.execute(MemberForm::init);
        ThreadUtil.execute(PushForm::init);
        ThreadUtil.execute(BoostForm::init);
        ThreadUtil.execute(ScheduleForm::init);
        ThreadUtil.execute(SettingForm::init);
        ThreadUtil.execute(PushHisForm::init);

        // 检查新版版
        if (App.config.isAutoCheckUpdate()) {
            ThreadUtil.execute(() -> UpgradeUtil.checkUpdate(true));
        }
        // 更新二维码
        ThreadUtil.execute(AboutListener::initQrCode);
    }

    /**
     * 引导用户调整字号
     */
    public static void initFontSize() {
        if (StringUtils.isEmpty(App.config.getProps(FONT_SIZE_INIT_PROP))) {
            FontSizeAdjustDialog fontSizeAdjustDialog = new FontSizeAdjustDialog();
            fontSizeAdjustDialog.pack();
            fontSizeAdjustDialog.setVisible(true);
        }

        App.config.setProps(FONT_SIZE_INIT_PROP, "true");
        App.config.save();
    }

    /**
     * 初始化系统托盘
     */
    public static void initTray() {

        try {
            if (App.config.isUseTray() && SystemTray.isSupported()) {
                App.tray = SystemTray.getSystemTray();

                PopupMenu popupMenu = new PopupMenu();
                popupMenu.setFont(App.mainFrame.getContentPane().getFont());

                MenuItem openItem = new MenuItem("BatchMsgPush");
                MenuItem exitItem = new MenuItem("退出");

                openItem.addActionListener(e -> {
                    App.mainFrame.setExtendedState(JFrame.NORMAL);
                    App.mainFrame.setVisible(true);
                    App.mainFrame.requestFocus();
                });
                exitItem.addActionListener(e -> {
                    if (!PushForm.getInstance().getPushStartButton().isEnabled()) {
                        JOptionPane.showMessageDialog(MainWindow.getInstance().getPushPanel(),
                                "有推送任务正在进行！\n\n为避免数据丢失，请先停止!\n\n", "Sorry~",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        App.config.save();
                        App.sqlSession.close();
                        App.mainFrame.dispose();
                        System.exit(0);
                    }
                });

                popupMenu.add(openItem);
                popupMenu.add(exitItem);

                App.trayIcon = new TrayIcon(Consts.IMAGE_LOGO_64, "BatchMsgPush", popupMenu);
                App.trayIcon.setImageAutoSize(true);

                App.trayIcon.addActionListener(e -> {
                    App.mainFrame.setExtendedState(JFrame.NORMAL);
                    App.mainFrame.setVisible(true);
                    App.mainFrame.requestFocus();
                });
                App.trayIcon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        switch (e.getButton()) {
                            case MouseEvent.BUTTON1: {
                                App.mainFrame.setExtendedState(JFrame.NORMAL);
                                App.mainFrame.setVisible(true);
                                App.mainFrame.requestFocus();
                                break;
                            }
                            case MouseEvent.BUTTON2: {
                                logger.debug("托盘图标被鼠标中键被点击");
                                break;
                            }
                            case MouseEvent.BUTTON3: {
                                logger.debug("托盘图标被鼠标右键被点击");
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                });

                try {
                    App.tray.add(App.trayIcon);
                } catch (AWTException e) {
                    e.printStackTrace();
                    logger.error(ExceptionUtils.getStackTrace(e));
                }

            }

        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
