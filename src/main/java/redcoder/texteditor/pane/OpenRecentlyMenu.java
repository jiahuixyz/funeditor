package redcoder.texteditor.pane;

import redcoder.texteditor.action.OpenRecentlyAction;
import redcoder.texteditor.pane.file.FileOpenListener;
import redcoder.texteditor.pane.file.RecentlyOpenedFiles;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

public class OpenRecentlyMenu extends JMenu implements FileOpenListener {

    private final MainTabPane mainTabPane;

    public OpenRecentlyMenu(MainTabPane mainTabPane) {
        super("Open Recently");
        this.mainTabPane = mainTabPane;

        initMenuItem();
        setFont(EditorFrame.MENU_ITEM_DEFAULT_FONT);
        Framework.INSTANCE.getFileProcessor().addFileOpenListener(this);
    }

    @Override
    public void onFileOpen(FileOpenEvent e) {
        if (!e.isUnSavedNew()) {
            File file = e.getOpenedFile();
            addOpenedFileRecently(file);
        }
    }

    private void initMenuItem() {
        RecentlyOpenedFiles rof = Framework.INSTANCE.getRecentlyOpenedFiles();
        for (File file : rof.getRecentlyFile()) {
            add(new OpenRecentlyAction(mainTabPane, file));
        }
    }

    private void addOpenedFileRecently(File file) {
        RecentlyOpenedFiles rof = Framework.INSTANCE.getRecentlyOpenedFiles();
        rof.addFile(file);

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
        JMenuItem menuItem = new JMenuItem(new OpenRecentlyAction(mainTabPane, file));
        add(menuItem, 0);
    }
}
