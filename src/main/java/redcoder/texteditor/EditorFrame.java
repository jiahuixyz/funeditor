package redcoder.texteditor;

import redcoder.texteditor.action.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
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
        mainPane.addActionListener(scrollTextPane);

        // 添加菜单
        addMenu(mainPane.getDefaultActions());
        // 添加主窗格
        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.add(mainPane, BorderLayout.CENTER);
        setContentPane(rootPane);
        // add key bindings
        addDefaultKeyBinding(rootPane, mainPane.getDefaultActions());
    }


    // ---------- 添加菜单
    private void addMenu(Map<ActionName, Action> defaultActions) {
        // create 'File' menu
        JMenu fileMenu = createFileMenu(defaultActions);
        // create 'Edit' menu
        JMenu editMenu = createEditMenu(defaultActions);
        // create 'View' menu
        JMenu viewMenu = createViewMenu(defaultActions);

        // set menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private JMenu createFileMenu(Map<ActionName, Action> actions) {
        // New File, Open File, Close File, Close All File
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

        addMenuItem(menu, actions.get(NEW_FILE), actions.get(OPEN_FILE));
        menu.addSeparator();
        addMenuItem(menu, actions.get(SAVE_FILE), actions.get(SAVE_ALL));
        menu.addSeparator();
        addMenuItem(menu, actions.get(CLOSE), actions.get(CLOSE_ALL));

        return menu;
    }

    private JMenu createEditMenu(Map<ActionName, Action> actions) {
        // undo, redo, cut, copy, paste
        JMenu menu = new JMenu("Edit");
        menu.setFont(MENU_DEFAULT_FONT);

        addMenuItem(menu, actions.get(UNDO), actions.get(REDO));
        menu.addSeparator();
        addMenuItem(menu, actions.get(CUT), actions.get(COPY), actions.get(PASTE));

        return menu;
    }

    private JMenu createViewMenu(Map<ActionName, Action> actions) {
        // zoom in, zoom out
        JMenu menu = new JMenu("View");
        menu.setFont(MENU_DEFAULT_FONT);

        addMenuItem(menu, actions.get(ZOOM_IN), actions.get(ZOOM_OUT));

        return menu;
    }

    private void addMenuItem(JMenu menu, Action... actions) {
        for (Action action : actions) {
            JMenuItem menuItem = menu.add(action);
            menuItem.setFont(MENU_ITEM_DEFAULT_FONT);
        }
    }

    private Map<ActionName, Action> createDefaultActions(MainPane mainPane) {
        Map<ActionName, Action> actions = new HashMap<>();
        actions.put(UNDO, new UndoActionWrapper(mainPane));
        actions.put(REDO, new RedoActionWrapper(mainPane));
        actions.put(ZOOM_IN, new ZoomInAction(mainPane));
        actions.put(ZOOM_OUT, new ZoomOutAction(mainPane));
        actions.put(NEW_FILE, new NewAction(mainPane));
        actions.put(OPEN_FILE, new OpenAction(mainPane));
        actions.put(SAVE_FILE, new SaveAction(mainPane));
        actions.put(SAVE_ALL, new SaveAllAction(mainPane));
        actions.put(CUT, new CutAction());
        actions.put(COPY, new CopyAction());
        actions.put(PASTE, new PasteAction());
        actions.put(CLOSE, new CloseAction(mainPane));
        actions.put(CLOSE_ALL, new CloseAllAction(mainPane));
        return actions;
    }

    private void addDefaultKeyBinding(JPanel rootPane, Map<ActionName, Action> defaultActions) {
        ActionMap actionMap = rootPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : defaultActions.entrySet()) {
            actionMap.put(entry.getKey(), entry.getValue());
        }

        InputMap inputMap = rootPane.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK), ZOOM_IN);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK), ZOOM_OUT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), NEW_FILE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), OPEN_FILE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), SAVE_FILE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), SAVE_ALL);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), CLOSE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), CLOSE_ALL);
    }
}
