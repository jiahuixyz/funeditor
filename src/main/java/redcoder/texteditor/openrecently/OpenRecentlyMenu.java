package redcoder.texteditor.openrecently;

import redcoder.texteditor.action.OpenRecentlyAction;
import redcoder.texteditor.pane.EditorFrame;
import redcoder.texteditor.pane.MainPane;

import javax.swing.*;
import java.util.Objects;

public class OpenRecentlyMenu extends JMenu {

    private MainPane mainPane;

    public OpenRecentlyMenu(MainPane mainPane){
        super("Open Recently");
        this.mainPane = mainPane;
        setFont(EditorFrame.MENU_ITEM_DEFAULT_FONT);
        initMenuItem(mainPane);
    }

    private void initMenuItem(MainPane mainPane) {
        for (String filepath : mainPane.getOfr().getRecentlyFile()) {
            add(new OpenRecentlyAction(filepath, mainPane));
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
        JMenuItem menuItem = new JMenuItem(new OpenRecentlyAction(filepath, mainPane));
        add(menuItem, 0);
    }
}
