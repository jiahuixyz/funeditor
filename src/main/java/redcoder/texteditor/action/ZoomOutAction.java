package redcoder.texteditor.action;

import redcoder.texteditor.core.font.FontChangeProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

/**
 * 缩小字体
 */
public class ZoomOutAction extends AbstractAction {


    public ZoomOutAction() {
        super("Zoom Out");
        Optional.ofNullable(IconResource.getImageIcon("ZoomOut24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        FontChangeProcessor.zoomOut(this);
    }
}
