package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class RedoActionWrapper extends AbstractAction {

    private TabPane tabPane;

    public RedoActionWrapper(TabPane tabPane) {
        super("Redo");
        this.tabPane = tabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("Redo24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tabPane.getSelectedTextPane().redo(e);
    }
}
