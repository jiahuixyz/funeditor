package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenRecentlyAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public OpenRecentlyAction(String filepath, MainTabPane mainTabPane){
        super(filepath);
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.openFile(e.getActionCommand());
    }
}
