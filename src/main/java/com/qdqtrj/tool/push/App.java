package com.qdqtrj.tool.push;

import com.qdqtrj.tool.push.ui.Init;
import com.qdqtrj.tool.push.ui.form.LoadingForm;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.ui.frame.MainFrame;
import com.qdqtrj.tool.push.util.ConfigUtil;
import com.qdqtrj.tool.push.util.MybatisUtil;
import com.qdqtrj.tool.push.util.UpgradeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * Main Enter!
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Slf4j
public class App {
    public static MainFrame mainFrame;

    public static ConfigUtil config = ConfigUtil.getInstance();

    public static SqlSession sqlSession = MybatisUtil.getSqlSession();

    public static SystemTray tray;

    public static TrayIcon trayIcon;

    public static void main(String[] args) {
        Init.initTheme();
        mainFrame = new MainFrame();
        mainFrame.init();
        JPanel loadingPanel = new LoadingForm().getLoadingPanel();
        mainFrame.add(loadingPanel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.getWidth() <= 1366) {
            // 低分辨率下自动最大化窗口
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        mainFrame.pack();
        mainFrame.setVisible(true);
        UpgradeUtil.smoothUpgrade();

        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Init.initGlobalFont();
        mainFrame.setContentPane(MainWindow.getInstance().getMainPanel());
        MainWindow.getInstance().init();
        Init.initAllTab();
        Init.initOthers();
        mainFrame.addListeners();
        mainFrame.remove(loadingPanel);
        Init.initFontSize();
        Init.initTray();
    }
}
