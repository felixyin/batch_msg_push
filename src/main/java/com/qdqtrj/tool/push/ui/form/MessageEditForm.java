package com.qdqtrj.tool.push.ui.form;

import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;
import com.qdqtrj.tool.push.ui.form.msg.*;
import com.qdqtrj.tool.push.util.UndoUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.qdqtrj.tool.push.ui.form.msg.*;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * MessageEditForm
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
public class MessageEditForm {
    private JPanel messageEditPanel;
    private JLabel msgNameLabel;
    private JTextField msgNameField;
    private JLabel previewMemberLabel;
    private JTextField previewUserField;
    private JButton previewMsgButton;
    private JButton msgSaveButton;
    private JPanel msgEditorPanel;
    private JLabel msgTypeName;
    private JLabel previewUserHelpLabel;
    private JScrollPane msgEditScrollPane;

    private static MessageEditForm messageEditForm;

    private MessageEditForm() {
        UndoUtil.register(this);
    }

    public static MessageEditForm getInstance() {
        if (messageEditForm == null) {
            messageEditForm = new MessageEditForm();
        }
        return messageEditForm;
    }

    /**
     * 初始化消息tab
     */
    public static void init(String selectedMsgName) {
        messageEditForm = getInstance();
        // 设置滚动条速度
        messageEditForm.getMsgEditScrollPane().getVerticalScrollBar().setUnitIncrement(15);
        messageEditForm.getMsgEditScrollPane().getVerticalScrollBar().setDoubleBuffered(true);

        String msgName;
        if (StringUtils.isEmpty(selectedMsgName)) {
            msgName = App.config.getMsgName();
        } else {
            msgName = selectedMsgName;
        }

        messageEditForm.getMsgNameField().setText(msgName);
        messageEditForm.getPreviewUserField().setText(App.config.getPreviewUser());

        MsgFormFactory.getMsgForm().init(msgName);
    }

