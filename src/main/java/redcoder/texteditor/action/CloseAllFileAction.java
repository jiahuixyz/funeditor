package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAllFileAction extends AbstractAction {

    private MainPane mainPane;

    public CloseAllFileAction(MainPane mainPane){
        super("Close All");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainPane.closeAllFile();
    }
}
