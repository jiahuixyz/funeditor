package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainTabPane;
import redcoder.texteditor.pane.textpane.ScrollTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LineWrapAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public LineWrapAction(MainTabPane mainTabPane) {
        super("Line Wrap");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Component component : mainTabPane.getComponents()) {
            if (component instanceof ScrollTextPane) {
                ((ScrollTextPane) component).lineWrapSwitch();
            }
        }
    }
}
