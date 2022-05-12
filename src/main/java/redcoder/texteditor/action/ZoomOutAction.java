package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 缩小字体
 */
public class ZoomOutAction extends AbstractAction {

    private MainPane mainPane;

    public ZoomOutAction(MainPane mainPane) {
        super("Zoom Out");
        this.mainPane = mainPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        mainPane.zoomOutFont();
    }
}
