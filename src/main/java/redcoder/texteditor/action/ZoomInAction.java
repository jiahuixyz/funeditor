package redcoder.texteditor.action;

import redcoder.texteditor.core.fontsize.FontZoomInZoomOutProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 放大字体
 */
public class ZoomInAction extends AbstractAction {

    public ZoomInAction() {
        super("Zoom In");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FontZoomInZoomOutProcessor.zoomIn(this);
    }
}