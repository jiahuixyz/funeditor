package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public OpenAction(MainTabPane mainTabPane) {
        super("Open File");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Framework.getFileProcessor().openFile(mainTabPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
