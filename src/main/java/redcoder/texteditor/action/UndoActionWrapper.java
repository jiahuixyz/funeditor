package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class UndoActionWrapper extends AbstractAction {

    public UndoActionWrapper() {
        super("Undo");
        Optional.ofNullable(IconResource.getImageIcon("Undo24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        tabPane.getSelectedTextPane().undo(e);
    }
}
