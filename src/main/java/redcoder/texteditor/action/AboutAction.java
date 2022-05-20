package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.help.AboutDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AboutAction extends AbstractAction {

    public AboutAction() {
        super("About");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AboutDialog.showAbout(Framework.getActivatedFrame());
    }
}
