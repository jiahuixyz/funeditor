package redcoder.texteditor.action;

import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class CopyAction extends TextAction {

    public CopyAction() {
        super("Copy");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getTextComponent(e);
        if (target != null) {
            target.copy();
        }
    }
}
