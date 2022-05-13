package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;
import redcoder.texteditor.pane.ScrollTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LineWrapAction extends AbstractAction {

    private MainPane mainPane;

    public LineWrapAction(MainPane mainPane) {
        super("Line Wrap");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Component component : mainPane.getComponents()) {
            if (component instanceof ScrollTextPane) {
                ((ScrollTextPane) component).lineWrapSwitch();
            }
        }
    }
}
