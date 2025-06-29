package redcoder.texteditor.core.tabpane;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.file.UnsavedCreatedNewlyFiles;
import redcoder.texteditor.core.font.FontChangeProcessor;
import redcoder.texteditor.core.statusbar.EditorStatusBar;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

/**
 * 支持多tab的窗格，
 */
public class TabPane extends JTabbedPane {

    private static final Logger LOGGER = Logger.getLogger(TabPane.class.getName());
    private static final Object[] CLOSE_OPTIONS = {"Save All", "Don't Save", "Cancel"};
    private final EditorStatusBar statusBar;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<String, ScrollTextPane> addedFileTabbedIndex;
    private ScrollTextPane selectedScrollTextPane;

    public TabPane(EditorStatusBar statusBar) {
        this.statusBar = statusBar;
        this.addedFileTabbedIndex = new HashMap<>();

        setFont(new Font(null, Font.PLAIN, 20));
        // 添加监听器-记录选中的tab，更新底部状态信息
        addChangeListener(e -> {
            if (this.getTabCount() == 0) {
                statusBar.hideIndicator();
            } else {
                if (this.getTabCount() == 1) {
                    statusBar.displayIndicator();
                }
                selectedScrollTextPane = (ScrollTextPane) getSelectedComponent();
                selectedScrollTextPane.touch();
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

                if (getTabCount() == 0) {
                    selectedScrollTextPane = null;
                }
            }
        });
    }

    /**
     * 加载新创建的且未保存的文件
     */
    public void loadUnSavedNewTextPane() {
        try {
            int i = UnsavedCreatedNewlyFiles.load(this);
            counter.set(i);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load unsaved created file newly", e);
        }
    }

    /**
     * 创建新的文本窗格
     */
    public void createTextPane() {
        int i = counter.getAndIncrement();
        String filename = "new" + i;

        ScrollTextPane scrollTextPane = new ScrollTextPane(filename);
        scrollTextPane.addTextPaneChangeListener(statusBar);
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
            UnsavedCreatedNewlyFiles.addTextPanes(scrollTextPane);
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
        if (selectedScrollTextPane == null) {
            return false;
        }
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
     * 使用指定的文件替换当前tab下的文本窗格内容。
     *
     * @param file 文件
     * @return true-替换成功，false-替换失败
     */
    public boolean replaceSelectedTab(File file) {
        removeTabAt(getSelectedIndex());
        openFile(file, false);
        return true;
    }

    /**
     * 关闭当前选中的tab
     *
     * @return true：关闭成功，false：关闭失败
     */
    public boolean closeSelectedTab() {
        if (selectedScrollTextPane == null) {
            return false;
        }

        if (selectedScrollTextPane.closeTextPane()) {
            removeTabAt(getSelectedIndex());
            UnsavedCreatedNewlyFiles.removeTextPane(selectedScrollTextPane);
            FontChangeProcessor.removeListener(selectedScrollTextPane);
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
                UnsavedCreatedNewlyFiles.removeTextPane(scrollTextPane);
                FontChangeProcessor.removeListener(scrollTextPane);
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
            scrollTextPane = new ScrollTextPane(filename);
            scrollTextPane.addTextPaneChangeListener(statusBar);
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
                scrollTextPane = new ScrollTextPane(file);
                scrollTextPane.addTextPaneChangeListener(Framework.getActivatedFrame().getStatusBar());
                addTab(filename, scrollTextPane, false);
                setSelectedComponent(scrollTextPane);
            }
        }

        return true;
    }

    /**
     * 当前tab窗格是否可以关闭
     */
    public boolean canCloseNormally() {
        List<ScrollTextPane> textPanes = getModifiedTextPane();
        if (!textPanes.isEmpty()) {
            String message = createDialogMessage(textPanes);
            int state = JOptionPane.showOptionDialog(this, message, EditorFrame.TITLE, JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, CLOSE_OPTIONS, CLOSE_OPTIONS[0]);
            if (state == JOptionPane.YES_OPTION) {
                for (ScrollTextPane pane : textPanes) {
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
    }

    private List<ScrollTextPane> getModifiedTextPane() {
        List<ScrollTextPane> textPaneList = new ArrayList<>();
        for (int i = 0; i < getTabCount(); i++) {
            Component component = getComponentAt(i);
            if (component instanceof ScrollTextPane) {
                if (((ScrollTextPane) component).isModified()) {
                    textPaneList.add((ScrollTextPane) component);
                }
            }
        }
        return textPaneList;
    }

    private String createDialogMessage(List<ScrollTextPane> textPanes) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("Do you want to save the changes to the following %d files?", textPanes.size()))
                .append("\n\n");

        List<String> filenames = textPanes.stream().map(ScrollTextPane::getFilename).collect(toList());
        if (filenames.size() > 5) {
            int i = filenames.size() - 5;
            message.append(StringUtils.join(filenames.subList(0, 5), "\n"))
                    .append("\n")
                    .append(String.format("...%d additional files not shown", i))
                    .append("\n\n");
        } else {
            message.append(StringUtils.join(filenames, "\n"))
                    .append("\n\n");
        }

        message.append("Your changes will be lost if you don't save them.");

        return message.toString();
    }


    /**
     * 返回当前tab下的文本窗
     */
    public ScrollTextPane getSelectedTextPane() {
        return selectedScrollTextPane;
    }
}
