package redcoder.texteditor.action;

import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class CutAction extends TextAction {

    public CutAction() {
        super("Cut");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getTextComponent(e);
        if (target != null) {
            target.cut();
        }
    }
}
