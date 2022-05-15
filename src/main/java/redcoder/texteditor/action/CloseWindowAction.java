package redcoder.texteditor.action;

import redcoder.texteditor.pane.Framework;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseWindowAction extends AbstractAction {

    public CloseWindowAction() {
        super("Close Window");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.INSTANCE.closeWindow();
    }
}
