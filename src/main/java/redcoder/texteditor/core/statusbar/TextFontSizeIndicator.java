package redcoder.texteditor.core.statusbar;

import redcoder.texteditor.core.fontsize.FontZoomInZoomOutProcessor;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.core.textpane.TextPaneChangeEvent;
import redcoder.texteditor.core.textpane.TextPaneChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * 显示文本字符大小
 */
public class TextFontSizeIndicator extends JPanel implements Indicator, TextPaneChangeListener {

    private static final String TEMPLATE_TEXT = "  Font Size: %s";
    private final JLabel label = new JLabel("  UTF-8");

    public TextFontSizeIndicator() {
        super(new GridLayout(1, 1));
        setBorder(StatusBarBorder.BORDER);

        add(label);
        updateLabel(FontZoomInZoomOutProcessor.getSharedFont());
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
            updateLabel(textArea.getFont());
        }
    }

    private void updateLabel(Font font) {
        String text = String.format(TEMPLATE_TEXT, font.getSize());
        this.label.setText(text);
    }
}
