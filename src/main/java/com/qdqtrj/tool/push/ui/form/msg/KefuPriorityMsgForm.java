package com.qdqtrj.tool.push.ui.form.msg;

import com.qdqtrj.tool.push.dao.TMsgKefuPriorityMapper;
import com.qdqtrj.tool.push.dao.TTemplateDataMapper;
import com.qdqtrj.tool.push.domain.TMsgKefuPriority;
import com.qdqtrj.tool.push.domain.TTemplateData;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;
import com.qdqtrj.tool.push.ui.component.TableInCellButtonColumn;
import com.qdqtrj.tool.push.ui.form.MainWindow;
import com.qdqtrj.tool.push.util.MybatisUtil;
import com.qdqtrj.tool.push.util.SqliteUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * 客服消息优先(伪form)
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class KefuPriorityMsgForm implements IMsgForm {

    private static TMsgKefuPriorityMapper msgKefuPriorityMapper = MybatisUtil.getSqlSession().getMapper(TMsgKefuPriorityMapper.class);
    private static TTemplateDataMapper templateDataMapper = MybatisUtil.getSqlSession().getMapper(TTemplateDataMapper.class);

    private static KefuPriorityMsgForm kefuPriorityMsgForm;

    @Override
    public void init(String msgName) {
        clearAllField();
        List<TMsgKefuPriority> tMsgKefuPriorityList = msgKefuPriorityMapper.selectByMsgTypeAndMsgName(MessageTypeEnum.KEFU_PRIORITY_CODE, msgName);
        if (tMsgKefuPriorityList.size() > 0) {
            TMsgKefuPriority tMsgKefuPriority = tMsgKefuPriorityList.get(0);
            Integer msgId = tMsgKefuPriority.getId();
            MpTemplateMsgForm.getInstance().getMsgTemplateIdTextField().setText(tMsgKefuPriority.getTemplateId());
            MpTemplateMsgForm.getInstance().getMsgTemplateUrlTextField().setText(tMsgKefuPriority.getUrl());
            MpTemplateMsgForm.getInstance().getMsgTemplateMiniAppidTextField().setText(tMsgKefuPriority.getMaAppid());
            MpTemplateMsgForm.getInstance().getMsgTemplateMiniPagePathTextField().setText(tMsgKefuPriority.getMaPagePath());

            String kefuMsgType = tMsgKefuPriority.getKefuMsgType();
            KefuMsgForm.getInstance().getMsgKefuMsgTypeComboBox().setSelectedItem(kefuMsgType);
            if ("文本消息".equals(kefuMsgType)) {
                KefuMsgForm.getInstance().getContentTextArea().setText(tMsgKefuPriority.getContent());
            } else if ("图文消息".equals(kefuMsgType)) {
                KefuMsgForm.getInstance().getMsgKefuMsgTitleTextField().setText(tMsgKefuPriority.getTitle());
            }
            KefuMsgForm.getInstance().getMsgKefuPicUrlTextField().setText(tMsgKefuPriority.getImgUrl());
            KefuMsgForm.getInstance().getMsgKefuDescTextField().setText(tMsgKefuPriority.getDescribe());
            KefuMsgForm.getInstance().getMsgKefuUrlTextField().setText(tMsgKefuPriority.getKefuUrl());

            KefuMsgForm.switchKefuMsgType(kefuMsgType);

            MpTemplateMsgForm.selectedMsgTemplateId = tMsgKefuPriority.getTemplateId();
            // 模板消息Data表
            List<TTemplateData> templateDataList = templateDataMapper.selectByMsgTypeAndMsgId(MessageTypeEnum.KEFU_PRIORITY_CODE, msgId);
            String[] headerNames = {"Name", "Value", "Color", "操作"};
            Object[][] cellData = new String[templateDataList.size()][headerNames.length];
            for (int i = 0; i < templateDataList.size(); i++) {
                TTemplateData tTemplateData = templateDataList.get(i);
                cellData[i][0] = tTemplateData.getName();
                cellData[i][1] = tTemplateData.getValue();
                cellData[i][2] = tTemplateData.getColor();
            }
            DefaultTableModel model = new DefaultTableModel(cellData, headerNames);
            MpTemplateMsgForm.getInstance().getTemplateMsgDataTable().setModel(model);
            TableColumnModel tableColumnModel = MpTemplateMsgForm.getInstance().getTemplateMsgDataTable().getColumnModel();
            tableColumnModel.getColumn(headerNames.length - 1).
                    setCellRenderer(new TableInCellButtonColumn(MpTemplateMsgForm.getInstance().getTemplateMsgDataTable(), headerNames.length - 1));
            tableColumnModel.getColumn(headerNames.length - 1).
                    setCellEditor(new TableInCellButtonColumn(MpTemplateMsgForm.getInstance().getTemplateMsgDataTable(), headerNames.length - 1));

            // 设置列宽
            tableColumnModel.getColumn(3).setPreferredWidth(46);
            tableColumnModel.getColumn(3).setMaxWidth(46);

            MpTemplateMsgForm.getInstance().getTemplateMsgDataTable().updateUI();
        } else {
            KefuMsgForm.switchKefuMsgType("图文消息");
        }
        MpTemplateMsgForm.initTemplateList();
    }

    @Override
    public void save(String msgName) {
        int msgId = 0;
        boolean existSameMsg = false;

        List<TMsgKefuPriority> tMsgKefuPriorityList = msgKefuPriorityMapper.selectByMsgTypeAndMsgName(MessageTypeEnum.KEFU_PRIORITY_CODE, msgName);
        if (tMsgKefuPriorityList.size() > 0) {
            existSameMsg = true;
            msgId = tMsgKefuPriorityList.get(0).getId();
        }

        int isCover = JOptionPane.NO_OPTION;
        if (existSameMsg) {
            // 如果存在，是否覆盖
            isCover = JOptionPane.showConfirmDialog(MainWindow.getInstance().getMessagePanel(), "已经存在同名的历史消息，\n是否覆盖？", "确认",
                    JOptionPane.YES_NO_OPTION);
        }

        if (!existSameMsg || isCover == JOptionPane.YES_OPTION) {
            String templateId = MpTemplateMsgForm.getInstance().getMsgTemplateIdTextField().getText();
            String templateUrl = MpTemplateMsgForm.getInstance().getMsgTemplateUrlTextField().getText();
            String kefuMsgType = Objects.requireNonNull(KefuMsgForm.getInstance().getMsgKefuMsgTypeComboBox().getSelectedItem()).toString();
            String kefuMsgContent = KefuMsgForm.getInstance().getContentTextArea().getText();
            String kefuMsgTitle = KefuMsgForm.getInstance().getMsgKefuMsgTitleTextField().getText();
            String kefuPicUrl = KefuMsgForm.getInstance().getMsgKefuPicUrlTextField().getText();
            String kefuDesc = KefuMsgForm.getInstance().getMsgKefuDescTextField().getText();
            String kefuUrl = KefuMsgForm.getInstance().getMsgKefuUrlTextField().getText();
            String templateMiniAppid = MpTemplateMsgForm.getInstance().getMsgTemplateMiniAppidTextField().getText();
            String templateMiniPagePath = MpTemplateMsgForm.getInstance().getMsgTemplateMiniPagePathTextField().getText();

            String now = SqliteUtil.nowDateForSqlite();

            TMsgKefuPriority tMsgKefuPriority = new TMsgKefuPriority();
            tMsgKefuPriority.setMsgType(MessageTypeEnum.KEFU_PRIORITY_CODE);
            tMsgKefuPriority.setMsgName(msgName);
            tMsgKefuPriority.setTemplateId(templateId);
            tMsgKefuPriority.setUrl(templateUrl);
            tMsgKefuPriority.setMaAppid(templateMiniAppid);
            tMsgKefuPriority.setMaPagePath(templateMiniPagePath);
            tMsgKefuPriority.setKefuMsgType(kefuMsgType);
            tMsgKefuPriority.setContent(kefuMsgContent);
            tMsgKefuPriority.setTitle(kefuMsgTitle);
            tMsgKefuPriority.setImgUrl(kefuPicUrl);
            tMsgKefuPriority.setDescribe(kefuDesc);
            tMsgKefuPriority.setKefuUrl(kefuUrl);
            tMsgKefuPriority.setCreateTime(now);
            tMsgKefuPriority.setModifiedTime(now);

            if (existSameMsg) {
                msgKefuPriorityMapper.updateByMsgTypeAndMsgName(tMsgKefuPriority);
            } else {
                msgKefuPriorityMapper.insertSelective(tMsgKefuPriority);
                msgId = tMsgKefuPriority.getId();
            }

            // 保存模板数据
            // 如果是覆盖保存，则先清空之前的模板数据
            if (existSameMsg) {
                templateDataMapper.deleteByMsgTypeAndMsgId(MessageTypeEnum.KEFU_PRIORITY_CODE, msgId);
            }

            // 如果table为空，则初始化
            if (MpTemplateMsgForm.getInstance().getTemplateMsgDataTable().getModel().getRowCount() == 0) {
                MpTemplateMsgForm.initTemplateDataTable();
            }

            // 逐行读取
            DefaultTableModel tableModel = (DefaultTableModel) MpTemplateMsgForm.getInstance().getTemplateMsgDataTable()
                    .getModel();
            int rowCount = tableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                String name = (String) tableModel.getValueAt(i, 0);
                String value = (String) tableModel.getValueAt(i, 1);
                String color = ((String) tableModel.getValueAt(i, 2)).trim();

                TTemplateData tTemplateData = new TTemplateData();
                tTemplateData.setMsgType(MessageTypeEnum.KEFU_PRIORITY_CODE);
                tTemplateData.setMsgId(msgId);
                tTemplateData.setName(name);
                tTemplateData.setValue(value);
                tTemplateData.setColor(color);
                tTemplateData.setCreateTime(now);
                tTemplateData.setModifiedTime(now);

                templateDataMapper.insert(tTemplateData);
            }

            JOptionPane.showMessageDialog(MainWindow.getInstance().getMessagePanel(), "保存成功！", "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static KefuPriorityMsgForm getInstance() {
        if (kefuPriorityMsgForm == null) {
            kefuPriorityMsgForm = new KefuPriorityMsgForm();
        }
        return kefuPriorityMsgForm;
    }

    /**
     * 清空所有界面字段
     */
    public static void clearAllField() {
        KefuMsgForm.clearAllField();
        MpTemplateMsgForm.clearAllField();
    }
}
