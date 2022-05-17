package redcoder.texteditor.action;

import redcoder.texteditor.core.toolbar.ToolBarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class ExitAction extends AbstractAction {

    public ExitAction() {
        super("Exit");
        Optional.ofNullable(ToolBarIconResource.getImageIcon("Stop24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
