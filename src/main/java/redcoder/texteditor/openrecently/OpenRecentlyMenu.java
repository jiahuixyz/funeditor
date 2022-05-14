package redcoder.texteditor.openrecently;

import redcoder.texteditor.action.OpenRecentlyAction;
import redcoder.texteditor.pane.EditorFrame;
import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

public class OpenRecentlyMenu extends JMenu {

    private OpenedFilesRecently ofr;
    private MainTabPane mainTabPane;

    public OpenRecentlyMenu(MainTabPane mainTabPane) {
        super("Open Recently");
        this.ofr = new OpenedFilesRecently();
        this.mainTabPane = mainTabPane;
        setFont(EditorFrame.MENU_ITEM_DEFAULT_FONT);
        initMenuItem();
    }

    private void initMenuItem() {
        for (String filepath : ofr.getRecentlyFile()) {
            add(new OpenRecentlyAction(filepath, mainTabPane));
        }
    }

    public void addOpenedFileRecently(File file) {
        ofr.addFile(file);

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
        JMenuItem menuItem = new JMenuItem(new OpenRecentlyAction(filepath, mainTabPane));
        add(menuItem, 0);
    }
}
