package com.qdqtrj.tool.push.logic.msgsender;

/**
 * <pre>
 * 微信客服消息优先消息发送器
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class WxKefuPriorMsgSender implements IMsgSender {

    private WxMpTemplateMsgSender wxMpTemplateMsgSender;
    private WxKefuMsgSender wxKefuMsgSender;

    public WxKefuPriorMsgSender() {
        wxMpTemplateMsgSender = new WxMpTemplateMsgSender();
        wxKefuMsgSender = new WxKefuMsgSender();
    }

    @Override
    public SendResult send(String[] msgData) {
        SendResult sendResult = new SendResult();

        SendResult kefuMsgSendResult = wxKefuMsgSender.send(msgData);
        if (!kefuMsgSendResult.isSuccess()) {
            SendResult mpMsgSendResult = wxMpTemplateMsgSender.send(msgData);
            if (!mpMsgSendResult.isSuccess()) {
                return mpMsgSendResult;
            }
        }

        sendResult.setSuccess(true);
        return sendResult;
    }

    @Override
    public SendResult asyncSend(String[] msgData) {
        return null;
    }
}
