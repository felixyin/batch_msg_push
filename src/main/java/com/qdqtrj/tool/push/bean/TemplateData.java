package com.qdqtrj.tool.push.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <pre>
 * 模板数据
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
@Setter
public class TemplateData implements Serializable {

    private String name;

    private String value;

    private String color;
}
