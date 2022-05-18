package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class UndoActionWrapper extends AbstractAction {

    private TabPane tabPane;

    public UndoActionWrapper(TabPane tabPane) {
        super("Undo");
        this.tabPane = tabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("Undo24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tabPane.getSelectedTextPane().undo(e);
    }
}
