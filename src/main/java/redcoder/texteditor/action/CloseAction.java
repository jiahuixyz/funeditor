package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction {

    private TabPane tabPane;

    public CloseAction(TabPane tabPane) {
        super("Close File");
        this.tabPane = tabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tabPane.closeSelectedTab();
    }
}
