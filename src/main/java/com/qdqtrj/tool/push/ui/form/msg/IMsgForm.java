package com.qdqtrj.tool.push.ui.form.msg;

/**
 * <pre>
 * 消息编辑form界面接口
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public interface IMsgForm {
    /**
     * 初始化界面
     *
     * @param msgName
     */
    void init(String msgName);

    /**
     * 保存消息
     *
     * @param msgName
     */
    void save(String msgName);
}
