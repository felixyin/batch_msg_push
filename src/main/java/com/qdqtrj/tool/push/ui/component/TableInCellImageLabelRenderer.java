package com.qdqtrj.tool.push.ui.component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * <pre>
 * 自定义图片单元格渲染器
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class TableInCellImageLabelRenderer extends JLabel implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            String imgUrl = (String) value;
            imgUrl = imgUrl.replace("/132", "/64");
            URL url = new URL(imgUrl);

            BufferedImage image = ImageIO.read(url);
            ImageIcon imageIcon = new ImageIcon(image);
//            imageIcon.setImage(imageIcon.getImage().getScaledInstance(table.getRowHeight(), table.getRowHeight(), Image.SCALE_DEFAULT));
            setIcon(imageIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}