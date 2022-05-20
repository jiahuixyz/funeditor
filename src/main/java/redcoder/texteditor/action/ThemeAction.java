package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.theme.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ThemeAction extends AbstractAction {

    private final Theme theme;

    public ThemeAction(Theme theme) {
        super(theme.name);
        this.theme = theme;

        if (theme == Theme.FLAT_LIGHT_LAF) {
            Framework.setCurrentThemeAction(this);
            selected();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.switchTheme(this);
    }

    public Theme getTheme() {
        return theme;
    }

    public void selected() {
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("selected.png"));
    }

    public void unselected() {
        putValue(Action.SMALL_ICON, null);
    }
}
