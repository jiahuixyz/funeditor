package redcoder.texteditor.action;

import redcoder.texteditor.pane.Framework;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewWindowAction extends AbstractAction {

    public NewWindowAction() {
        super("New Window");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.FRAMEWORK.makeNewWindow();
    }
}
