package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAction extends AbstractAction {

    private MainPane mainPane;

    public SaveAction(MainPane mainPane) {
        super("Save File");
        this.mainPane = mainPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            mainPane.saveSelectedTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
