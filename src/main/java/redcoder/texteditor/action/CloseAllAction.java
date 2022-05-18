package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAllAction extends AbstractAction {

    private TabPane tabPane;

    public CloseAllAction(TabPane tabPane){
        super("Close All");
        this.tabPane = tabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tabPane.closeAllTab();
    }
}
