package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public SaveAction(MainTabPane mainTabPane) {
        super("Save File");
        this.mainTabPane = mainTabPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            mainTabPane.saveSelectedTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
