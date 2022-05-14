package redcoder.texteditor.openrecently;

import redcoder.texteditor.action.OpenRecentlyAction;
import redcoder.texteditor.pane.EditorFrame;
import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.util.Objects;

public class OpenRecentlyMenu extends JMenu {

    private MainTabPane mainTabPane;

    public OpenRecentlyMenu(MainTabPane mainTabPane){
        super("Open Recently");
        this.mainTabPane = mainTabPane;
        setFont(EditorFrame.MENU_ITEM_DEFAULT_FONT);
        initMenuItem(mainTabPane);
    }

    private void initMenuItem(MainTabPane mainTabPane) {
        for (String filepath : mainTabPane.getOfr().getRecentlyFile()) {
            add(new OpenRecentlyAction(filepath, mainTabPane));
        }
    }

    public void addOrMoveToFirst(String filepath) {
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (Objects.equals(item.getText(), filepath)) {
                // move to first
                remove(i);
                insert(item, 0);
                return;
            }
        }

        // insert to first
        JMenuItem menuItem = new JMenuItem(new OpenRecentlyAction(filepath, mainTabPane));
        add(menuItem, 0);
    }
}
