package com.qdqtrj.tool.push.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * <pre>
 * JTableUtil
 * </pre>
 *
 * @author <a href="http://www.qdqtrj.com">青岛前途软件-尹彬</a>
 * @since 2020/10/06
 */
public class JTableUtil {
    /**
     * 隐藏表格中的某一列
     *
     * @param table
     * @param index
     */
    public static void hideColumn(JTable table, int index) {
        TableColumn tableColumn = table.getColumnModel().getColumn(index);
        tableColumn.setMaxWidth(0);
        tableColumn.setMinWidth(0);
        tableColumn.setPreferredWidth(0);
        tableColumn.setWidth(0);

        table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

    /**
     * 隐藏表头
     *
     * @param table
     */
    public static void hideTableHeader(JTable table) {
        table.getTableHeader().setVisible(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setPreferredSize(new Dimension(0, 0));
        table.getTableHeader().setDefaultRenderer(renderer);
    }
}
