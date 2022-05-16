package redcoder.texteditor.action;

import redcoder.texteditor.theme.Theme;
import redcoder.texteditor.core.Framework;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ThemeAction extends AbstractAction {

    private final Theme theme;

    public ThemeAction(Theme theme) {
        super(theme.name);
        this.theme = theme;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.switchTheme(theme.clazz);
    }
}
