package redcoder.texteditor.statusbar;

import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.awt.*;

/**
 * 显示文本字符大小
 */
public class TextFontSizeIndicator extends JPanel {

    private static final String TEMPLATE_TEXT = "  Font Size: %s";
    private final JLabel label = new JLabel("  UTF-8");

    public TextFontSizeIndicator() {
        super(new GridLayout(1, 1));
        setBorder(StatusBarBorder.BORDER);

        refresh(MainTabPane.DEFAULT_FONT);
        add(label);
    }

    public void refresh(Font font) {
        String text = String.format(TEMPLATE_TEXT, font.getSize());
        this.label.setText(text);
    }

}
