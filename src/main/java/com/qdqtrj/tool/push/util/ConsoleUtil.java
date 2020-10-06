package com.qdqtrj.tool.push.util;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.qdqtrj.tool.push.ui.form.BoostForm;
import com.qdqtrj.tool.push.ui.form.PushForm;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * BatchMsgPush控制台打印相关
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Slf4j
public class ConsoleUtil {

    private static final Log logger = LogFactory.get();

    /**
     * 输出到控制台和log
     *
     * @param log
     */
    public static void consoleWithLog(String log) {
        PushForm.getInstance().getPushConsoleTextArea().append(log + "\n");
        PushForm.getInstance().getPushConsoleTextArea().setCaretPosition(PushForm.getInstance().getPushConsoleTextArea().getText().length());
        logger.warn(log);
    }

    /**
     * 输出到性能模式控制台和log
     *
     * @param log
     */
    public static void boostConsoleWithLog(String log) {
        BoostForm.getInstance().getConsoleTextArea().append(log + "\n");
        BoostForm.getInstance().getConsoleTextArea().setCaretPosition(BoostForm.getInstance().getConsoleTextArea().getText().length());
        logger.warn(log);
    }

    /**
     * 仅输出到控制台
     *
     * @param log
     */
    public static void consoleOnly(String log) {
        PushForm.getInstance().getPushConsoleTextArea().append(log + "\n");
        PushForm.getInstance().getPushConsoleTextArea().setCaretPosition(PushForm.getInstance().getPushConsoleTextArea().getText().length());
    }

    /**
     * 仅输出到性能模式控制台
     *
     * @param log
     */
    public static void boostConsoleOnly(String log) {
        BoostForm.getInstance().getConsoleTextArea().append(log + "\n");
        BoostForm.getInstance().getConsoleTextArea().setCaretPosition(BoostForm.getInstance().getConsoleTextArea().getText().length());
    }
}
