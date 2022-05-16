package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoActionWrapper extends AbstractAction {

    private MainTabPane mainTabPane;

    public UndoActionWrapper(MainTabPane mainTabPane) {
        super("Undo");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.getSelectedTextPane().undo(e);
    }
}
