package redcoder.texteditor.statusbar;

import javax.swing.*;
import java.awt.*;

/**
 * 编辑器底部状态栏
 */
public class StatusBar extends JPanel {

    public static final StatusBar INSTANCE = new StatusBar();
    public static final int STATUS_BAR_HEIGHT = 30;

    public StatusBar() {
        super(new GridBagLayout());
        setPreferredSize(new Dimension(Short.MAX_VALUE, STATUS_BAR_HEIGHT));
        init();
    }

    private void init() {
        // left part
        Box leftBox = Box.createHorizontalBox();
        leftBox.add(Box.createRigidArea(new Dimension(10, STATUS_BAR_HEIGHT)));
        leftBox.add(new JLabel("status bar"));

        // right part
        Box rightBox = Box.createHorizontalBox();
        rightBox.add(TextLengthIndicator.INDICATOR);
        rightBox.add(Box.createRigidArea(new Dimension(60, STATUS_BAR_HEIGHT)));
        rightBox.add(CaretStatusIndicator.INDICATOR);
        rightBox.add(Box.createRigidArea(new Dimension(60, STATUS_BAR_HEIGHT)));
        rightBox.add(LineSeparatorIndicator.INDICATOR);
        rightBox.add(Box.createRigidArea(new Dimension(20, STATUS_BAR_HEIGHT)));
        rightBox.add(TextFontSizeIndicator.INDICATOR);
        rightBox.add(Box.createRigidArea(new Dimension(50, STATUS_BAR_HEIGHT)));

        add(leftBox, new GridBagConstraints(0, 0, 1, 1,
                0.2, 1, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        add(rightBox,  new GridBagConstraints(1, 0, 1, 1,
                0.8, 1, GridBagConstraints.LINE_END, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    public void hideStatusBar() {
        TextLengthIndicator.INDICATOR.setVisible(false);
        CaretStatusIndicator.INDICATOR.setVisible(false);
        LineSeparatorIndicator.INDICATOR.setVisible(false);
        TextFontSizeIndicator.INDICATOR.setVisible(false);
    }

    public void displayStatusBar() {
        TextLengthIndicator.INDICATOR.setVisible(true);
        CaretStatusIndicator.INDICATOR.setVisible(true);
        LineSeparatorIndicator.INDICATOR.setVisible(true);
        TextFontSizeIndicator.INDICATOR.setVisible(true);
    }
}
