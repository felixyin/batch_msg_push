package com.qdqtrj.tool.push.ui.form;

import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.dao.TMsgDingMapper;
import com.qdqtrj.tool.push.dao.TMsgHttpMapper;
import com.qdqtrj.tool.push.dao.TMsgKefuMapper;
import com.qdqtrj.tool.push.dao.TMsgKefuPriorityMapper;
import com.qdqtrj.tool.push.dao.TMsgMaSubscribeMapper;
import com.qdqtrj.tool.push.dao.TMsgMaTemplateMapper;
import com.qdqtrj.tool.push.dao.TMsgMailMapper;
import com.qdqtrj.tool.push.dao.TMsgMpTemplateMapper;
import com.qdqtrj.tool.push.dao.TMsgSmsMapper;
import com.qdqtrj.tool.push.dao.TMsgWxCpMapper;
import com.qdqtrj.tool.push.dao.TMsgWxUniformMapper;
import com.qdqtrj.tool.push.domain.TMsgDing;
import com.qdqtrj.tool.push.domain.TMsgHttp;
import com.qdqtrj.tool.push.domain.TMsgKefu;
import com.qdqtrj.tool.push.domain.TMsgKefuPriority;
import com.qdqtrj.tool.push.domain.TMsgMaSubscribe;
import com.qdqtrj.tool.push.domain.TMsgMaTemplate;
import com.qdqtrj.tool.push.domain.TMsgMail;
import com.qdqtrj.tool.push.domain.TMsgMpTemplate;
import com.qdqtrj.tool.push.domain.TMsgSms;
import com.qdqtrj.tool.push.domain.TMsgWxCp;
import com.qdqtrj.tool.push.domain.TMsgWxUniform;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;
import com.qdqtrj.tool.push.util.JTableUtil;
import com.qdqtrj.tool.push.util.MybatisUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * <pre>
 * MessageManageForm
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
public class MessageManageForm {

    private JPanel messageManagePanel;
    private JTable msgHistable;
    private JButton msgHisTableDeleteButton;
    private JButton createMsgButton;

    private static MessageManageForm messageManageForm;

    private static TMsgKefuMapper msgKefuMapper = MybatisUtil.getSqlSession().getMapper(TMsgKefuMapper.class);
    private static TMsgKefuPriorityMapper msgKefuPriorityMapper = MybatisUtil.getSqlSession().getMapper(TMsgKefuPriorityMapper.class);
    private static TMsgWxUniformMapper wxUniformMapper = MybatisUtil.getSqlSession().getMapper(TMsgWxUniformMapper.class);
    private static TMsgMaTemplateMapper msgMaTemplateMapper = MybatisUtil.getSqlSession().getMapper(TMsgMaTemplateMapper.class);
    private static TMsgMaSubscribeMapper msgMaSubscribeMapper = MybatisUtil.getSqlSession().getMapper(TMsgMaSubscribeMapper.class);
    private static TMsgMpTemplateMapper msgMpTemplateMapper = MybatisUtil.getSqlSession().getMapper(TMsgMpTemplateMapper.class);
    private static TMsgSmsMapper msgSmsMapper = MybatisUtil.getSqlSession().getMapper(TMsgSmsMapper.class);
    private static TMsgMailMapper msgMailMapper = MybatisUtil.getSqlSession().getMapper(TMsgMailMapper.class);
    private static TMsgWxCpMapper msgWxCpMapper = MybatisUtil.getSqlSession().getMapper(TMsgWxCpMapper.class);
    private static TMsgHttpMapper msgHttpMapper = MybatisUtil.getSqlSession().getMapper(TMsgHttpMapper.class);
    private static TMsgDingMapper msgDingMapper = MybatisUtil.getSqlSession().getMapper(TMsgDingMapper.class);

    private MessageManageForm() {
    }

    public static MessageManageForm getInstance() {
        if (messageManageForm == null) {
            messageManageForm = new MessageManageForm();
        }
        return messageManageForm;
    }

