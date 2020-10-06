package com.qdqtrj.tool.push.logic.msgsender;

/**
 * <pre>
 * 消息发送器接口
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public interface IMsgSender {

    /**
     * 发送消息
     *
     * @param msgData 消息数据
     */
    SendResult send(String[] msgData);

    /**
     * 异步发送消息
     *
     * @param msgData 消息数据
     */
    SendResult asyncSend(String[] msgData);
}
