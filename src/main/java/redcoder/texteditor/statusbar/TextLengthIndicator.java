package redcoder.texteditor.statusbar;

import javax.swing.*;
import java.awt.*;

/**
 * 显示当前文本窗格的字符长度和行数。
 */
public class TextLengthIndicator extends JPanel {

    public static final TextLengthIndicator INDICATOR = new TextLengthIndicator();
    private static final String TEMPLATE_TEXT = "  length: %d  lines: %d";

    private final JLabel label = new JLabel();

    private TextLengthIndicator(){
        super(new GridLayout(1, 1));
        setBorder(StatusBarBorder.BORDER);

        label.setText(getFormattedText(0, 0));
        add(label);
    }

    public void refresh(JTextArea textArea) {
        int length = textArea.getText().length();
        int lineCount = textArea.getLineCount();
        label.setText(getFormattedText(length, lineCount));
    }

    private String getFormattedText(int length, int lines) {
        return String.format(TEMPLATE_TEXT, length, lines);
    }
}
