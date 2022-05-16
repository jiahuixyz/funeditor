package redcoder.texteditor.action;

import redcoder.texteditor.core.fontsize.FontZoomInZoomOutProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 缩小字体
 */
public class ZoomOutAction extends AbstractAction {


    public ZoomOutAction() {
        super("Zoom Out");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        FontZoomInZoomOutProcessor.zoomOut(this);
    }
}
