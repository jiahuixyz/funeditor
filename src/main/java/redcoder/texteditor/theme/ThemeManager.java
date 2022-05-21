package redcoder.texteditor.theme;

import redcoder.texteditor.action.ThemeAction;
import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;

import javax.swing.*;

public class ThemeManager {

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
            System.err.printf("Failed to switch %s theme%n", theme.name);
            e.printStackTrace();
        }
    }
}
