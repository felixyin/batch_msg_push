package com.qdqtrj.tool.push.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <pre>
 * 钉钉消息
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
@Setter
@ToString
public class DingMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;

    private String title;

    private String picUrl;

    private String url;

    private String btnTxt;

    private String btnUrl;
}
