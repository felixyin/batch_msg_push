package com.qdqtrj.tool.push.logic.msgsender;

import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;

/**
 * <pre>
 * 消息发送器工厂类
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class MsgSenderFactory {

    /**
     * 根据消息类型获取对应的消息发送器
     *
     * @return IMsgSender
     */
    public static IMsgSender getMsgSender() {
        IMsgSender iMsgSender = null;
        switch (App.config.getMsgType()) {
            case MessageTypeEnum.MP_TEMPLATE_CODE:
                iMsgSender = new WxMpTemplateMsgSender();
                break;
            case MessageTypeEnum.MA_TEMPLATE_CODE:
                iMsgSender = new WxMaTemplateMsgSender();
                break;
            case MessageTypeEnum.MA_SUBSCRIBE_CODE:
                iMsgSender = new WxMaSubscribeMsgSender();
                break;
            case MessageTypeEnum.KEFU_CODE:
                iMsgSender = new WxKefuMsgSender();
                break;
            case MessageTypeEnum.KEFU_PRIORITY_CODE:
                iMsgSender = new WxKefuPriorMsgSender();
                break;
            case MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE:
                iMsgSender = new WxUniformMsgSender();
                break;
            case MessageTypeEnum.ALI_YUN_CODE:
                iMsgSender = new AliYunMsgSender();
                break;
            case MessageTypeEnum.TX_YUN_CODE:
                iMsgSender = new TxYunMsgSender();
                break;
            case MessageTypeEnum.HW_YUN_CODE:
                iMsgSender = new HwYunMsgSender();
                break;
            case MessageTypeEnum.YUN_PIAN_CODE:
                iMsgSender = new YunPianMsgSender();
                break;
            case MessageTypeEnum.EMAIL_CODE:
                iMsgSender = new MailMsgSender();
                break;
            case MessageTypeEnum.WX_CP_CODE:
                iMsgSender = new WxCpMsgSender();
                break;
            case MessageTypeEnum.HTTP_CODE:
                iMsgSender = new HttpMsgSender();
                break;
            case MessageTypeEnum.DING_CODE:
                iMsgSender = new DingMsgSender();
                break;
            case MessageTypeEnum.BD_YUN_CODE:
                iMsgSender = new BdYunMsgSender();
                break;
            case MessageTypeEnum.UP_YUN_CODE:
                iMsgSender = new UpYunMsgSender();
                break;
            case MessageTypeEnum.QI_NIU_YUN_CODE:
                iMsgSender = new QiNiuYunMsgSender();
                break;
            default:
                break;
        }
        return iMsgSender;
    }
}
