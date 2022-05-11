package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseFileAction extends AbstractAction {

    private MainPane mainPane;

    public CloseFileAction(MainPane mainPane){
        super("Close");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainPane.closeFile();
    }
}
