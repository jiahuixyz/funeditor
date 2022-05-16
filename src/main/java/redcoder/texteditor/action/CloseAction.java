package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public CloseAction(MainTabPane mainTabPane) {
        super("Close File");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.closeSelectedTab();
    }
}
