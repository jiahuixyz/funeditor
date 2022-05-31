package redcoder.texteditor.core.dnd;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * transfer handler for file
 */
public class FileTransferHandler extends TransferHandler {

    public static final FileTransferHandler TRANSFER_HANDLER = new FileTransferHandler();

    private static final Logger LOGGER = Logger.getLogger(FileTransferHandler.class.getName());

    private FileTransferHandler() {
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            TabPane tabPane = Framework.getActivatedFrame().getTabPane();
            for (File file : files) {
                if (file.isFile()) {
                    FileProcessor.openFile(tabPane, file, false);
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to remove exported content.", e);
            return false;
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDrop() && support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }
}
