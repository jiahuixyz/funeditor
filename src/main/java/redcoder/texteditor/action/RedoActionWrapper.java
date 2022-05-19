package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class RedoActionWrapper extends AbstractAction {

    public RedoActionWrapper() {
        super("Redo");
        Optional.ofNullable(IconResource.getImageIcon("Redo24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        tabPane.getSelectedTextPane().redo(e);
    }
}
