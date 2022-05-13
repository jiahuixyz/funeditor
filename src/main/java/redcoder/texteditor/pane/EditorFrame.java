package redcoder.texteditor.pane;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.openrecently.OpenRecentlyMenu;
import redcoder.texteditor.statusbar.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static redcoder.texteditor.action.ActionName.*;

public class EditorFrame extends JFrame {

    public static final String TITLE = "A Simple Text Editor";
    public static final Font MENU_DEFAULT_FONT = new Font(null, Font.BOLD, 18);
    public static final Font MENU_ITEM_DEFAULT_FONT = new Font(null, Font.ITALIC, 16);

    public EditorFrame() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        init();

        setMinimumSize(new Dimension(700, 432));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void init() {
        // 创建文本主面板
        MainPane mainPane = new MainPane();
        // 创建底部状态栏
        StatusBar statusBar = new StatusBar();

        // 添加菜单
        addMenu(mainPane);
        // 添加主窗格和状态栏
        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.add(mainPane, BorderLayout.CENTER);
        rootPane.add(statusBar, BorderLayout.SOUTH);
        setContentPane(rootPane);
        // add key bindings
        addDefaultKeyBinding(rootPane, mainPane.getKeyStrokes(), mainPane.getActions());

        // 加载未保存的新建文件
        if (!mainPane.loadUnSavedNewFile()) {
            // 创建默认的文本窗
            ScrollTextPane scrollTextPane = new ScrollTextPane(mainPane, "new-1");
            mainPane.addTab("new-1", scrollTextPane, true);
        }
    }


    // ---------- 创建菜单
    private void addMenu(MainPane mainPane) {
        Map<ActionName, KeyStroke> keyStrokes = mainPane.getKeyStrokes();
        Map<ActionName, Action> actions = mainPane.getActions();

        // create 'File' menu
        JMenu fileMenu = createFileMenu(keyStrokes, actions, mainPane);
        // create 'Edit' menu
        JMenu editMenu = createEditMenu(keyStrokes, actions);
        // create 'View' menu
        JMenu viewMenu = createViewMenu(keyStrokes, actions);

        // set menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private JMenu createFileMenu(Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions, MainPane mainPane) {
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

        // new file
        addMenuItem(menu, keyStrokes.get(NEW_FILE), actions.get(NEW_FILE));

        // open file
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(OPEN_FILE), actions.get(OPEN_FILE));
        // open recently
        OpenRecentlyMenu recentlyMenu = new OpenRecentlyMenu(mainPane);
        mainPane.setOpenRecentlyMenu(recentlyMenu);
        menu.add(recentlyMenu);

        // save & save all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(SAVE_FILE), actions.get(SAVE_FILE),
                keyStrokes.get(SAVE_ALL), actions.get(SAVE_ALL));

        // close & close all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CLOSE), actions.get(CLOSE),
                keyStrokes.get(CLOSE_ALL), actions.get(CLOSE_ALL));

        // exit
        menu.addSeparator();
        addMenuItem(menu, actions.get(EXIT));

        return menu;
    }

    private JMenu createEditMenu(Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions) {
        JMenu menu = new JMenu("Edit");
        menu.setFont(MENU_DEFAULT_FONT);

        // undo & redo
        addMenuItem(menu, keyStrokes.get(UNDO), actions.get(UNDO),
                keyStrokes.get(REDO), actions.get(REDO));

        // cut, copy, paste
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CUT), actions.get(CUT),
                keyStrokes.get(COPY), actions.get(COPY),
                keyStrokes.get(PASTE), actions.get(PASTE));

        return menu;
    }

    private JMenu createViewMenu(Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions) {
        JMenu menu = new JMenu("View");
        menu.setFont(MENU_DEFAULT_FONT);

        // zoom in
        addMenuItem(menu, keyStrokes.get(ZOOM_IN), actions.get(ZOOM_IN),
                keyStrokes.get(ZOOM_OUT), actions.get(ZOOM_OUT));

        // zoom out
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(LINE_WRAP), actions.get(LINE_WRAP));

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

    private void addDefaultKeyBinding(JPanel rootPane, Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions) {
        ActionMap actionMap = rootPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : actions.entrySet()) {
            actionMap.put(entry.getKey(), entry.getValue());
        }

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
    }
}
