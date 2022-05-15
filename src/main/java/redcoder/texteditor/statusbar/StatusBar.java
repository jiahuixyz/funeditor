package redcoder.texteditor.statusbar;

import redcoder.texteditor.pane.textpane.ScrollTextPane;
import redcoder.texteditor.pane.textpane.TextPaneChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑器底部状态栏
 */
public class StatusBar extends JPanel implements TextPaneChangeListener {

    public static final StatusBar INSTANCE = new StatusBar();
    public static final int STATUS_BAR_HEIGHT = 30;

    private List<JComponent> indicators;

    public StatusBar() {
        super(new GridBagLayout());
        setPreferredSize(new Dimension(Short.MAX_VALUE, STATUS_BAR_HEIGHT));
        init();
    }

    private void init() {
        indicators = new ArrayList<>();
        TextLengthIndicator textLengthIndicator = new TextLengthIndicator();
        CaretStatusIndicator caretStatusIndicator = new CaretStatusIndicator();
        LineSeparatorIndicator lineSeparatorIndicator = new LineSeparatorIndicator();
        TextFontSizeIndicator textFontSizeIndicator = new TextFontSizeIndicator();
        indicators.add(textLengthIndicator);
        indicators.add(caretStatusIndicator);
        indicators.add(lineSeparatorIndicator);
        indicators.add(textFontSizeIndicator);

        // left part
        Box leftBox = Box.createHorizontalBox();
        leftBox.add(Box.createRigidArea(new Dimension(10, STATUS_BAR_HEIGHT)));
        leftBox.add(new JLabel("status bar"));

        // right part
        Box rightBox = Box.createHorizontalBox();
        rightBox.add(textLengthIndicator);
        rightBox.add(Box.createRigidArea(new Dimension(60, STATUS_BAR_HEIGHT)));
        rightBox.add(caretStatusIndicator);
        rightBox.add(Box.createRigidArea(new Dimension(60, STATUS_BAR_HEIGHT)));
        rightBox.add(lineSeparatorIndicator);
        rightBox.add(Box.createRigidArea(new Dimension(20, STATUS_BAR_HEIGHT)));
        rightBox.add(textFontSizeIndicator);
        rightBox.add(Box.createRigidArea(new Dimension(50, STATUS_BAR_HEIGHT)));

        add(leftBox, new GridBagConstraints(0, 0, 1, 1,
                0.2, 1, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        add(rightBox, new GridBagConstraints(1, 0, 1, 1,
                0.8, 1, GridBagConstraints.LINE_END, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    public void hideIndicator() {
        for (JComponent indicator : indicators) {
            if (indicator instanceof Indicator) {
                ((Indicator) indicator).hidden();
            }
        }
    }

    public void displayIndicator() {
        for (JComponent indicator : indicators) {
            if (indicator instanceof Indicator) {
                ((Indicator) indicator).display();
            }
        }
    }

    @Override
    public void onChange(ScrollTextPane textPane) {
        for (JComponent indicator : indicators) {
            if (indicator instanceof TextPaneChangeListener) {
                ((TextPaneChangeListener) indicator).onChange(textPane);
            }
        }
    }
}
