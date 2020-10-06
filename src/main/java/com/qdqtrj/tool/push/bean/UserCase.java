package com.qdqtrj.tool.push.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 * 用户案例
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Data
public class UserCase implements Serializable {

    private static final long serialVersionUID = 2829237163275443844L;

    private String qrCodeUrl;

    private String title;

    private String desc;

}
