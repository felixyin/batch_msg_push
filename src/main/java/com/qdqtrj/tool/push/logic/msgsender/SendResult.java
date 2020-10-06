package com.qdqtrj.tool.push.logic.msgsender;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <pre>
 * 发送结果
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
@Setter
@ToString
public class SendResult {
    private boolean success = false;

    private String info;
}
