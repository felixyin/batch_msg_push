package com.qdqtrj.tool.push.util;

//import org.junit.Test;

import java.awt.*;

/**
 * <pre>
 * 通用测试
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class CommonTest {

//    @Test
    public void testGetSysFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        for (String font : fonts) {
            System.err.println(font);
        }
    }
}
