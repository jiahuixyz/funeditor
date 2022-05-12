package redcoder.texteditor.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends AbstractAction {

    public ExitAction() {
        super("Exit");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
