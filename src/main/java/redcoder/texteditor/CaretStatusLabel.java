package redcoder.texteditor;

import redcoder.texteditor.utils.RowColumnUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * 根据插入符号的位置展示一些信息：位于第几行第几列，选中的几个字符。
 */
public class CaretStatusLabel extends JLabel implements CaretListener {

    private static final String MESSAGE_TPL1 = "row %d, col %d";
    private static final String MESSAGE_TPL2 = "row %d, col %d (%d selected)";
    private static final CaretStatusLabel INSTANCE = new CaretStatusLabel();

    public static CaretStatusLabel getInstance() {
        return INSTANCE;
    }

    public CaretStatusLabel() {
        // setOpaque(true);
        // setBackground(Color.BLACK);
        setPreferredSize(new Dimension(150, 30));
        setText("");
    }

    public void resetStatus() {
        setText("");
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        displayInfo(e.getSource(), e.getDot(), e.getMark());
    }

    private void displayInfo(Object source, int dot, int mark) {
        SwingUtilities.invokeLater(() -> {
            if (!(source instanceof JTextComponent)) {
                return;
            }
            JTextComponent textComponent = (JTextComponent) source;
            int row = RowColumnUtils.getRow(dot, textComponent);
            int col = RowColumnUtils.getColumn(dot, textComponent);

            if (dot == mark) {
                setText(String.format(MESSAGE_TPL1, row, col));
            } else {
                int selectedCharCount = Math.abs(dot - mark);
                setText(String.format(MESSAGE_TPL2, row, col, selectedCharCount));
            }
        });
    }
}
