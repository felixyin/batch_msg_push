package com.qdqtrj.tool.push.logic.msgsender;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <pre>
 * HttpSendResult
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
@Setter
@ToString
public class HttpSendResult extends SendResult {
    private String headers;

    private String body;

    private String cookies;
}
