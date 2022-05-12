package redcoder.texteditor;

import redcoder.texteditor.action.ActionName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static redcoder.texteditor.action.ActionName.*;

public class EditorFrame extends JFrame {

    public static final String TITLE = "A Simple Text Editor";

    private static final Font MENU_DEFAULT_FONT = new Font(null, Font.BOLD, 18);
    private static final Font MENU_ITEM_DEFAULT_FONT = new Font(null, Font.ITALIC, 16);

    public EditorFrame() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        init();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void init() {
        // 创建文本主面板
        MainPane mainPane = new MainPane();

        // 创建默认的文本窗
        ScrollTextPane scrollTextPane = new ScrollTextPane(mainPane, "new-1");
        mainPane.addTab("new-1", scrollTextPane);

        // 添加菜单
        addMenu(mainPane.getKeyStrokes(), mainPane.getActions());
        // 添加主窗格
        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.add(mainPane, BorderLayout.CENTER);
        setContentPane(rootPane);
        // add key bindings
        addDefaultKeyBinding(rootPane, mainPane.getKeyStrokes(), mainPane.getActions());
    }


    // ---------- 创建菜单
    private void addMenu(Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions) {
        // create 'File' menu
        JMenu fileMenu = createFileMenu(keyStrokes, actions);
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

    private JMenu createFileMenu(Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions) {
        // New File, Open File, Close File, Close All File
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

        addMenuItem(menu, keyStrokes.get(NEW_FILE), actions.get(NEW_FILE),
                keyStrokes.get(OPEN_FILE), actions.get(OPEN_FILE));
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(SAVE_FILE), actions.get(SAVE_FILE),
                keyStrokes.get(SAVE_ALL), actions.get(SAVE_ALL));
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CLOSE), actions.get(CLOSE),
                keyStrokes.get(CLOSE_ALL), actions.get(CLOSE_ALL));

        return menu;
    }

    private JMenu createEditMenu(Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions) {
        // undo, redo, cut, copy, paste
        JMenu menu = new JMenu("Edit");
        menu.setFont(MENU_DEFAULT_FONT);

        addMenuItem(menu, keyStrokes.get(UNDO), actions.get(UNDO),
                keyStrokes.get(REDO), actions.get(REDO));
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CUT), actions.get(CUT),
                keyStrokes.get(COPY), actions.get(COPY),
                keyStrokes.get(PASTE), actions.get(PASTE));

        return menu;
    }

    private JMenu createViewMenu(Map<ActionName, KeyStroke> keyStrokes, Map<ActionName, Action> actions) {
        // zoom in, zoom out
        JMenu menu = new JMenu("View");
        menu.setFont(MENU_DEFAULT_FONT);

        addMenuItem(menu, keyStrokes.get(ZOOM_IN), actions.get(ZOOM_IN),
                keyStrokes.get(ZOOM_OUT), actions.get(ZOOM_OUT));

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
    }
}
