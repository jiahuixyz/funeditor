package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 放大字体
 */
public class ZoomInAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public ZoomInAction(MainTabPane mainTabPane) {
        super("Zoom In");
        this.mainTabPane = mainTabPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.zoomInFont();
    }
}