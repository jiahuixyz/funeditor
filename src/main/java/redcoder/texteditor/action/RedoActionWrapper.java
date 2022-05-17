package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.toolbar.ToolBarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class RedoActionWrapper extends AbstractAction {

    private MainTabPane mainTabPane;

    public RedoActionWrapper(MainTabPane mainTabPane) {
        super("Redo");
        this.mainTabPane = mainTabPane;
        Optional.ofNullable(ToolBarIconResource.getImageIcon("Redo24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.getSelectedTextPane().redo(e);
    }
}
