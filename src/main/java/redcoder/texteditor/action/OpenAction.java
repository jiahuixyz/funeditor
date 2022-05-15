package redcoder.texteditor.action;

import redcoder.texteditor.pane.Framework;
import redcoder.texteditor.pane.MainTabPane;

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
            Framework.INSTANCE.getFileProcessor().openFile(mainTabPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
