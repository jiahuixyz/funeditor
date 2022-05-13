package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;
import redcoder.texteditor.pane.ScrollTextPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction {

    private MainPane mainPane;

    public CloseAction(MainPane mainPane) {
        super("Close File");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ScrollTextPane scrollTextPane = mainPane.getSelectedTextPane();
        int index = mainPane.getSelectedIndex();
        scrollTextPane.closeTextPane(index);
    }
}
