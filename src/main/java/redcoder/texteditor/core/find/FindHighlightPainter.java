package redcoder.texteditor.core.find;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;

public class FindHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

    public FindHighlightPainter() {
        super(Color.GREEN);
    }
}
