package com.qdqtrj.tool.push.util;

import com.qdqtrj.tool.push.App;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <pre>
 * Hikari连接池工具
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class HikariUtil {
    private volatile static HikariDataSource hikariDataSource;

    /**
     * 获取数据源
     *
     * @return HikariDataSource
     */
    public static HikariDataSource getHikariDataSource() {
        if (hikariDataSource == null || hikariDataSource.isClosed()) {
            synchronized (HikariUtil.class) {
                if (hikariDataSource == null || hikariDataSource.isClosed()) {
                    String mysqlUrl = App.config.getMysqlUrl();
                    String mysqlUser = App.config.getMysqlUser();
                    String mysqlPassword = App.config.getMysqlPassword();

                    hikariDataSource = new HikariDataSource();
                    hikariDataSource.setJdbcUrl("jdbc:mysql://" + mysqlUrl);
                    hikariDataSource.setUsername(mysqlUser);
                    hikariDataSource.setPassword(mysqlPassword);
                }
            }
        }
        return hikariDataSource;
    }

    /**
     * 获取连接
     *
     * @return Connection
     * @throws SQLException SQLException
     */
    public static Connection getConnection() throws SQLException {
        return getHikariDataSource().getConnection();
    }

    /**
     * 执行查询
     *
     * @param sql sql
     * @return ResultSet
     */
    public static ResultSet executeQuery(String sql) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }
}
