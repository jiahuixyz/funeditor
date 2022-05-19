package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class NewWindowAction extends AbstractAction {

    public NewWindowAction() {
        super("New Window");
        Optional.ofNullable(IconResource.getImageIcon("Add24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.makeNewWindow();
    }
}
