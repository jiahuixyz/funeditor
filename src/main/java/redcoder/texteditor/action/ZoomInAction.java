package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 放大字体
 */
public class ZoomInAction extends AbstractAction {

    private MainPane mainPane;

    public ZoomInAction(MainPane mainPane) {
        super("Zoom In");
        this.mainPane = mainPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        mainPane.zoomInFont();
    }
}