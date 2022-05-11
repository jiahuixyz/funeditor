package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAllFileAction extends AbstractAction {

    private MainPane mainPane;

    public SaveAllFileAction(MainPane mainPane) {
        super("Save All");
        this.mainPane = mainPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            mainPane.saveAllFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
