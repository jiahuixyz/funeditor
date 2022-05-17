package redcoder.texteditor.core.statusbar;

import redcoder.texteditor.utils.SystemUtils;

import javax.swing.*;
import java.awt.*;

/**
 * display line separator.
 */
public class LineSeparatorIndicator extends JPanel implements Indicator {

    private static final String TEMPLATE_TEXT1 = "  Windows (CR LF)";
    private static final String TEMPLATE_TEXT2 = "  UNIX (LF)";

    private final JLabel label = new JLabel();

    public LineSeparatorIndicator() {
        super(new GridLayout(1, 1));
        setBorder(LeftEdgeLineBorder.BORDER);

        init();
    }

    @Override
    public void hidden() {
        setVisible(false);
    }

    @Override
    public void display() {
        setVisible(true);
    }

    private void init() {
        if (SystemUtils.isWindowsOS()) {
            label.setText(TEMPLATE_TEXT1);
        } else {
            label.setText(TEMPLATE_TEXT2);
        }
        this.add(label);
    }
}
