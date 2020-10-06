package com.qdqtrj.tool.push.logic.msgmaker;

import com.qdqtrj.tool.push.App;
import com.qdqtrj.tool.push.logic.MessageTypeEnum;

/**
 * <pre>
 * 消息加工器工厂类
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class MsgMakerFactory {

    /**
     * 获取消息加工器
     *
     * @return IMsgMaker
     */
    public static IMsgMaker getMsgMaker() {
        IMsgMaker iMsgMaker = null;
        switch (App.config.getMsgType()) {
            case MessageTypeEnum.MP_TEMPLATE_CODE:
                iMsgMaker = new WxMpTemplateMsgMaker();
                break;
            case MessageTypeEnum.MA_TEMPLATE_CODE:
                iMsgMaker = new WxMaTemplateMsgMaker();
                break;
            case MessageTypeEnum.MA_SUBSCRIBE_CODE:
                iMsgMaker = new WxMaSubscribeMsgMaker();
                break;
            case MessageTypeEnum.KEFU_CODE:
                iMsgMaker = new WxKefuMsgMaker();
                break;
            case MessageTypeEnum.ALI_YUN_CODE:
                iMsgMaker = new AliyunMsgMaker();
                break;
            case MessageTypeEnum.TX_YUN_CODE:
                iMsgMaker = new TxYunMsgMaker();
                break;
            case MessageTypeEnum.HW_YUN_CODE:
                iMsgMaker = new HwYunMsgMaker();
                break;
            case MessageTypeEnum.YUN_PIAN_CODE:
                iMsgMaker = new YunPianMsgMaker();
                break;
            case MessageTypeEnum.EMAIL_CODE:
                iMsgMaker = new MailMsgMaker();
                break;
            case MessageTypeEnum.WX_CP_CODE:
                iMsgMaker = new WxCpMsgMaker();
                break;
            case MessageTypeEnum.HTTP_CODE:
                iMsgMaker = new HttpMsgMaker();
                break;
            case MessageTypeEnum.DING_CODE:
                iMsgMaker = new DingMsgMaker();
                break;
            case MessageTypeEnum.BD_YUN_CODE:
                iMsgMaker = new BdYunMsgMaker();
                break;
            case MessageTypeEnum.UP_YUN_CODE:
                iMsgMaker = new UpYunMsgMaker();
                break;
            case MessageTypeEnum.QI_NIU_YUN_CODE:
                iMsgMaker = new QiNiuYunMsgMaker();
                break;
            default:
        }
        return iMsgMaker;
    }
}
