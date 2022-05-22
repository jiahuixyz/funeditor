package redcoder.texteditor.theme;

import redcoder.texteditor.action.ThemeAction;
import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThemeManager {

    private static final Logger LOGGER = Logger.getLogger(ThemeManager.class.getName());

    private static ThemeAction currentThemeAction;

    public static ThemeAction getCurrentThemeAction() {
        return currentThemeAction;
    }

    public static void setCurrentThemeAction(ThemeAction action) {
        currentThemeAction = action;
    }

    public static void switchTheme(ThemeAction action) {
        Theme theme = action.getTheme();
        if (UIManager.getLookAndFeel().getClass().getName().equals(theme.clazz)) {
            return;
        }

        try {
            UIManager.setLookAndFeel(theme.clazz);
            for (EditorFrame frame : Framework.getOpenedFrame()) {
                SwingUtilities.updateComponentTreeUI(frame);
            }
            action.selected();

            currentThemeAction.unselected();
            currentThemeAction = action;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("Failed to switch %s theme", theme.name), e);
        }
    }
}
