package com.qdqtrj.tool.push.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * HttpMsg
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
@Getter
@Setter
@ToString
public class HttpMsg implements Serializable {

    private static final long serialVersionUID = 114436270588113296L;

    private String url;

    private String body;

    private Map<String, Object> paramMap;

    private Map<String, Object> headerMap;

    private List<HttpCookie> cookies;

}
