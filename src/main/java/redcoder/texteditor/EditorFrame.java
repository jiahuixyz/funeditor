package redcoder.texteditor;

import redcoder.texteditor.action.ActionName;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

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
        getContentPane().add(mainPane);
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

        addMenuItem(menu, actions.get(NEW_FILE), actions.get(OPEN_FILE), actions.get(SAVE_FILE),
                actions.get(CLOSE), actions.get(CLOSE_ALL));

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
