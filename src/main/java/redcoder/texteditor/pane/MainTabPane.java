package redcoder.texteditor.pane;

import redcoder.texteditor.statusbar.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 编辑器主窗格，支持多tab
 */
public class MainTabPane extends JTabbedPane {

    public static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 20);

    private static final int FONT_SIZE_MINIMUM = 10;
    private static final int FONT_SIZE_MAXIMUM = 100;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final StatusBar statusBar;
    private final ActionCollection actionCollection;
    private final Map<String, ScrollTextPane> addedFileTabbedIndex;
    private ScrollTextPane selectedScrollTextPane;
    private Font stpFont = DEFAULT_FONT; // font used by ScrollTextPane's all instance

    public MainTabPane(StatusBar statusBar) {
        this.statusBar = statusBar;
        this.addedFileTabbedIndex = new HashMap<>();
        this.actionCollection = new ActionCollection(this);

        setFont(new Font(null, Font.PLAIN, 16));
        // 添加监听器-记录选中的tab，更新底部状态信息
        addChangeListener(e -> {
            if (this.getTabCount() == 0) {
                StatusBar.INSTANCE.hideStatusBar();
            } else {
                if (this.getTabCount() == 1) {
                    StatusBar.INSTANCE.displayStatusBar();
                }
                selectedScrollTextPane = (ScrollTextPane) getSelectedComponent();
                JTextArea textArea = selectedScrollTextPane.getTextArea();
                statusBar.getTextLengthIndicator().refresh(textArea);
                statusBar.getCaretStatusIndicator().refresh(textArea);
            }
        });
        // 添加监听器-记录增加/移除的tab index
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

    /**
     * 加载新创建的且未保存的文件
     */
    public void loadUnSavedNewTextPane() {
        int i = Framework.INSTANCE.getUnsavedCreatedNewlyFiles().load(this);
        counter.set(i);
    }

    /**
     * 创建新的文本窗格
     */
    public void createTextPane() {
        int i = counter.getAndIncrement();
        String filename = "new-" + i;

        ScrollTextPane scrollTextPane = new ScrollTextPane(statusBar, this, filename);
        addTab(filename, scrollTextPane, true);
        setSelectedComponent(scrollTextPane);
    }

    /**
     * 添加新的tab
     *
     * @param title          tab标题
     * @param scrollTextPane 文本窗格
     * @param ucnf           是否是新创建的且未保存的文件
     */
    public void addTab(String title, ScrollTextPane scrollTextPane, boolean ucnf) {
        if (ucnf) {
            Framework.INSTANCE.getUnsavedCreatedNewlyFiles().addTextPanes(scrollTextPane);
        }
        addTab(title, scrollTextPane);
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

    /**
     * 保存当前选中的tab
     *
     * @return true-保存成功，false-保存失败
     */
    public boolean saveSelectedTab() {
        return selectedScrollTextPane.saveTextPane();
    }

    /**
     * 保存所有tab
     *
     * @return 只有全部保存成功才返回true，否则返回false。
     */
    public boolean saveAllTab() {
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
     * 关闭当前选中的tab
     *
     * @return true：关闭成功，false：关闭失败
     */
    public boolean closeSelectedTab() {
        if (selectedScrollTextPane.closeTextPane()) {
            removeTabAt(getSelectedIndex());
            // remove from UnsavedNewFile
            Framework.INSTANCE.getUnsavedCreatedNewlyFiles().removeTextPane(selectedScrollTextPane);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 关闭指定位置的tab
     *
     * @param index tab位置
     * @return true：关闭成功，false：关闭失败
     */
    public boolean closeTab(int index) {
        Component component = getComponentAt(index);
        if (component instanceof ScrollTextPane) {
            ScrollTextPane scrollTextPane = (ScrollTextPane) component;
            if (scrollTextPane.closeTextPane()) {
                removeTabAt(index);
                Framework.INSTANCE.getUnsavedCreatedNewlyFiles().removeTextPane(scrollTextPane);
                return true;
            }
        } else {
            removeTabAt(index);
            return true;
        }
        return false;
    }

    /**
     * 关闭所有的文本窗格。
     *
     * @return 只有全部关闭成功才返回true，否则返回false。
     */
    public boolean closeAllTab() {
        for (int i = this.getTabCount() - 1; i >= 0; i--) {
            // switch to tab i
            setSelectedIndex(i);
            // then close it
            closeTab(i);
        }
        return true;
    }

    // ------------ font operation

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
        statusBar.getTextFontSizeIndicator().refresh(stpFont);
    }

    /**
     * 打开指定的文件
     *
     * @param file 要打开的文件
     * @param ucnf 是否是新创建的且未保存的文件
     * @return true-打开成功、false-打开失败
     */
    public boolean openFile(File file, boolean ucnf) {
        ScrollTextPane scrollTextPane = addedFileTabbedIndex.get(file.getAbsolutePath());
        if (scrollTextPane != null) {
            // 文件已打开，切换到文件所在的tab即可
            setSelectedComponent(scrollTextPane);
            return true;
        }

        String filename = file.getName();
        if (ucnf) {
            scrollTextPane = new ScrollTextPane(statusBar, this, filename);
            addTab(filename, scrollTextPane, true);
            scrollTextPane.setText(file, false);
        } else {
            if (selectedScrollTextPane != null
                    && selectedScrollTextPane.getFile() == null
                    && !selectedScrollTextPane.isModified()) {
                // 当前tab是新打开的且未写入任何内容，将文件放入改tab下
                selectedScrollTextPane.setText(file, true);
                selectedScrollTextPane.setFile(file);
                selectedScrollTextPane.updateTabbedTitle(filename);
            } else {
                scrollTextPane = new ScrollTextPane(statusBar, this, file);
                addTab(filename, scrollTextPane, false);
                setSelectedComponent(scrollTextPane);
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

    public ActionCollection getActionCollection() {
        return actionCollection;
    }

    /**
     * 返回所有tab下的文本窗共享的字体
     */
    public Font getStpFont() {
        return stpFont;
    }
}
