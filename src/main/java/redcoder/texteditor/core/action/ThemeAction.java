package redcoder.texteditor.core.action;

import redcoder.texteditor.resources.IconResource;
import redcoder.texteditor.theme.Theme;
import redcoder.texteditor.theme.ThemeManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ThemeAction extends AbstractAction {

    private final Theme theme;

    public ThemeAction(Theme theme) {
        super(theme.name);
        this.theme = theme;

        if (theme == Theme.FLAT_LIGHT_LAF) {
            ThemeManager.setCurrentThemeAction(this);
            selected();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ThemeManager.switchTheme(this);
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
