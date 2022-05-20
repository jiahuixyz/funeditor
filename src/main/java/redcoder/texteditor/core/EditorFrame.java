package redcoder.texteditor.core;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.action.ThemeAction;
import redcoder.texteditor.core.menu.OpenRecentlyMenu;
import redcoder.texteditor.core.statusbar.EditorStatusBar;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.core.toolbar.EditorToolbar;
import redcoder.texteditor.theme.Theme;
import redcoder.texteditor.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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
    private TabPane tabPane;
    private EditorStatusBar statusBar;

    private JDialog findDialog;
    private JDialog replaceDialog;

    public EditorFrame() {
        super(TITLE);
    }

    public void createAndShowGUI() {
        JPanel rootPane = new JPanel();
        rootPane.setLayout(new BorderLayout());
        setContentPane(rootPane);

        // 创建工具栏
        toolBar = new EditorToolbar();
        // 创建状态栏
        statusBar = new EditorStatusBar();
        // 创建主窗格
        // TODO: 2022/5/18 remove constructor param?
        tabPane = new TabPane(statusBar);

        Map<ActionName, KeyStroke> keyStrokes = Framework.getKeyStrokes();
        Map<ActionName, Action> action = Framework.getActions();

        // 添加菜单
        addMenu(keyStrokes, action);

        // 组装工具栏、主窗格和状态栏
        rootPane.add(toolBar, BorderLayout.NORTH);
        rootPane.add(tabPane, BorderLayout.CENTER);
        rootPane.add(statusBar, BorderLayout.SOUTH);

        // add key bindings
        addKeyBinding(rootPane, keyStrokes, action);

        // 加载未保存的新建文件
        tabPane.loadUnSavedNewTextPane();

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
                int state = JOptionPane.showOptionDialog(tabPane, message, EditorFrame.TITLE, JOptionPane.YES_NO_CANCEL_OPTION,
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
        for (int i = 0; i < tabPane.getTabCount(); i++) {
            Component component = tabPane.getComponentAt(i);
            if (component instanceof ScrollTextPane) {
                if (((ScrollTextPane) component).isModified()) {
                    textPaneList.add((ScrollTextPane) component);
                }
            }
        }
        return textPaneList;
    }

    // ---------- 创建菜单
    private void addMenu(Map<ActionName, KeyStroke> keyStrokes,
                         Map<ActionName, Action> action) {
        // create 'File' menu
        JMenu fileMenu = createFileMenu(keyStrokes, action);
        // create 'Edit' menu
        JMenu editMenu = createEditMenu(keyStrokes, action);
        // create 'View' menu
        JMenu viewMenu = createViewMenu(keyStrokes, action);
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
                                 Map<ActionName, Action> action) {
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

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
        JMenu menu = new JMenu("Edit");
        menu.setFont(MENU_DEFAULT_FONT);

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
        JMenu menu = new JMenu("View");
        menu.setFont(MENU_DEFAULT_FONT);

        // zoom in & zoom out
        addMenuItem(menu, keyStrokes.get(ZOOM_IN), action.get(ZOOM_IN),
                keyStrokes.get(ZOOM_OUT), action.get(ZOOM_OUT));

        // line wrap
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(LINE_WRAP), action.get(LINE_WRAP));

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

    private void addKeyBinding(JComponent rootPane,
                               Map<ActionName, KeyStroke> keyStrokes,
                               Map<ActionName, Action> action) {
        // register action
        ActionMap actionMap = rootPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : action.entrySet()) {
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

    public EditorToolbar getToolBar() {
        return toolBar;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public EditorStatusBar getStatusBar() {
        return statusBar;
    }

    public JDialog getFindDialog() {
        return findDialog;
    }

    public void setFindDialog(JDialog findDialog) {
        this.findDialog = findDialog;
    }

    public JDialog getReplaceDialog() {
        return replaceDialog;
    }

    public void setReplaceDialog(JDialog replaceDialog) {
        this.replaceDialog = replaceDialog;
    }
}
