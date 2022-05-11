package redcoder.texteditor;

import redcoder.texteditor.action.*;
import redcoder.texteditor.shortcut.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static redcoder.texteditor.action.ActionName.*;

public class EditorFrame extends JFrame {

    private static final Font MENU_DEFAULT_FONT = new Font(null, Font.BOLD, 18);
    private static final Font MENU_ITEM_DEFAULT_FONT = new Font(null, Font.ITALIC, 16);

    public EditorFrame() {
        super("简单的文本编辑器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        init();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setUndecorated(true);
        setVisible(true);
    }

    private void init() {
        // 创建文本主面板
        MainPane mainPane = new MainPane();

        // 快捷键处理器链
        ShortcutKeyHandlerChain handlerChain = new ShortcutKeyHandlerChain();

        // 快捷键监听器
        ShortcutKeyListener shortcutKeyListener = new ShortcutKeyListener(handlerChain);

        // 文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 创建默认的Action
        Map<ActionName, Action> defaultActions = createDefaultActions(mainPane, shortcutKeyListener, fileChooser);

        // 添加默认的ShortcutKeyHandler
        addDefaultShortHandlerKey(mainPane, handlerChain, defaultActions);

        // 创建默认的文本窗
        ScrollTextPane scrollTextPane = new ScrollTextPane(shortcutKeyListener);
        mainPane.addTab("* new-1", scrollTextPane);
        mainPane.addActionListener(scrollTextPane);

        // 添加菜单
        addMenu(defaultActions);
        // 添加主窗格
        getContentPane().add(mainPane);
    }

    private Map<ActionName, Action> createDefaultActions(MainPane mainPane, ShortcutKeyListener shortcutKeyListener, JFileChooser fileChooser) {
        Map<ActionName, Action> actions = new HashMap<>();
        actions.put(UNDO, new UndoActionWrapper(mainPane));
        actions.put(REDO, new RedoActionWrapper(mainPane));
        actions.put(ZOOM_IN, new ZoomInAction(mainPane));
        actions.put(ZOOM_OUT, new ZoomOutAction(mainPane));
        actions.put(NEW_FILE, new NewFileAction(mainPane, shortcutKeyListener));
        actions.put(OPEN_FILE, new OpenFileAction(mainPane, shortcutKeyListener, fileChooser));
        actions.put(SAVE_FILE, new SaveFileAction(mainPane, shortcutKeyListener, fileChooser));
        actions.put(CUT, new CutAction());
        actions.put(COPY, new CopyAction());
        actions.put(PASTE, new PasteAction());
        return actions;
    }

    private void addDefaultShortHandlerKey(MainPane mainPane,
                                           ShortcutKeyHandlerChain handlerChain,
                                           Map<ActionName, Action> defaultActions) {
        handlerChain.addHandler(new RedoHandler(mainPane));
        handlerChain.addHandler(new UndoHandler(mainPane));
        handlerChain.addHandler(new ZoomInHandler(defaultActions.get(ZOOM_IN)));
        handlerChain.addHandler(new ZoomOutHandler(defaultActions.get(ZOOM_OUT)));
        handlerChain.addHandler(new NewFileHandler(defaultActions.get(NEW_FILE)));
        handlerChain.addHandler(new OpenFileHandler(defaultActions.get(OPEN_FILE)));
        handlerChain.addHandler(new SaveFileHandler(defaultActions.get(SAVE_FILE)));
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
        // New File, Open File
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

        addMenuItem(menu, actions.get(NEW_FILE), actions.get(OPEN_FILE), actions.get(SAVE_FILE));

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
}
