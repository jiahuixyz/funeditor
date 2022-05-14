package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAllAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public SaveAllAction(MainTabPane mainTabPane) {
        super("Save All");
        this.mainTabPane = mainTabPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            mainTabPane.saveAllTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
