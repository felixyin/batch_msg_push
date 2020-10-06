package com.qdqtrj.tool.push.ui.listener;

import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;
import com.qdqtrj.tool.push.ui.Consts;
import com.qdqtrj.tool.push.ui.dialog.CommonTipsDialog;
import com.qdqtrj.tool.push.ui.form.MessageEditForm;
import com.qdqtrj.tool.push.ui.form.MessageTypeForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <pre>
 * 消息类型form事件监听
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class MessageTypeListener {

    public static void addListeners() {
        MessageTypeForm messageTypeForm = MessageTypeForm.getInstance();
        messageTypeForm.getMpTemplateRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.MP_TEMPLATE_CODE);
            saveType();
        });
        messageTypeForm.getMaTemplateRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.MA_TEMPLATE_CODE);
            saveType();
        });
        messageTypeForm.getMaSubscribeRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.MA_SUBSCRIBE_CODE);
            saveType();
        });
        messageTypeForm.getKefuRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.KEFU_CODE);
            saveType();
        });
        messageTypeForm.getKefuPriorityRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.KEFU_PRIORITY_CODE);
            saveType();
        });
        messageTypeForm.getWxUniformMessageRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE);
            saveType();
        });
        messageTypeForm.getAliYunRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.ALI_YUN_CODE);
            saveType();
        });
        messageTypeForm.getTxYunRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.TX_YUN_CODE);
            saveType();
        });
        messageTypeForm.getQiniuRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.QI_NIU_YUN_CODE);
            saveType();
        });
        messageTypeForm.getBdYunRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.BD_YUN_CODE);
            saveType();
        });
        messageTypeForm.getYunPianRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.YUN_PIAN_CODE);
            saveType();
        });
        messageTypeForm.getUpYunRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.UP_YUN_CODE);
            saveType();
        });
        messageTypeForm.getHwYunRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.HW_YUN_CODE);
            saveType();
        });
        messageTypeForm.getEMailRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.EMAIL_CODE);
            saveType();
        });
        messageTypeForm.getWxCpRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.WX_CP_CODE);
            saveType();
        });
        messageTypeForm.getHttpRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.HTTP_CODE);
            saveType();
        });
        messageTypeForm.getDingRadioButton().addActionListener(e -> {
            App.config.setMsgType(MessageTypeEnum.DING_CODE);
            saveType();
        });

        messageTypeForm.getKefuPriorityTipsLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                CommonTipsDialog dialog = new CommonTipsDialog();

                StringBuilder tipsBuilder = new StringBuilder();
                tipsBuilder.append("<h1>这是什么？</h1>");
                tipsBuilder.append("<h2>优先发送客服消息，如果发送失败则再发送模板消息</h2>");
                tipsBuilder.append("<p>选择该消息类型需要同时编辑客服消息和模板消息</p>");
                tipsBuilder.append("<p>首先尝试发送客服消息，如果失败则继续给该用户发送模板消息</p>");

                dialog.setHtmlText(tipsBuilder.toString());
                dialog.pack();
                dialog.setVisible(true);

                super.mousePressed(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getComponent();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                label.setIcon(new ImageIcon(Consts.HELP_FOCUSED_ICON));
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JLabel label = (JLabel) e.getComponent();
                label.setIcon(new ImageIcon(Consts.HELP_ICON));
                super.mouseExited(e);
            }
        });
    }

    /**
     * 保存消息类型
     */
    private static void saveType() {
        String msgName = App.config.getMsgName();

        MessageEditForm.getInstance().getMsgNameField().setText(msgName);
        MessageEditForm.getInstance().getPreviewUserField().setText(App.config.getPreviewUser());
        App.config.save();
        MessageTypeForm.init();
    }
}
