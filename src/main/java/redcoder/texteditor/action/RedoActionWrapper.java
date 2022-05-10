package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoActionWrapper extends AbstractAction {

    private MainPane mainPane;

    public RedoActionWrapper(MainPane mainPane) {
        super("Redo");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainPane.getSelectedTextPane().getRedoAction().actionPerformed(e);
    }
}
