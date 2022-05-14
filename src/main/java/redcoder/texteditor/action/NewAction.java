package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public NewAction(MainTabPane mainTabPane) {
        super("New File");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.createTextPane();
    }
}
