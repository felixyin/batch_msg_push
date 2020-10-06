package com.qdqtrj.tool.push.dao;

/**
 * <pre>
 * 初始化数据库
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public interface InitMapper {
    /**
     * 初始化创建所有表
     *
     * @return
     */
    int createAllTables();
}
