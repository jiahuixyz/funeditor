package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoActionWrapper extends AbstractAction {

    public UndoActionWrapper() {
        super("Undo");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("Undo24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        tabPane.getSelectedTextPane().undo(e);
    }
}
