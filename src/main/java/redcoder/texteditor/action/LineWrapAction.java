package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LineWrapAction extends AbstractAction {

    public LineWrapAction() {
        super("Line Wrap");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        for (Component component : tabPane.getComponents()) {
            if (component instanceof ScrollTextPane) {
                ((ScrollTextPane) component).lineWrapSwitch();
            }
        }
    }
}
