package redcoder.texteditor.core;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.action.ThemeAction;
import redcoder.texteditor.core.menu.OpenRecentlyMenu;
import redcoder.texteditor.core.statusbar.EditorStatusBar;
import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.core.toolbar.EditorToolbar;
import redcoder.texteditor.theme.Theme;
import redcoder.texteditor.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static redcoder.texteditor.action.ActionName.*;

public class EditorFrame extends JFrame {

    public static final String TITLE = "A Simple Text Editor";
    public static final Font MENU_DEFAULT_FONT = new Font(null, Font.BOLD, 18);
    public static final Font MENU_ITEM_DEFAULT_FONT = new Font(null, Font.ITALIC, 16);

    private static final Object[] CLOSE_OPTIONS = {"Save All", "Don't Save", "Cancel"};

    private EditorToolbar toolBar;
    private MainTabPane mainTabPane;
    private EditorStatusBar editorStatusBar;

    public EditorFrame() {
        super(TITLE);
    }

    public void init() {
        // 创建状态栏
        editorStatusBar = new EditorStatusBar();
        // 创建主窗格
        mainTabPane = new MainTabPane(editorStatusBar);
        // 创建工具栏
        toolBar = new EditorToolbar(mainTabPane);

        Map<ActionName, KeyStroke> keyStrokes = Framework.getFrameworkShareKeyStrokes();
        Map<ActionName, Action> mergedAction = new HashMap<>();
        mergedAction.putAll(Framework.getFrameworkSharedAction());
        mergedAction.putAll(mainTabPane.getActions());

        // 添加菜单
        addMenu(keyStrokes, mergedAction);

        // 组装工具栏、主窗格和状态栏
        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.add(toolBar, BorderLayout.NORTH);
        rootPane.add(mainTabPane, BorderLayout.CENTER);
        rootPane.add(editorStatusBar, BorderLayout.SOUTH);
        setContentPane(rootPane);

        // add key bindings
        addKeyBinding(rootPane, keyStrokes, mergedAction);

        // 加载未保存的新建文件
        mainTabPane.loadUnSavedNewTextPane();

        setMinimumSize(new Dimension(700, 432));
        setSize(900, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Framework.closeWindow();
            }
        });
    }

    public boolean shouldClose() {
        if (Framework.getNumWindows() > 1) {
            List<ScrollTextPane> list = getModifiedTextPane();
            if (!list.isEmpty()) {
                String message = String.format("Do you want to save the changes to the following %d files?", list.size());
                String filenames = StringUtils.join(list.stream().map(ScrollTextPane::getFilename).collect(toList()), "\n");
                message = message + "\n\n" + filenames + "\n\n" + "Your changes will be lost if you don't save them.";
                int state = JOptionPane.showOptionDialog(mainTabPane, message, EditorFrame.TITLE, JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, CLOSE_OPTIONS, CLOSE_OPTIONS[0]);
                if (state == JOptionPane.YES_OPTION) {
                    for (ScrollTextPane pane : list) {
                        if (!pane.saveTextPane()) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return state == JOptionPane.NO_OPTION;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private List<ScrollTextPane> getModifiedTextPane() {
        List<ScrollTextPane> textPaneList = new ArrayList<>();
        for (int i = 0; i < mainTabPane.getTabCount(); i++) {
            Component component = mainTabPane.getComponentAt(i);
            if (component instanceof ScrollTextPane) {
                if (((ScrollTextPane) component).isModified()) {
                    textPaneList.add((ScrollTextPane) component);
                }
            }
        }
        return textPaneList;
    }

    public EditorStatusBar getStatusBar() {
        return editorStatusBar;
    }

    public MainTabPane getMainTabPane() {
        return mainTabPane;
    }

    // ---------- 创建菜单
    private void addMenu(Map<ActionName, KeyStroke> keyStrokes,
                         Map<ActionName, Action> mergedAction) {
        // create 'File' menu
        JMenu fileMenu = createFileMenu(keyStrokes, mergedAction);
        // create 'Edit' menu
        JMenu editMenu = createEditMenu(keyStrokes, mergedAction);
        // create 'View' menu
        JMenu viewMenu = createViewMenu(keyStrokes, mergedAction);
        // create 'Theme' menu
        JMenu themeMenu = createThemeMenu();

        // set menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(themeMenu);
        setJMenuBar(menuBar);
    }

    private JMenu createFileMenu(Map<ActionName, KeyStroke> keyStrokes,
                                 Map<ActionName, Action> mergedAction) {
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

        // new file
        addMenuItem(menu, keyStrokes.get(NEW_FILE), mergedAction.get(NEW_FILE));

        // open file
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(OPEN_FILE), mergedAction.get(OPEN_FILE));
        // open recently
        OpenRecentlyMenu recentlyMenu = new OpenRecentlyMenu(mainTabPane);
        menu.add(recentlyMenu);

        // save & save as & save all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(SAVE_FILE), mergedAction.get(SAVE_FILE),
                keyStrokes.get(SAVE_AS_FILE), mergedAction.get(SAVE_AS_FILE),
                keyStrokes.get(SAVE_ALL), mergedAction.get(SAVE_ALL));

        // close & close all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CLOSE), mergedAction.get(CLOSE),
                keyStrokes.get(CLOSE_ALL), mergedAction.get(CLOSE_ALL));

        // new window & close window
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(NEW_WINDOW), mergedAction.get(NEW_WINDOW),
                keyStrokes.get(CLOSE_WINDOW), mergedAction.get(CLOSE_WINDOW));

        // exit
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(EXIT), mergedAction.get(EXIT));

        return menu;
    }

    private JMenu createEditMenu(Map<ActionName, KeyStroke> keyStrokes,
                                 Map<ActionName, Action> mergedAction) {
        JMenu menu = new JMenu("Edit");
        menu.setFont(MENU_DEFAULT_FONT);

        // undo & redo
        addMenuItem(menu, keyStrokes.get(UNDO), mergedAction.get(UNDO),
                keyStrokes.get(REDO), mergedAction.get(REDO));

        // cut, copy, paste
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CUT), mergedAction.get(CUT),
                keyStrokes.get(COPY), mergedAction.get(COPY),
                keyStrokes.get(PASTE), mergedAction.get(PASTE));

        return menu;
    }

    private JMenu createViewMenu(Map<ActionName, KeyStroke> keyStrokes,
                                 Map<ActionName, Action> mergedAction) {
        JMenu menu = new JMenu("View");
        menu.setFont(MENU_DEFAULT_FONT);

        // zoom in & zoom out
        addMenuItem(menu, keyStrokes.get(ZOOM_IN), mergedAction.get(ZOOM_IN),
                keyStrokes.get(ZOOM_OUT), mergedAction.get(ZOOM_OUT));

        // line wrap
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(LINE_WRAP), mergedAction.get(LINE_WRAP));

        return menu;
    }

    private JMenu createThemeMenu() {
        JMenu menu = new JMenu("Theme");
        menu.setFont(MENU_DEFAULT_FONT);
        addMenuItem(menu, new ThemeAction(Theme.JAVA_METAL), new ThemeAction(Theme.FOLLOW_SYSTEM));
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

    private void addKeyBinding(JPanel rootPane,
                               Map<ActionName, KeyStroke> keyStrokes,
                               Map<ActionName, Action> mergedAction) {
        // register action
        ActionMap actionMap = rootPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : mergedAction.entrySet()) {
            actionMap.put(entry.getKey(), entry.getValue());
        }
        // add key binding
        InputMap inputMap = rootPane.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStrokes.get(ZOOM_IN), ZOOM_IN);
        inputMap.put(keyStrokes.get(ZOOM_OUT), ZOOM_OUT);
        inputMap.put(keyStrokes.get(NEW_FILE), NEW_FILE);
        inputMap.put(keyStrokes.get(OPEN_FILE), OPEN_FILE);
        inputMap.put(keyStrokes.get(SAVE_FILE), SAVE_FILE);
        inputMap.put(keyStrokes.get(SAVE_ALL), SAVE_ALL);
        inputMap.put(keyStrokes.get(CLOSE), CLOSE);
        inputMap.put(keyStrokes.get(CLOSE_ALL), CLOSE_ALL);
        inputMap.put(keyStrokes.get(EXIT), EXIT);
        inputMap.put(keyStrokes.get(LINE_WRAP), LINE_WRAP);
        inputMap.put(keyStrokes.get(NEW_WINDOW), NEW_WINDOW);
        inputMap.put(keyStrokes.get(CLOSE_WINDOW), CLOSE_WINDOW);
    }
}
