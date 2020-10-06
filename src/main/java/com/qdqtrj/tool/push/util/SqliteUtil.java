package com.qdqtrj.tool.push.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * <pre>
 * Sqlite相关工具
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class SqliteUtil {

    public static String nowDateForSqlite() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
