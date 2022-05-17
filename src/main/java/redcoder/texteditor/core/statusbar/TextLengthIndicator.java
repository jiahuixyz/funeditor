package redcoder.texteditor.core.statusbar;

import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.core.textpane.TextPaneChangeEvent;
import redcoder.texteditor.core.textpane.TextPaneChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * 显示当前文本窗格的字符长度和行数。
 */
public class TextLengthIndicator extends JPanel implements Indicator, TextPaneChangeListener {

    private static final String TEMPLATE_TEXT = "  length: %d, lines: %d";

    private final JLabel label = new JLabel();

    public TextLengthIndicator() {
        super(new GridLayout(1, 1));
        setBorder(LeftEdgeLineBorder.BORDER);

        label.setText(getFormattedText(0, 0));
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
    public void onChange(TextPaneChangeEvent e) {
        Object source = e.getSource();
        if (source instanceof ScrollTextPane) {
            ScrollTextPane textPane = (ScrollTextPane) source;
            JTextArea textArea = textPane.getTextArea();
            int length = textArea.getText().length();
            int lineCount = textArea.getLineCount();
            label.setText(getFormattedText(length, lineCount));
        }
    }

    private String getFormattedText(int length, int lines) {
        return String.format(TEMPLATE_TEXT, length, lines);
    }
}
