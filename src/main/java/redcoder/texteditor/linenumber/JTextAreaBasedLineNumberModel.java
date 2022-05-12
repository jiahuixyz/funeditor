package redcoder.texteditor.linenumber;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

/**
 * The JTextArea-based LineNumberModel implementations.
 */
public class JTextAreaBasedLineNumberModel implements LineNumberModel {

    private final JTextArea textArea;

    public JTextAreaBasedLineNumberModel(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public int getNumberLines() {
        return this.textArea.getLineCount();
    }

    @Override
    public Rectangle getLineRect(int line) {
        try {
            int startOffset = this.textArea.getLineStartOffset(line);
            return this.textArea.modelToView(startOffset);
        } catch (BadLocationException e) {
            throw new RuntimeException("Failed to get line Rectangle", e);
        }
    }
}
