package com.qdqtrj.tool.push.ui.listener;

import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;
import com.qdqtrj.tool.push.ui.form.AboutForm;
import com.qdqtrj.tool.push.ui.form.BoostForm;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.ui.form.MessageEditForm;
import com.qdqtrj.tool.push.ui.form.PushForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <pre>
 * tab事件监听
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class TabListener {

    private static boolean warnFlag = true;

    public static void addListeners() {
        MainWindow.getInstance().getTabbedPane().addChangeListener(new ChangeListener() {
            /**
             * Invoked when the target of the listener has changed its state.
             *
             * @param e a ChangeEvent object
             */
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = MainWindow.getInstance().getTabbedPane().getSelectedIndex();
                int msgType = App.config.getMsgType();
                switch (index) {
                    case 0:
                        AboutForm.init();
                        break;
                    case 3:
                        if (warnFlag && msgType != MessageTypeEnum.EMAIL_CODE && msgType != MessageTypeEnum.HTTP_CODE) {
                            JOptionPane.showMessageDialog(MainWindow.getInstance().getSettingPanel(), "\n请确认您了解所要发送消息类型的使用频率、使用规范和限制规则，\n" +
                                            "以免账号相关功能被封禁等给您带来麻烦\n", "提示",
                                    JOptionPane.INFORMATION_MESSAGE);
                            warnFlag = false;
                        }
                        break;
                    case 4:
                        PushForm.getInstance().getPushMsgName().setText(MessageEditForm.getInstance().getMsgNameField().getText());
                        PushListener.refreshPushInfo();
                        break;
                    case 5:
                        BoostForm.getInstance().getMsgNameLabel().setText(MessageEditForm.getInstance().getMsgNameField().getText());
                        BoostListener.refreshPushInfo();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
