package com.qdqtrj.tool.push.util;

import com.qdqtrj.tool.push.logic.PushControl;
import com.qdqtrj.tool.push.logic.msgsender.WxMpTemplateMsgSender;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

/**
 * <pre>
 * 模板工具
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class TemplateUtil {

    private static VelocityEngine velocityEngine;

    static {
        velocityEngine = new VelocityEngine();
        velocityEngine.init();
    }

    public static String evaluate(String content, VelocityContext velocityContext) {

        if (content.contains("NICK_NAME")) {
            WxMpService wxMpService = WxMpTemplateMsgSender.getWxMpService();
            String nickName = "";
            try {
                nickName = wxMpService.getUserService().userInfo(velocityContext.get(PushControl.TEMPLATE_VAR_PREFIX + "0").toString()).getNickname();
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
            velocityContext.put("NICK_NAME", nickName);
        }

        velocityContext.put("ENTER", "\n");

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(velocityContext, writer, "", content);

        return writer.toString();
    }
}
