package redcoder.texteditor.statusbar;

import redcoder.texteditor.pane.textpane.ScrollTextPane;
import redcoder.texteditor.pane.textpane.TextPaneChangeListener;
import redcoder.texteditor.utils.RowColumnUtils;

import javax.swing.*;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * 根据插入符号的位置展示一些信息：位于第几行第几列，选中的几个字符。
 */
public class CaretStatusIndicator extends JPanel implements Indicator, TextPaneChangeListener {

    private static final String TEMPLATE_TEXT1 = "  row %d, col %d";
    private static final String TEMPLATE_TEXT2 = "  row %d, col %d (%d selected)";

    private final JLabel label = new JLabel();

    public CaretStatusIndicator() {
        super(new GridLayout(1, 1));
        setBorder(StatusBarBorder.BORDER);

        label.setText(getFormattedText(1, 1, 0));
        add(label);
    }

    @Override
    public void hidden() {
        setVisible(false);
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void onChange(ScrollTextPane textPane) {
        JTextArea textArea = textPane.getTextArea();
        Caret caret = textArea.getCaret();
        refresh(textArea, caret.getDot(), caret.getMark());
    }

    private void refresh(JTextComponent textComponent, int dot, int mark) {
        SwingUtilities.invokeLater(() -> {
            int row = RowColumnUtils.getRow(dot, textComponent);
            int col = RowColumnUtils.getColumn(dot, textComponent);

            if (dot == mark) {
                label.setText(getFormattedText(row, col, 0));
            } else {
                int selectedCharCount = Math.abs(dot - mark);
                label.setText(getFormattedText(row, col, selectedCharCount));
            }
        });
    }

    private String getFormattedText(int row, int col, int selectedCharCount) {
        if (selectedCharCount > 0) {
            return String.format(TEMPLATE_TEXT2, row, col, selectedCharCount);
        }
        return String.format(TEMPLATE_TEXT1, row, col);
    }
}
