package com.qdqtrj.tool.push.logic.msgmaker;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import com.qdqtrj.tool.push.bean.TemplateData;
import com.qdqtrj.tool.push.ui.form.msg.MaTemplateMsgForm;
import com.qdqtrj.tool.push.util.TemplateUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.velocity.VelocityContext;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * <pre>
 * 小程序模板消息加工器
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class WxMaTemplateMsgMaker extends BaseMsgMaker implements IMsgMaker {

    public static String templateId;
    private static String templateUrl;
    private static String templateKeyWord;
    public static List<TemplateData> templateDataList;

    /**
     * 准备(界面字段等)
     */
    @Override
    public void prepare() {
        templateId = MaTemplateMsgForm.getInstance().getMsgTemplateIdTextField().getText().trim();
        templateUrl = MaTemplateMsgForm.getInstance().getMsgTemplateUrlTextField().getText().trim();
        templateKeyWord = MaTemplateMsgForm.getInstance().getMsgTemplateKeyWordTextField().getText().trim() + ".DATA";

        if (MaTemplateMsgForm.getInstance().getTemplateMsgDataTable().getModel().getRowCount() == 0) {
            MaTemplateMsgForm.initTemplateDataTable();
        }

        DefaultTableModel tableModel = (DefaultTableModel) MaTemplateMsgForm.getInstance().getTemplateMsgDataTable().getModel();
        int rowCount = tableModel.getRowCount();
        TemplateData templateData;
        templateDataList = Lists.newArrayList();
        for (int i = 0; i < rowCount; i++) {
            String name = ((String) tableModel.getValueAt(i, 0)).trim();
            String value = ((String) tableModel.getValueAt(i, 1)).trim();
            String color = ((String) tableModel.getValueAt(i, 2)).trim();
            templateData = new TemplateData();
            templateData.setName(name);
            templateData.setValue(value);
            templateData.setColor(color);
            templateDataList.add(templateData);
        }
    }

    /**
     * 组织模板消息-小程序
     *
     * @param msgData 消息信息
     * @return WxMaTemplateMessage
     */
    @Override
    public WxMaTemplateMessage makeMsg(String[] msgData) {
        // 拼模板
        WxMaTemplateMessage wxMessageTemplate = new WxMaTemplateMessage();
        wxMessageTemplate.setTemplateId(templateId);
        VelocityContext velocityContext = getVelocityContext(msgData);
        String templateUrlEvaluated = TemplateUtil.evaluate(templateUrl, velocityContext);
        wxMessageTemplate.setPage(templateUrlEvaluated);
        wxMessageTemplate.setEmphasisKeyword(templateKeyWord);

        WxMaTemplateData wxMaTemplateData;
        for (TemplateData templateData : templateDataList) {
            wxMaTemplateData = new WxMaTemplateData();
            wxMaTemplateData.setName(templateData.getName());
            wxMaTemplateData.setValue(TemplateUtil.evaluate(templateData.getValue(), velocityContext));
            wxMaTemplateData.setColor(templateData.getColor());
            wxMessageTemplate.addData(wxMaTemplateData);
        }

        return wxMessageTemplate;
    }
}
