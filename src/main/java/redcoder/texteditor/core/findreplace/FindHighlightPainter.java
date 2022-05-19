package redcoder.texteditor.core.findreplace;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;

class FindHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

    public FindHighlightPainter() {
        super(Color.GREEN);
    }
}
