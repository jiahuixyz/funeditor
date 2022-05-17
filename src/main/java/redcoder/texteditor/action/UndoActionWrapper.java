package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.toolbar.ToolBarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class UndoActionWrapper extends AbstractAction {

    private MainTabPane mainTabPane;

    public UndoActionWrapper(MainTabPane mainTabPane) {
        super("Undo");
        this.mainTabPane = mainTabPane;
        Optional.ofNullable(ToolBarIconResource.getImageIcon("Undo24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.getSelectedTextPane().undo(e);
    }
}
