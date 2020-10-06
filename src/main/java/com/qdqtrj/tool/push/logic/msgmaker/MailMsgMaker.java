package com.qdqtrj.tool.push.logic.msgmaker;

import com.qdqtrj.tool.push.bean.MailMsg;
import com.qdqtrj.tool.push.ui.form.msg.MailMsgForm;
import com.qdqtrj.tool.push.util.TemplateUtil;
import org.apache.velocity.VelocityContext;

/**
 * <pre>
 * E-Mail加工器
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class MailMsgMaker extends BaseMsgMaker implements IMsgMaker {

    public static String mailTitle;
    public static String mailCc;
    public static String mailFiles;
    public static String mailContent;

    /**
     * 准备(界面字段等)
     */
    @Override
    public void prepare() {
        mailTitle = MailMsgForm.getInstance().getMailTitleTextField().getText();
        mailCc = MailMsgForm.getInstance().getMailCcTextField().getText();
        mailFiles = MailMsgForm.getInstance().getMailFilesTextField().getText();
        mailContent = MailMsgForm.getInstance().getMailContentPane().getText();
    }

    /**
     * 组织E-Mail消息
     *
     * @param msgData 消息信息
     * @return MailMsg
     */
    @Override
    public MailMsg makeMsg(String[] msgData) {
        MailMsg mailMsg = new MailMsg();
        VelocityContext velocityContext = getVelocityContext(msgData);
        String title = TemplateUtil.evaluate(mailTitle, velocityContext);
        String cc = TemplateUtil.evaluate(mailCc, velocityContext);
        String files = TemplateUtil.evaluate(mailFiles, velocityContext);
        String content = TemplateUtil.evaluate(mailContent, velocityContext);
        mailMsg.setMailTitle(title);
        mailMsg.setMailCc(cc);
        mailMsg.setMailFiles(files);
        mailMsg.setMailContent(content);
        return mailMsg;
    }
}
