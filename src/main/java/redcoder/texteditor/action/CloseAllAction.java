package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAllAction extends AbstractAction {

    private MainPane mainPane;

    public CloseAllAction(MainPane mainPane){
        super("Close All");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainPane.closeAllTextPane();
    }
}
