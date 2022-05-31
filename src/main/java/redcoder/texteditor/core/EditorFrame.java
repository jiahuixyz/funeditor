package redcoder.texteditor.core;

import redcoder.texteditor.core.action.ActionName;
import redcoder.texteditor.core.menu.EditorMenuBar;
import redcoder.texteditor.core.statusbar.EditorStatusBar;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.toolbar.EditorToolbar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static redcoder.texteditor.core.action.ActionName.*;

public class EditorFrame extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(EditorFrame.class.getName());
    public static final String TITLE = "Rc Text Editor";

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

        // 添加菜单栏
        EditorMenuBar menuBar = new EditorMenuBar(keyStrokes, action, tabPane);
        setJMenuBar(menuBar);

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

        if (LOGO != null) {
            setIconImage(LOGO);
        }
    }

    /**
     * 当前窗口是否可以关闭
     */
    public boolean canCloseNormally() {
        return Framework.getNumWindows() <= 1 || tabPane.canCloseNormally();
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

    private static Image LOGO = null;

    static {
        try {
            URL url = EditorFrame.class.getClassLoader().getResource("images/logo.png");
            if (url != null) {
                LOGO = ImageIO.read(url);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "load logo error", e);
        }
    }
}
