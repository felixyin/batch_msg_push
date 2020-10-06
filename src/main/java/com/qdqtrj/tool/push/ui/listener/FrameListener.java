package com.qdqtrj.tool.push.ui.listener;

import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.ui.form.PushForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static com.qdqtrj.tool.push.App.mainFrame;

/**
 * <pre>
 * 窗体事件监听
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class FrameListener {

    public static void addListeners() {
        mainFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (!PushForm.getInstance().getPushStartButton().isEnabled()) {
                    JOptionPane.showMessageDialog(MainWindow.getInstance().getPushPanel(),
                            "有推送任务正在进行！\n\n为避免数据丢失，请先停止!\n\n", "Sorry~",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    App.sqlSession.close();
                    mainFrame.dispose();
                    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }
        });

        MainWindow.getInstance().getMainPanel().registerKeyboardAction(e -> mainFrame.setExtendedState(Frame.ICONIFIED), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}
