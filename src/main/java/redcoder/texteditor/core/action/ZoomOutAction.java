package redcoder.texteditor.core.action;

import redcoder.texteditor.core.font.FontChangeProcessor;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 缩小字体
 */
public class ZoomOutAction extends AbstractAction {


    public ZoomOutAction() {
        super("Zoom Out");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("ZoomOut24.gif"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        FontChangeProcessor.zoomOut(this);
    }
}
