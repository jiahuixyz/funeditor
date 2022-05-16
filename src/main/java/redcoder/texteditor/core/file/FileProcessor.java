package redcoder.texteditor.core.file;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

    private static final JFileChooser fileChooser;
    private static final List<FileOpenListener> listenerList;

    static {
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return !f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Just Files";
            }
        });
        listenerList = new ArrayList<>();
    }

    private FileProcessor() {
    }

    /**
     * 打开文件，由用户选择打开哪个文件
     *
     * @return true：打开成功，false：打开失败
     */
    public static boolean openFile(MainTabPane mainTabPane) {
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
     * @param mainTabPane 编辑器主窗格
     * @param file        要打开的文件
     * @param ucnf        是否是新创建的且未保存的文件
     * @return true：打开成功，false：打开失败
     */
    public static boolean openFile(MainTabPane mainTabPane, File file, boolean ucnf) {
        boolean b = mainTabPane.openFile(file, ucnf);
        fireFileOpenEvent(mainTabPane, file, ucnf);
        return b;
    }

    /**
     * 将文本窗格中的内容保存到文件中
     *
     * @param textPane 文本窗格
     * @return true：保存成功，false：保存失败
     */
    public static boolean saveTextPaneToFile(ScrollTextPane textPane) {
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

    /**
     * 添加文件打开监听器
     *
     * @param listener 监听器
     */
    public static void addFileOpenListener(FileOpenListener listener) {
        listenerList.add(listener);
    }

    private static void fireFileOpenEvent(MainTabPane mainTabPane, File file, boolean ucnf) {
        FileOpenEvent e = new FileOpenEvent(mainTabPane, ucnf, file);
        for (FileOpenListener listener : listenerList) {
            listener.onFileOpen(e);
        }
    }

}
