package redcoder.texteditor.action;

import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class PasteAction extends TextAction {

    public PasteAction() {
        super("Paste");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getTextComponent(e);
        if (target != null) {
            target.paste();
        }
    }
}
