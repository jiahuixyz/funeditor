package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAllAction extends AbstractAction {

    private MainPane mainPane;

    public SaveAllAction(MainPane mainPane) {
        super("Save All");
        this.mainPane = mainPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            mainPane.saveAllTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