    /**
     * 初始化消息列表
     */
    public static void init() {
        messageManageForm = getInstance();
        // 历史消息管理
        String[] headerNames = {"消息名称"};
        DefaultTableModel model = new DefaultTableModel(null, headerNames);
        messageManageForm.getMsgHistable().setModel(model);
        // 隐藏表头
        JTableUtil.hideTableHeader(messageManageForm.getMsgHistable());

        int msgType = App.config.getMsgType();

        Object[] data;

        if (msgType == MessageTypeEnum.KEFU_CODE) {
            List<TMsgKefu> tMsgKefuList = msgKefuMapper.selectByMsgType(msgType);
            for (TMsgKefu tMsgKefu : tMsgKefuList) {
                data = new Object[1];
                data[0] = tMsgKefu.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.KEFU_PRIORITY_CODE) {
            List<TMsgKefuPriority> tMsgKefuPriorityList = msgKefuPriorityMapper.selectByMsgType(msgType);
            for (TMsgKefuPriority tMsgKefuPriority : tMsgKefuPriorityList) {
                data = new Object[1];
                data[0] = tMsgKefuPriority.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE) {
            List<TMsgWxUniform> tMsgWxUniformList = wxUniformMapper.selectByMsgType(msgType);
            for (TMsgWxUniform tMsgWxUniform : tMsgWxUniformList) {
                data = new Object[1];
                data[0] = tMsgWxUniform.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.MA_TEMPLATE_CODE) {
            List<TMsgMaTemplate> tMsgMaTemplateList = msgMaTemplateMapper.selectByMsgType(msgType);
            for (TMsgMaTemplate tMsgMaTemplate : tMsgMaTemplateList) {
                data = new Object[1];
                data[0] = tMsgMaTemplate.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.MA_SUBSCRIBE_CODE) {
            List<TMsgMaSubscribe> tMsgMaSubscribeList = msgMaSubscribeMapper.selectByMsgType(msgType);
            for (TMsgMaSubscribe tMsgMaSubscribe : tMsgMaSubscribeList) {
                data = new Object[1];
                data[0] = tMsgMaSubscribe.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.MP_TEMPLATE_CODE) {
            List<TMsgMpTemplate> tMsgMpTemplateList = msgMpTemplateMapper.selectByMsgType(msgType);
            for (TMsgMpTemplate tMsgMpTemplate : tMsgMpTemplateList) {
                data = new Object[1];
                data[0] = tMsgMpTemplate.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.EMAIL_CODE) {
            List<TMsgMail> tMsgMailList = msgMailMapper.selectByMsgType(msgType);
            for (TMsgMail tMsgMail : tMsgMailList) {
                data = new Object[1];
                data[0] = tMsgMail.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.WX_CP_CODE) {
            List<TMsgWxCp> tMsgWxCpList = msgWxCpMapper.selectByMsgType(msgType);
            for (TMsgWxCp tMsgWxCp : tMsgWxCpList) {
                data = new Object[1];
                data[0] = tMsgWxCp.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.HTTP_CODE) {
            List<TMsgHttp> tMsgHttpList = msgHttpMapper.selectByMsgType(msgType);
            for (TMsgHttp tMsgHttp : tMsgHttpList) {
                data = new Object[1];
                data[0] = tMsgHttp.getMsgName();
                model.addRow(data);
            }
        } else if (msgType == MessageTypeEnum.DING_CODE) {
            List<TMsgDing> tMsgDingList = msgDingMapper.selectByMsgType(msgType);
            for (TMsgDing tMsgDing : tMsgDingList) {
                data = new Object[1];
                data[0] = tMsgDing.getMsgName();
                model.addRow(data);
            }
        } else {
            List<TMsgSms> tMsgSmsList = msgSmsMapper.selectByMsgType(msgType);
            for (TMsgSms tMsgSms : tMsgSmsList) {
                data = new Object[1];
                data[0] = tMsgSms.getMsgName();
                model.addRow(data);
            }
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
        messageManagePanel = new JPanel();
        messageManagePanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        messageManagePanel.setMaximumSize(new Dimension(-1, -1));
        messageManagePanel.setMinimumSize(new Dimension(-1, -1));
        messageManagePanel.setPreferredSize(new Dimension(280, -1));
        panel1.add(messageManagePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        messageManagePanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        msgHistable = new JTable();
        msgHistable.setGridColor(new Color(-12236470));
        msgHistable.setRowHeight(36);
        msgHistable.setShowVerticalLines(false);
        scrollPane1.setViewportView(msgHistable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 5, 5, 0), -1, -1));
        messageManagePanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        msgHisTableDeleteButton = new JButton();
        msgHisTableDeleteButton.setIcon(new ImageIcon(getClass().getResource("/icon/remove.png")));
        msgHisTableDeleteButton.setText("删除");
        panel2.add(msgHisTableDeleteButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        createMsgButton = new JButton();
        createMsgButton.setEnabled(true);
        createMsgButton.setIcon(new ImageIcon(getClass().getResource("/icon/add.png")));
        createMsgButton.setText("新建");
        panel2.add(createMsgButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}
