package redcoder.texteditor;

import redcoder.texteditor.action.*;

import javax.swing.*;
import java.awt.*;

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

        // 按键监听器
        ShortcutKeyListener shortcutKeyListener = new ShortcutKeyListener(mainPane);

        // 创建默认的文本窗
        TextPane textPane = new TextPane(shortcutKeyListener);
        mainPane.addTab("* new-1", textPane);
        mainPane.addActionListener(textPane);

        // create file menu
        JMenu fileMenu = createFileMenu(mainPane, shortcutKeyListener);
        // create edit menu
        JMenu editMenu = createEditMenu(mainPane);
        // create view menu
        JMenu viewMenu = createViewMenu(mainPane, shortcutKeyListener);

        // set menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        getContentPane().add(mainPane);
    }


    private JMenu createFileMenu(MainPane mainPane, ShortcutKeyListener shortcutKeyListener) {
        // New File, Open File
        JMenu menu = new JMenu("File");
        menu.setFont(MENU_DEFAULT_FONT);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        NewFileAction newFileAction = new NewFileAction(mainPane, shortcutKeyListener);
        OpenFileAction openFileAction = new OpenFileAction(mainPane, shortcutKeyListener, fileChooser);
        SaveFileAction saveFileAction = new SaveFileAction(mainPane, shortcutKeyListener, fileChooser);
        addMenuItem(menu, newFileAction, openFileAction, saveFileAction);

        shortcutKeyListener.setNewFileAction(newFileAction);
        shortcutKeyListener.setOpenFileAction(openFileAction);
        shortcutKeyListener.setSaveFileAction(saveFileAction);

        return menu;
    }

    private JMenu createEditMenu(MainPane mainPane) {
        // undo, redo, cut, copy, paste
        JMenu menu = new JMenu("Edit");
        menu.setFont(MENU_DEFAULT_FONT);
        addMenuItem(menu, new UndoActionWrapper(mainPane), new RedoActionWrapper(mainPane));
        menu.addSeparator();
        addMenuItem(menu, new CutAction(), new CopyAction(), new PasteAction());
        return menu;
    }

    private JMenu createViewMenu(MainPane mainPane, ShortcutKeyListener shortcutKeyListener) {
        // zoom in, zoom out
        JMenu menu = new JMenu("View");
        menu.setFont(MENU_DEFAULT_FONT);
        ZoomInAction zoomInAction = new ZoomInAction(mainPane);
        ZoomOutAction zoomOutAction = new ZoomOutAction(mainPane);
        addMenuItem(menu, zoomInAction, zoomOutAction);

        shortcutKeyListener.setZoomInAction(zoomInAction);
        shortcutKeyListener.setZoomOutAction(zoomOutAction);

        return menu;
    }

    private void addMenuItem(JMenu menu, Action... actions) {
        for (Action action : actions) {
            JMenuItem menuItem = menu.add(action);
            menuItem.setFont(MENU_ITEM_DEFAULT_FONT);
        }
    }
}
