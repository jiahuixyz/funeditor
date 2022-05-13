package redcoder.texteditor;

import redcoder.texteditor.action.*;
import redcoder.texteditor.statusbar.CaretStatusIndicator;
import redcoder.texteditor.statusbar.StatusBar;
import redcoder.texteditor.statusbar.TextFontSizeIndicator;
import redcoder.texteditor.statusbar.TextLengthIndicator;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.awt.event.KeyEvent.*;
import static redcoder.texteditor.action.ActionName.*;

/**
 * 编辑器主窗格
 */
public class MainPane extends JTabbedPane {

    public static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 20);

    private static final int FONT_SIZE_MINIMUM = 10;
    private static final int FONT_SIZE_MAXIMUM = 100;

    private ScrollTextPane selectedScrollTextPane;
    private JFileChooser fileChooser;
    private Map<ActionName, KeyStroke> keyStrokes;
    private Map<ActionName, Action> actions;
    // font used by ScrollTextPane's all instance
    private Font stpFont = DEFAULT_FONT;

    private OpenedFilesRecently ofr;
    private OpenRecentlyMenu openRecentlyMenu;
    private Map<String, ScrollTextPane> addedFileTabbedIndex = new HashMap<>();

    public MainPane() {
        init();
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        // 设置自定义的TabComponent
        ButtonTabComponent buttonTabComponent = new ButtonTabComponent(this, title);
        this.setTabComponentAt(index, buttonTabComponent);
        if (component instanceof ScrollTextPane) {
            ScrollTextPane scrollTextPane = (ScrollTextPane) component;
            scrollTextPane.setButtonTabComponent(buttonTabComponent);
        }
    }


    // ------------ operation about font

    /**
     * 放大字体（当前仅放大编辑窗口内的文本字体）
     */
    public void zoomInFont() {
        int newSize = Math.min(stpFont.getSize() + 2, FONT_SIZE_MAXIMUM);
        stpFont = new Font(stpFont.getName(), stpFont.getStyle(), newSize);
        setChildComponentFont();
    }

    /**
     * 缩小字体（当前仅缩小编辑窗口内的文本字体）
     */
    public void zoomOutFont() {
        int newSize = Math.max(stpFont.getSize() - 2, FONT_SIZE_MINIMUM);
        stpFont = new Font(stpFont.getName(), stpFont.getStyle(), newSize);
        setChildComponentFont();
    }

    private void setChildComponentFont() {
        for (Component component : getComponents()) {
            if (component instanceof ScrollTextPane) {
                ScrollTextPane scrollTextPane = (ScrollTextPane) component;
                scrollTextPane.setFont(stpFont);
            }
        }
        TextFontSizeIndicator.INDICATOR.refresh(stpFont);
    }


    // -------------  operation about file

    /**
     * 打开用户选择的文件
     *
     * @return true：打开成功，false：打开失败
     */
    public boolean openFile() {
        // FIXME: 2022/5/13 打开相同的文件
        int state = fileChooser.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            openFile(file);
            return true;
        }
        return false;
    }

    /**
     * 打开指定的文件
     *
     * @param filepath 文件路径
     * @return true：打开成功，false：打开失败
     */
    public boolean openFile(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return false;
        }
        openFile(file);
        return true;
    }

    private void openFile(File file) {
        ScrollTextPane scrollTextPane = addedFileTabbedIndex.get(file.getAbsolutePath());
        if (scrollTextPane != null) {
            // 文件已打开，切换到文件所在的tab即可
            setSelectedComponent(scrollTextPane);
            // for resort menu item
            openRecentlyMenu.addOrMoveToFirst(file.getAbsolutePath());
            return;
        }

        String filename = file.getName();
        String content = FileUtils.readFile(file);

        if (selectedScrollTextPane != null
                && selectedScrollTextPane.getFile() == null
                && !selectedScrollTextPane.isModified()) {
            scrollTextPane = selectedScrollTextPane;
            scrollTextPane.setText(content);
            scrollTextPane.setFile(file);
            scrollTextPane.updateTabbedTitle(filename);
        } else {
            scrollTextPane = new ScrollTextPane(this, file);
            scrollTextPane.setText(content);
            addTab(filename, scrollTextPane);
            setSelectedComponent(scrollTextPane);
        }


        // 添加最近打开列表中
        ofr.addFile(file);
        openRecentlyMenu.addOrMoveToFirst(file.getAbsolutePath());
    }

    /**
     * 保存所有文本窗格内的文件。
     *
     * @return 只有全部保存成功才返回true，否则返回false。
     */
    public boolean saveAllTextPane() {
        for (int i = this.getTabCount() - 1; i >= 0; i--) {
            // switch to tab i
            this.setSelectedIndex(i);
            Component component = this.getComponentAt(i);
            if (component instanceof ScrollTextPane) {
                if (!((ScrollTextPane) component).saveTextPane()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 关闭所有的文本窗格。
     *
     * @return 只有全部关闭成功才返回true，否则返回false。
     */
    public boolean closeAllTextPane() {
        for (int i = this.getTabCount() - 1; i >= 0; i--) {
            // switch to tab i
            this.setSelectedIndex(i);
            Component component = this.getComponentAt(i);
            if (component instanceof ScrollTextPane) {
                if (!((ScrollTextPane) component).closeTextPane(i)) {
                    return false;
                }
            }
        }
        return true;
    }


    // ----------- getter setter

    /**
     * 返回当前tab下的文本窗
     */
    public ScrollTextPane getSelectedTextPane() {
        return selectedScrollTextPane;
    }

    /**
     * 返回使用的文件选择器
     */
    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public OpenedFilesRecently getOfr() {
        return ofr;
    }

    public Map<ActionName, KeyStroke> getKeyStrokes() {
        return keyStrokes;
    }

    /**
     * 返回所有默认使用的Action
     */
    public Map<ActionName, Action> getActions() {
        return actions;
    }

    /**
     * 返回所有tab下的文本窗共享的字体
     */
    public Font getStpFont() {
        return stpFont;
    }

    public void setOpenRecentlyMenu(OpenRecentlyMenu openRecentlyMenu) {
        this.openRecentlyMenu = openRecentlyMenu;
    }

    // ----------------- init MainPane
    private void init() {
        this.fileChooser = new JFileChooser();
        this.fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return !f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Just Files";
            }
        });
        this.ofr = new OpenedFilesRecently();

        // create default key strokes
        keyStrokes = createDefaultKeyStrokes();
        // create default action
        actions = createDefaultActions();
        // set main pane font
        setFont(new Font(null, Font.PLAIN, 16));
        // record selected text pane with change listener
        addChangeListener(e -> {
            if (this.getTabCount() == 0) {
                StatusBar.INSTANCE.hideStatusBar();
            } else {
                if (this.getTabCount() == 1) {
                    StatusBar.INSTANCE.displayStatusBar();
                }
                selectedScrollTextPane = (ScrollTextPane) getSelectedComponent();
                JTextArea textArea = selectedScrollTextPane.getTextArea();
                TextLengthIndicator.INDICATOR.refresh(textArea);
                CaretStatusIndicator.INDICATOR.refresh(textArea);
            }
        });
        // 监听器，记录增加/移除的tab index
        addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                Component child = e.getChild();
                if (child instanceof ScrollTextPane) {
                    ScrollTextPane scrollTextPane = (ScrollTextPane) child;
                    File file = scrollTextPane.getFile();
                    if (file != null) {
                        addedFileTabbedIndex.put(file.getAbsolutePath(), scrollTextPane);
                    }
                }
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                Component child = e.getChild();
                if (child instanceof ScrollTextPane) {
                    ScrollTextPane scrollTextPane = (ScrollTextPane) child;
                    File file = scrollTextPane.getFile();
                    if (file != null) {
                        addedFileTabbedIndex.remove(file.getAbsolutePath());
                    }
                }
            }
        });
    }

    private Map<ActionName, KeyStroke> createDefaultKeyStrokes() {
        Map<ActionName, KeyStroke> keyStrokes = new HashMap<>();
        keyStrokes.put(ZOOM_IN, KeyStroke.getKeyStroke(VK_ADD, CTRL_DOWN_MASK));
        keyStrokes.put(ZOOM_OUT, KeyStroke.getKeyStroke(VK_SUBTRACT, CTRL_DOWN_MASK));
        keyStrokes.put(NEW_FILE, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK));
        keyStrokes.put(OPEN_FILE, KeyStroke.getKeyStroke(VK_O, CTRL_DOWN_MASK));
        keyStrokes.put(SAVE_FILE, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK));
        keyStrokes.put(SAVE_ALL, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        keyStrokes.put(CLOSE, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK));
        keyStrokes.put(CLOSE_ALL, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        keyStrokes.put(UNDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK));
        keyStrokes.put(REDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        keyStrokes.put(CUT, KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK));
        keyStrokes.put(COPY, KeyStroke.getKeyStroke(VK_C, CTRL_DOWN_MASK));
        keyStrokes.put(PASTE, KeyStroke.getKeyStroke(VK_V, CTRL_DOWN_MASK));
        keyStrokes.put(EXIT, KeyStroke.getKeyStroke(VK_F4, CTRL_DOWN_MASK));
        keyStrokes.put(LINE_WRAP, KeyStroke.getKeyStroke(VK_Z, ALT_DOWN_MASK));
        return keyStrokes;
    }

    private Map<ActionName, Action> createDefaultActions() {
        Map<ActionName, Action> actions = new HashMap<>();
        actions.put(UNDO, new UndoActionWrapper(this));
        actions.put(REDO, new RedoActionWrapper(this));
        actions.put(ZOOM_IN, new ZoomInAction(this));
        actions.put(ZOOM_OUT, new ZoomOutAction(this));
        actions.put(NEW_FILE, new NewAction(this));
        actions.put(OPEN_FILE, new OpenAction(this));
        actions.put(SAVE_FILE, new SaveAction(this));
        actions.put(SAVE_ALL, new SaveAllAction(this));
        actions.put(CUT, new CutAction());
        actions.put(COPY, new CopyAction());
        actions.put(PASTE, new PasteAction());
        actions.put(CLOSE, new CloseAction(this));
        actions.put(CLOSE_ALL, new CloseAllAction(this));
        actions.put(EXIT, new ExitAction());
        actions.put(LINE_WRAP, new LineWrapAction(this));
        return actions;
    }
}
