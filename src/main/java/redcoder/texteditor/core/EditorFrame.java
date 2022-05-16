package redcoder.texteditor.core;

import org.apache.commons.lang3.StringUtils;
import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.core.menu.OpenRecentlyMenu;
import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.core.statusbar.StatusBar;

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

    private StatusBar statusBar;
    private MainTabPane mainTabPane;

    public EditorFrame() {
        super(TITLE);
    }

    public void init() {
        // 创建底部状态栏
        statusBar = new StatusBar();
        // 创建文本主面板
        mainTabPane = new MainTabPane(statusBar);

        // 添加菜单
        addMenu();
        // 添加主窗格和状态栏
        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.add(mainTabPane, BorderLayout.CENTER);
        rootPane.add(statusBar, BorderLayout.SOUTH);
        setContentPane(rootPane);
        // add key bindings
        addDefaultKeyBinding(rootPane);

        // 加载未保存的新建文件
        mainTabPane.loadUnSavedNewTextPane();

        setMinimumSize(new Dimension(700, 432));
        setSize(900, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Framework.INSTANCE.closeWindow();
            }
        });
    }

    public boolean shouldClose() {
        if (Framework.INSTANCE.getNumWindows() > 1) {
            List<ScrollTextPane> list = getModifiedTextPane();
            if (!list.isEmpty()) {
                String message = String.format("Do you want to save the changes to the following %d files?", list.size());
                String filenames = StringUtils.join(list.stream().map(ScrollTextPane::getFilename).collect(toList()), '\n');
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

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public MainTabPane getMainTabPane() {
        return mainTabPane;
    }

    // ---------- 创建菜单
    private void addMenu() {
        Map<ActionName, KeyStroke> keyStrokes = mainTabPane.getActionCollection().getKeyStrokes();
        Map<ActionName, Action> actions = mainTabPane.getActionCollection().getActions();

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
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

        // new file
        addMenuItem(menu, keyStrokes.get(NEW_FILE), actions.get(NEW_FILE));

        // open file
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(OPEN_FILE), actions.get(OPEN_FILE));
        // open recently
        OpenRecentlyMenu recentlyMenu = new OpenRecentlyMenu(mainTabPane);
        menu.add(recentlyMenu);

        // save & save all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(SAVE_FILE), actions.get(SAVE_FILE),
                keyStrokes.get(SAVE_ALL), actions.get(SAVE_ALL));

        // close & close all
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(CLOSE), actions.get(CLOSE),
                keyStrokes.get(CLOSE_ALL), actions.get(CLOSE_ALL));

        // new window & close window
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(NEW_WINDOW), actions.get(NEW_WINDOW),
                keyStrokes.get(CLOSE_WINDOW), actions.get(CLOSE_WINDOW));

        // exit
        menu.addSeparator();
        addMenuItem(menu, keyStrokes.get(EXIT), actions.get(EXIT));

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

    private void addDefaultKeyBinding(JPanel rootPane) {
        Map<ActionName, KeyStroke> keyStrokes = mainTabPane.getActionCollection().getKeyStrokes();
        Map<ActionName, Action> actions = mainTabPane.getActionCollection().getActions();

        // register action
        ActionMap actionMap = rootPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : actions.entrySet()) {
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
