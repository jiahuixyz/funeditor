package redcoder.texteditor.core.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseWindowAction extends AbstractAction {

    public CloseWindowAction() {
        super("Close Window");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("close_window24.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.closeWindow();
    }
}
