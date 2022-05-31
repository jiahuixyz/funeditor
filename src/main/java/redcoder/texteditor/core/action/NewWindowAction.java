package redcoder.texteditor.core.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewWindowAction extends AbstractAction {

    public NewWindowAction() {
        super("New Window");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("new_window24.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.makeNewWindow();
    }
}
