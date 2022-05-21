package redcoder.texteditor.action;

import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class PasteAction extends TextAction {

    public PasteAction() {
        super("Paste");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("Paste24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getTextComponent(e);
        if (target != null) {
            target.paste();
        }
    }
}
