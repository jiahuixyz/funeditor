package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class NewWindowAction extends AbstractAction {

    public NewWindowAction() {
        super("New Window");
        Optional.ofNullable(ToolbarIconResource.getImageIcon("Add24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.makeNewWindow();
    }
}
