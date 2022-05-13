package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenRecentlyAction extends AbstractAction {

    private MainPane mainPane;

    public OpenRecentlyAction(String filepath,MainPane mainPane){
        super(filepath);
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainPane.openFile(e.getActionCommand());
    }
}
