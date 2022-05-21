package redcoder.texteditor.action;

import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends AbstractAction {

    public ExitAction() {
        super("Exit");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("Stop24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
