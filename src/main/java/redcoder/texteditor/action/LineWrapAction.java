package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LineWrapAction extends AbstractAction {

    private TabPane tabPane;

    public LineWrapAction(TabPane tabPane) {
        super("Line Wrap");
        this.tabPane = tabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Component component : tabPane.getComponents()) {
            if (component instanceof ScrollTextPane) {
                ((ScrollTextPane) component).lineWrapSwitch();
            }
        }
    }
}
