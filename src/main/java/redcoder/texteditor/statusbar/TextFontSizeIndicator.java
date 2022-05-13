package redcoder.texteditor.statusbar;

import redcoder.texteditor.pane.MainPane;

import javax.swing.*;
import java.awt.*;

/**
 * 显示文本字符大小
 */
public class TextFontSizeIndicator extends JPanel {

    public static final TextFontSizeIndicator INDICATOR = new TextFontSizeIndicator();
    private static final String TEMPLATE_TEXT = "  Font Size: %s";
    private final JLabel label = new JLabel("  UTF-8");

    private TextFontSizeIndicator() {
        super(new GridLayout(1, 1));
        setBorder(StatusBarBorder.BORDER);

        refresh(MainPane.DEFAULT_FONT);
        add(label);
    }

    public void refresh(Font font) {
        String text = String.format(TEMPLATE_TEXT, font.getSize());
        this.label.setText(text);
    }

}