    /**
     * 根据消息类型转换界面显示
     *
     * @param msgType
     */
    public static void switchMsgType(int msgType) {
        messageEditForm = getInstance();
        messageEditForm.getMsgEditorPanel().removeAll();
        GridConstraints gridConstraintsRow0 = new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false);
        GridConstraints gridConstraintsRow1 = new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false);

        MsgFormFactory.getMsgForm().init(null);
        switch (msgType) {
            case MessageTypeEnum.MP_TEMPLATE_CODE:
                messageEditForm.getMsgEditorPanel().add(MpTemplateMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.MA_TEMPLATE_CODE:
                messageEditForm.getMsgEditorPanel().add(MaTemplateMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.MA_SUBSCRIBE_CODE:
                messageEditForm.getMsgEditorPanel().add(MaSubscribeMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.KEFU_CODE:
                messageEditForm.getMsgEditorPanel().add(KefuMsgForm.getInstance().getKefuMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.KEFU_PRIORITY_CODE:
                messageEditForm.getMsgEditorPanel().setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
                messageEditForm.getMsgEditorPanel().add(KefuMsgForm.getInstance().getKefuMsgPanel(), gridConstraintsRow0);
                messageEditForm.getMsgEditorPanel().add(MpTemplateMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow1);
                break;
            case MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE:
                messageEditForm.getMsgEditorPanel().setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
                messageEditForm.getMsgEditorPanel().add(MaTemplateMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                messageEditForm.getMsgEditorPanel().add(MpTemplateMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow1);
                break;
            case MessageTypeEnum.ALI_YUN_CODE:
                messageEditForm.getMsgEditorPanel().add(AliYunMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.TX_YUN_CODE:
                messageEditForm.getMsgEditorPanel().add(TxYunMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.QI_NIU_YUN_CODE:
                messageEditForm.getMsgEditorPanel().add(QiNiuYunMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.UP_YUN_CODE:
                messageEditForm.getMsgEditorPanel().add(UpYunMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.HW_YUN_CODE:
                messageEditForm.getMsgEditorPanel().add(HwYunMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.YUN_PIAN_CODE:
                messageEditForm.getMsgEditorPanel().add(YunpianMsgForm.getInstance().getYunpianMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.EMAIL_CODE:
                messageEditForm.getMsgEditorPanel().add(MailMsgForm.getInstance().getMailPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.WX_CP_CODE:
                messageEditForm.getMsgEditorPanel().add(WxCpMsgForm.getInstance().getWxCpMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.HTTP_CODE:
                messageEditForm.getMsgEditorPanel().add(HttpMsgForm.getInstance().getHttpPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.DING_CODE:
                messageEditForm.getMsgEditorPanel().add(DingMsgForm.getInstance().getDingMsgPanel(), gridConstraintsRow0);
                break;
            case MessageTypeEnum.BD_YUN_CODE:
                messageEditForm.getMsgEditorPanel().add(BdYunMsgForm.getInstance().getTemplateMsgPanel(), gridConstraintsRow0);
                break;
            default:
                break;
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        messageEditPanel = new JPanel();
        messageEditPanel.setLayout(new GridLayoutManager(3, 1, new Insets(6, 2, 0, 5), -1, -1));
        messageEditPanel.setMaximumSize(new Dimension(-1, -1));
        messageEditPanel.setMinimumSize(new Dimension(-1, -1));
        messageEditPanel.setPreferredSize(new Dimension(-1, -1));
        panel1.add(messageEditPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 5, 10, 5), -1, -1));
        messageEditPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 10, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        msgTypeName = new JLabel();
        Font msgTypeNameFont = this.$$$getFont$$$(null, -1, 28, msgTypeName.getFont());
        if (msgTypeNameFont != null) msgTypeName.setFont(msgTypeNameFont);
        msgTypeName.setForeground(new Color(-276358));
        msgTypeName.setText("消息类型");
        panel3.add(msgTypeName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        msgNameLabel = new JLabel();
        Font msgNameLabelFont = this.$$$getFont$$$(null, Font.BOLD, -1, msgNameLabel.getFont());
        if (msgNameLabelFont != null) msgNameLabel.setFont(msgNameLabelFont);
        msgNameLabel.setText("消息名称 *");
        msgNameLabel.setToolTipText("给本次推送任务起个名字");
        panel2.add(msgNameLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        msgNameField = new JTextField();
        msgNameField.setToolTipText("给本次推送任务起个名字");
        panel2.add(msgNameField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 5, new Insets(0, 5, 5, 5), -1, -1));
        messageEditPanel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        previewMemberLabel = new JLabel();
        previewMemberLabel.setText("预览用户");
        previewMemberLabel.setToolTipText("多个以半角分号分隔");
        panel4.add(previewMemberLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        previewUserField = new JTextField();
        previewUserField.setToolTipText("多个以半角分号分隔");
        panel4.add(previewUserField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        previewMsgButton = new JButton();
        previewMsgButton.setIcon(new ImageIcon(getClass().getResource("/icon/send.png")));
        previewMsgButton.setText("预览");
        panel4.add(previewMsgButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        msgSaveButton = new JButton();
        msgSaveButton.setIcon(new ImageIcon(getClass().getResource("/icon/menu-saveall_dark.png")));
        msgSaveButton.setText("保存");
        panel4.add(msgSaveButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        previewUserHelpLabel = new JLabel();
        previewUserHelpLabel.setIcon(new ImageIcon(getClass().getResource("/icon/helpButton.png")));
        previewUserHelpLabel.setText("");
        panel4.add(previewUserHelpLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        msgEditScrollPane = new JScrollPane();
        messageEditPanel.add(msgEditScrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        msgEditorPanel = new JPanel();
        msgEditorPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, false));
        msgEditScrollPane.setViewportView(msgEditorPanel);
        msgNameLabel.setLabelFor(msgNameField);
        previewMemberLabel.setLabelFor(previewUserField);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

}
