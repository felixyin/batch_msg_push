package com.qdqtrj.tool.push.ui;

import java.awt.*;

/**
 * <pre>
 * UI相关的常量
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class Consts {

    /**
     * 软件名称,版本
     */
    public final static String APP_NAME = "群发助手";
    public final static String APP_VERSION = "v_4.2.1_200308";

    /**
     * Logo-1024*1024
     */
    public static final Image IMAGE_LOGO_1024 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-1024.png"));

    /**
     * Logo-512*512
     */
    public static final Image IMAGE_LOGO_512 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-512.png"));

    /**
     * Logo-256*256
     */
    public static final Image IMAGE_LOGO_256 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-256.png"));

    /**
     * Logo-128*128
     */
    public static final Image IMAGE_LOGO_128 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-128.png"));

    /**
     * Logo-64*64
     */
    public static final Image IMAGE_LOGO_64 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-64.png"));

    /**
     * Logo-48*48
     */
    public static final Image IMAGE_LOGO_48 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-48.png"));

    /**
     * Logo-32*32
     */
    public static final Image IMAGE_LOGO_32 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-32.png"));

    /**
     * Logo-24*24
     */
    public static final Image IMAGE_LOGO_24 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-24.png"));

    /**
     * Logo-16*16
     */
    public static final Image IMAGE_LOGO_16 = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/logo-16.png"));

    /**
     * 帮助图标
     */
    public final static Image HELP_ICON = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/helpButton.png"));

    /**
     * 帮助图标-focused
     */
    public final static Image HELP_FOCUSED_ICON = Toolkit.getDefaultToolkit()
            .getImage(Consts.class.getResource("/icon/helpButtonFocused.png"));

    /**
     * 软件版本检查url
     */
    public final static String CHECK_VERSION_URL = "https://gitee.com/zhoubochina/BatchMsgPush/raw/master/src/main/resources/version_summary.json";

    /**
     * 用户案例url
     */
    public final static String USER_CASE_URL = "http://download.zhoubochina.com/file/user_case.json";

    /**
     * 二维码url
     */
    public final static String QR_CODE_URL = "http://download.zhoubochina.com/file/BatchMsgPush_qrcode.json";

    /**
     * 介绍二维码URL
     */
    public final static String INTRODUCE_QRCODE_URL = "http://download.zhoubochina.com/qrcode/introduce-BatchMsgPush-qrcode.png";

}
