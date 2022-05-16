package redcoder.texteditor.pane.file;

import redcoder.texteditor.pane.EditorFrame;
import redcoder.texteditor.pane.tabpane.MainTabPane;
import redcoder.texteditor.pane.textpane.ScrollTextPane;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultFileProcessor implements FileProcessor {

    private final JFileChooser fileChooser;
    private final List<FileOpenListener> listenerList;

    public DefaultFileProcessor() {
        this.fileChooser = new JFileChooser();
        this.fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return !f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Just Files";
            }
        });
        this.listenerList = new ArrayList<>();
    }

    /**
     * 打开文件，由用户选择打开哪个文件
     *
     * @return true：打开成功，false：打开失败
     */
    @Override
    public boolean openFile(MainTabPane mainTabPane) {
        int state = fileChooser.showOpenDialog(mainTabPane);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return openFile(mainTabPane, file, false);
        }
        return false;
    }

    /**
     * 打开指定的文件
     *
     * @return true：打开成功，false：打开失败
     */
    @Override
    public boolean openFile(MainTabPane mainTabPane, File file, boolean ucnf) {
        boolean b = mainTabPane.openFile(file, ucnf);
        noticeFileOpenListener(file, ucnf);
        return b;
    }

    @Override
    public boolean saveTextPaneToFile(ScrollTextPane textPane) {
        File file = textPane.getFile();
        String text = textPane.getTextArea().getText();
        if (file != null) {
            FileUtils.writeFile(text, file);
            return true;
        } else {
            int state = fileChooser.showSaveDialog(textPane);
            if (state == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                if (file.exists()) {
                    String message = String.format("%s already exist, would you like overwriting it?", file.getName());
                    int n = JOptionPane.showConfirmDialog(textPane, message, EditorFrame.TITLE, JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        FileUtils.writeFile(text, file);
                        textPane.setFile(file);
                        return true;
                    }
                } else {
                    FileUtils.writeFile(text, file);
                    textPane.setFile(file);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addFileOpenListener(FileOpenListener listener) {
        this.listenerList.add(listener);
        return true;
    }

    private void noticeFileOpenListener(File file, boolean unsavedNewTextPane) {
        FileOpenEvent e = new FileOpenEvent(this, unsavedNewTextPane, file);
        for (FileOpenListener listener : listenerList) {
            listener.onFileOpen(e);
        }
    }

}
