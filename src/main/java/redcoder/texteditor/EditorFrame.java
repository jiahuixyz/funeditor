package redcoder.texteditor;

import redcoder.texteditor.action.*;

import javax.swing.*;

public class EditorFrame extends JFrame {

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
        MultiKeyPressListener multiKeyPressListener = new MultiKeyPressListener(mainPane);

        // 创建默认的文本窗
        TextPane textPane = new TextPane(multiKeyPressListener);
        mainPane.addTab("new-1", textPane);
        mainPane.addActionListener(textPane);

        // create edit menu
        JMenu editMenu = createEditMenu(mainPane);
        // create view menu
        JMenu viewMenu = createViewMenu(mainPane, multiKeyPressListener);

        // set menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        getContentPane().add(mainPane);
    }


    private JMenu createFileMenu() {
        // TODO: 2022/5/10
        return null;
    }

    private JMenu createEditMenu(MainPane mainPane) {
        // undo, redo, cut, copy, paste
        JMenu menu = new JMenu("Edit");
        menu.add(new UndoActionWrapper(mainPane));
        menu.add(new RedoActionWrapper(mainPane));
        menu.addSeparator();
        menu.add(new CutAction());
        menu.add(new CopyAction());
        menu.add(new PasteAction());
        return menu;
    }

    private JMenu createViewMenu(MainPane mainPane, MultiKeyPressListener multiKeyPressListener) {
        // zoom in, zoom out
        JMenu menu = new JMenu("View");
        ZoomInAction zoomInAction = new ZoomInAction(mainPane);
        menu.add(zoomInAction);
        ZoomOutAction zoomOutAction = new ZoomOutAction(mainPane);
        menu.add(zoomOutAction);

        multiKeyPressListener.setZoomInAction(zoomInAction);
        multiKeyPressListener.setZoomOutAction(zoomOutAction);

        return menu;
    }
}
