package redcoder.texteditor.core.menu;

import redcoder.texteditor.action.OpenRecentlyAction;
import redcoder.texteditor.core.file.FileOpenEvent;
import redcoder.texteditor.core.file.FileOpenListener;
import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.file.RecentlyOpenedFiles;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

import static redcoder.texteditor.core.menu.EditorMenuBar.MENU_ITEM_DEFAULT_FONT;

public class OpenRecentlyMenu extends JMenu implements FileOpenListener {

    private final TabPane tabPane;

    public OpenRecentlyMenu(TabPane tabPane) {
        super("Open Recently");
        this.tabPane = tabPane;

        initMenuItem();
        setFont(MENU_ITEM_DEFAULT_FONT);
        setIcon(IconResource.getImageIcon("open_recently24.png"));
        FileProcessor.addFileOpenListener(this);
    }

    @Override
    public void onFileOpen(FileOpenEvent e) {
        if (!e.isUnSavedNew()) {
            File file = e.getOpenedFile();
            addOpenedFileRecently(file);
        }
    }

    private void initMenuItem() {
        for (File file : RecentlyOpenedFiles.getRecentlyFile()) {
            add(new OpenRecentlyAction(tabPane, file));
        }
    }

    private void addOpenedFileRecently(File file) {
        RecentlyOpenedFiles.addFile(file);

        // if menuitem exist, move it to first
        String filepath = file.getAbsolutePath();
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (Objects.equals(item.getText(), filepath)) {
                // move to first
                remove(i);
                insert(item, 0);
                return;
            }
        }
        // not exist, insert to first
        JMenuItem menuItem = new JMenuItem(new OpenRecentlyAction(tabPane, file));
        add(menuItem, 0);
    }
}
