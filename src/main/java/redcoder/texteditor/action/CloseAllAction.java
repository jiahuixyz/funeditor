package redcoder.texteditor.action;

import redcoder.texteditor.pane.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAllAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public CloseAllAction(MainTabPane mainTabPane){
        super("Close All");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.closeAllTab();
    }
}
