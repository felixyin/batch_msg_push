package com.qdqtrj.tool.push.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <pre>
 * 邮件消息体
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
@Setter
@ToString
public class MailMsg implements Serializable {

    private static final long serialVersionUID = 7269816872586216264L;

    /**
     * 标题
     */
    private String mailTitle;

    /**
     * 抄送
     */
    private String mailCc;

    /**
     * 附件
     */
    private String mailFiles;

    /**
     * 内容
     */
    private String mailContent;

}
