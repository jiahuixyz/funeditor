package redcoder.texteditor;

import javax.swing.*;

public class AutoWrapJTextPane extends JTextPane {

    private boolean autoWrapLine = true;

    @Override
    public boolean getScrollableTracksViewportWidth() {
        if (autoWrapLine) {
            return super.getScrollableTracksViewportWidth();
        }
        return false;
    }

    public boolean isAutoWrapLine() {
        return autoWrapLine;
    }

    public void setAutoWrapLine(boolean autoWrapLine) {
        this.autoWrapLine = autoWrapLine;
    }
}
