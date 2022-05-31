package redcoder.texteditor.core.menu;

import redcoder.texteditor.core.action.ActionName;
import redcoder.texteditor.core.action.ThemeAction;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static redcoder.texteditor.core.action.ActionName.*;

public class EditorMenuBar extends JMenuBar {

    static final Font MENU_DEFAULT_FONT = new Font(null, Font.BOLD, 18);
    static final Font MENU_ITEM_DEFAULT_FONT = new Font(null, Font.ITALIC, 16);

    public EditorMenuBar(Map<ActionName, KeyStroke> keyStrokes,
                         Map<ActionName, Action> action,
                         TabPane tabPane) {
        init(keyStrokes, action, tabPane);
    }

    // ---------- 创建菜单
    private void init(Map<ActionName, KeyStroke> keyStrokes,
                      Map<ActionName, Action> action,
                      TabPane tabPane) {
        // create 'File' menu
        add(createFileMenu(keyStrokes, action, tabPane));
        // create 'Edit' menu
        add(createEditMenu(keyStrokes, action));
        // create 'View' menu
        add(createViewMenu(keyStrokes, action));
        // create 'Theme' menu
        add(createThemeMenu());
        // create 'Help' menu
        add(createHelpMenu(action));
    }

    private JMenu createFileMenu(Map<ActionName, KeyStroke> keyStrokes,
                                 Map<ActionName, Action> action,
                                 TabPane tabPane) {
        JMenu menu = createMenu("File");

        // new file
        addMenuItem(menu, keyStrokes.get(NEW_FILE), action.get(NEW_FILE));

        // open file
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(OPEN_FILE), action.get(OPEN_FILE));
        // open recently
        OpenRecentlyMenu recentlyMenu = new OpenRecentlyMenu(tabPane);
        menu.add(recentlyMenu);

        // save & save as & save all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(SAVE_FILE), action.get(SAVE_FILE),
                keyStrokes.get(SAVE_AS_FILE), action.get(SAVE_AS_FILE),
                keyStrokes.get(SAVE_ALL), action.get(SAVE_ALL));

        // close & close all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CLOSE), action.get(CLOSE),
                keyStrokes.get(CLOSE_ALL), action.get(CLOSE_ALL));

        // new window & close window
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(NEW_WINDOW), action.get(NEW_WINDOW),
                keyStrokes.get(CLOSE_WINDOW), action.get(CLOSE_WINDOW));

        // exit
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(EXIT), action.get(EXIT));

        return menu;
    }

    private JMenu createEditMenu(Map<ActionName, KeyStroke> keyStrokes,
                                 Map<ActionName, Action> action) {
        JMenu menu = createMenu("Edit");

        // undo & redo
        addMenuItem(menu, keyStrokes.get(UNDO), action.get(UNDO),
                keyStrokes.get(REDO), action.get(REDO));

        // cut, copy, paste
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CUT), action.get(CUT),
                keyStrokes.get(COPY), action.get(COPY),
                keyStrokes.get(PASTE), action.get(PASTE));

        // find, replace
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(FIND), action.get(FIND),
                keyStrokes.get(REPLACE), action.get(REPLACE));

        return menu;
    }

    private JMenu createViewMenu(Map<ActionName, KeyStroke> keyStrokes,
                                 Map<ActionName, Action> action) {
        JMenu menu = createMenu("View");

        // zoom in & zoom out
        addMenuItem(menu, keyStrokes.get(ZOOM_IN), action.get(ZOOM_IN),
                keyStrokes.get(ZOOM_OUT), action.get(ZOOM_OUT));

        // line wrap
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(LINE_WRAP), action.get(LINE_WRAP));

        return menu;
    }

    private JMenu createThemeMenu() {
        JMenu menu = createMenu("Theme");
        for (Theme theme : Theme.values()) {
            addMenuItem(menu, new ThemeAction(theme));
        }
        return menu;
    }

    private JMenu createHelpMenu(Map<ActionName, Action> action) {
        JMenu menu = createMenu("Help");
        addMenuItem(menu, action.get(ABOUT));
        return menu;
    }

    private JMenu createMenu(String menuName) {
        JMenu menu = new JMenu(menuName);
        menu.setFont(MENU_DEFAULT_FONT);
        return menu;
    }

    private void addMenuItem(JMenu menu, Object... objs) {
        for (int i = 0; i < objs.length; i++) {
            KeyStroke keyStroke = (KeyStroke) objs[i++];
            Action action = (Action) objs[i];

            JMenuItem menuItem = menu.add(action);
            menuItem.setAccelerator(keyStroke);
            menuItem.setFont(MENU_ITEM_DEFAULT_FONT);
        }
    }

    private void addMenuItem(JMenu menu, Action... actions) {
        for (Action action : actions) {
            JMenuItem menuItem = menu.add(action);
            menuItem.setFont(MENU_ITEM_DEFAULT_FONT);
        }
    }
}
