package com.qdqtrj.tool.push.util;

import java.io.File;

/**
 * <pre>
 * 系统工具
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class SystemUtil {
    private static String osName = System.getProperty("os.name");
    public static String configHome = System.getProperty("user.home") + File.separator + ".BatchMsgPush"
            + File.separator;

    public static boolean isMacOs() {
        return osName.contains("Mac");
    }
}