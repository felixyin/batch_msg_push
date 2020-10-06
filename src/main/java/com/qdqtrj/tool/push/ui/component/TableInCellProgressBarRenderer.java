package com.qdqtrj.tool.push.ui.component;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * <pre>
 * 自定义进度条单元格渲染器
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class TableInCellProgressBarRenderer extends JProgressBar implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        // 这一列必须都是integer类型(0-100)
        Integer v = (Integer) value;
        setValue(v);
        return this;
    }
}