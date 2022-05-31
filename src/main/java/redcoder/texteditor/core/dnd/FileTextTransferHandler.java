package redcoder.texteditor.core.dnd;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * transfer handler for file and text
 */
public class FileTextTransferHandler extends TransferHandler {

    private static final Logger LOGGER = Logger.getLogger(FileTextTransferHandler.class.getName());

    private Position startPos;
    private Position endPos;

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                Transferable transferable = support.getTransferable();
                JTextComponent textComponent = (JTextComponent) support.getComponent();
                Point point = support.getDropLocation().getDropPoint();

                int offset = textComponent.viewToModel(point);
                if (offset >= startPos.getOffset() && offset <= endPos.getOffset()) {
                    startPos = null;
                    endPos = null;
                    return false;
                }

                String data = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                Document document = textComponent.getDocument();
                document.insertString(offset, data, null);
                return true;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to import data!", e);
                return false;
            }
        } else {
            return FileTransferHandler.TRANSFER_HANDLER.importData(support);
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDrop()
                && (support.isDataFlavorSupported(DataFlavor.stringFlavor) || support.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        if (!(c instanceof JTextComponent)) {
            return null;
        }
        JTextComponent textComponent = (JTextComponent) c;
        int start = textComponent.getSelectionStart();
        int end = textComponent.getSelectionEnd();
        if (start == end) {
            return null;
        }
        Document doc = textComponent.getDocument();
        try {
            startPos = doc.createPosition(start);
            endPos = doc.createPosition(end);
        } catch (BadLocationException e) {
            LOGGER.log(Level.WARNING, "Can't create position - unable to remove text from source.");
        }
        String selectedText = textComponent.getSelectedText();
        return new StringSelection(selectedText);
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (!(source instanceof JTextComponent)) {
            return;
        }
        if (action != MOVE) {
            return;
        }
        if (startPos != null && endPos != null && startPos.getOffset() != endPos.getOffset()) {
            try {
                JTextComponent textComponent = (JTextComponent) source;
                textComponent.getDocument().remove(startPos.getOffset(), endPos.getOffset() - startPos.getOffset());
            } catch (BadLocationException e) {
                LOGGER.log(Level.WARNING, "Failed to remove exported content.", e);
            }
        }
    }
}
