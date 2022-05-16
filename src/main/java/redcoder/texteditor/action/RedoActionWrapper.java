package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoActionWrapper extends AbstractAction {

    private MainTabPane mainTabPane;

    public RedoActionWrapper(MainTabPane mainTabPane) {
        super("Redo");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.getSelectedTextPane().redo(e);
    }
}
