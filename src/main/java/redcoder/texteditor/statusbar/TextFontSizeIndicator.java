package redcoder.texteditor.statusbar;

import redcoder.texteditor.pane.MainTabPane;
import redcoder.texteditor.pane.textpane.ScrollTextPane;
import redcoder.texteditor.pane.textpane.TextPaneChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * 显示文本字符大小
 */
public class TextFontSizeIndicator extends JPanel implements Indicator,TextPaneChangeListener {

    private static final String TEMPLATE_TEXT = "  Font Size: %s";
    private final JLabel label = new JLabel("  UTF-8");

    public TextFontSizeIndicator() {
        super(new GridLayout(1, 1));
        setBorder(StatusBarBorder.BORDER);

        add(label);
        updateLabel(MainTabPane.DEFAULT_FONT);
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
        updateLabel(textArea.getFont());
    }

    private void updateLabel(Font font) {
        String text = String.format(TEMPLATE_TEXT, font.getSize());
        this.label.setText(text);
    }
}
