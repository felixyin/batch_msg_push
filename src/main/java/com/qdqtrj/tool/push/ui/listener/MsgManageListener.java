package com.qdqtrj.tool.push.ui.listener;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
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
import com.qdqtrj.tool.push.logic.MessageTypeEnum;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.ui.form.MessageEditForm;
import com.qdqtrj.tool.push.ui.form.MessageManageForm;
import com.qdqtrj.tool.push.ui.form.MessageTypeForm;
import com.qdqtrj.tool.push.ui.form.PushHisForm;
import com.qdqtrj.tool.push.util.MybatisUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <pre>
 * 编辑消息tab相关事件监听
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class MsgManageListener {
    private static final Log logger = LogFactory.get();

    private static TMsgKefuMapper msgKefuMapper = MybatisUtil.getSqlSession().getMapper(TMsgKefuMapper.class);
    private static TMsgKefuPriorityMapper msgKefuPriorityMapper = MybatisUtil.getSqlSession().getMapper(TMsgKefuPriorityMapper.class);
    private static TMsgWxUniformMapper wxUniformMapper = MybatisUtil.getSqlSession().getMapper(TMsgWxUniformMapper.class);
    private static TMsgMaTemplateMapper msgMaTemplateMapper = MybatisUtil.getSqlSession().getMapper(TMsgMaTemplateMapper.class);
    private static TMsgMaSubscribeMapper msgMaSubscribeMapper = MybatisUtil.getSqlSession().getMapper(TMsgMaSubscribeMapper.class);
    private static TMsgMpTemplateMapper msgMpTemplateMapper = MybatisUtil.getSqlSession().getMapper(TMsgMpTemplateMapper.class);
    private static TMsgSmsMapper msgSmsMapper = MybatisUtil.getSqlSession().getMapper(TMsgSmsMapper.class);
    private static TMsgMailMapper msgMailMapper = MybatisUtil.getSqlSession().getMapper(TMsgMailMapper.class);
    private static TMsgHttpMapper msgHttpMapper = MybatisUtil.getSqlSession().getMapper(TMsgHttpMapper.class);
    private static TMsgWxCpMapper msgWxCpMapper = MybatisUtil.getSqlSession().getMapper(TMsgWxCpMapper.class);
    private static TMsgDingMapper msgDingMapper = MybatisUtil.getSqlSession().getMapper(TMsgDingMapper.class);

    public static void addListeners() {
        JTable msgHistable = MessageManageForm.getInstance().getMsgHistable();
        JSplitPane messagePanel = MainWindow.getInstance().getMessagePanel();

        // 点击左侧表格事件
        msgHistable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ThreadUtil.execute(() -> {
                    PushHisForm.getInstance().getPushHisTextArea().setText("");

                    int selectedRow = msgHistable.getSelectedRow();
                    String selectedMsgName = msgHistable
                            .getValueAt(selectedRow, 0).toString();

                    MessageEditForm.init(selectedMsgName);
                });
                super.mousePressed(e);
            }
        });

        // 历史消息管理-删除
        MessageManageForm.getInstance().getMsgHisTableDeleteButton().addActionListener(e -> ThreadUtil.execute(() -> {
            try {
                int[] selectedRows = msgHistable.getSelectedRows();

                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(messagePanel, "请至少选择一个！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int isDelete = JOptionPane.showConfirmDialog(messagePanel, "确认删除？", "确认",
                            JOptionPane.YES_NO_OPTION);
                    if (isDelete == JOptionPane.YES_OPTION) {
                        DefaultTableModel tableModel = (DefaultTableModel) msgHistable
                                .getModel();
                        int msgType = App.config.getMsgType();

                        for (int i = selectedRows.length; i > 0; i--) {
                            int selectedRow = msgHistable.getSelectedRow();
                            String msgName = (String) tableModel.getValueAt(selectedRow, 0);
                            if (msgType == MessageTypeEnum.KEFU_CODE) {
                                msgKefuMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.KEFU_PRIORITY_CODE) {
                                msgKefuPriorityMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE) {
                                wxUniformMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.MA_TEMPLATE_CODE) {
                                msgMaTemplateMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.MA_SUBSCRIBE_CODE) {
                                msgMaSubscribeMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.MP_TEMPLATE_CODE) {
                                msgMpTemplateMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.EMAIL_CODE) {
                                msgMailMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.HTTP_CODE) {
                                msgHttpMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.WX_CP_CODE) {
                                msgWxCpMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else if (msgType == MessageTypeEnum.DING_CODE) {
                                msgDingMapper.deleteByMsgTypeAndName(msgType, msgName);
                            } else {
                                msgSmsMapper.deleteByMsgTypeAndName(msgType, msgName);
                            }

                            tableModel.removeRow(selectedRow);
                        }
                        MessageEditForm.init(null);
                    }
                }
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(messagePanel, "删除失败！\n\n" + e1.getMessage(), "失败",
                        JOptionPane.ERROR_MESSAGE);
                logger.error(e1);
            }
        }));

        // 编辑消息-新建
        MessageManageForm.getInstance().getCreateMsgButton().addActionListener(e -> {
            MessageTypeForm.init();
            MessageEditForm.getInstance().getMsgNameField().setText("");
            MessageEditForm.getInstance().getMsgNameField().grabFocus();
        });
    }
}