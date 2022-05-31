package redcoder.texteditor.core.file;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.tabpane.TabPane;
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
    public static boolean openFile(TabPane tabPane) {
        int state = fileChooser.showOpenDialog(tabPane);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return openFile(tabPane, file, false);
        }
        return false;
    }

    /**
     * 打开指定的文件
     *
     * @param tabPane 编辑器主窗格
     * @param file    要打开的文件
     * @param ucnf    是否是新创建的且未保存的文件
     * @return true：打开成功，false：打开失败
     */
    public static boolean openFile(TabPane tabPane, File file, boolean ucnf) {
        boolean b = tabPane.openFile(file, ucnf);
        fireFileOpenEvent(tabPane, file, ucnf);
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
            return saveTextPaneToAnotherFile(textPane) != null;
        }
    }

    /**
     * 将文本窗格中的内容保存到另外一个文件中
     *
     * @param textPane 文本窗格
     * @return 返回保存的文件，如果保存失败，返回null
     */
    public static File saveTextPaneToAnotherFile(ScrollTextPane textPane) {
        int state = fileChooser.showSaveDialog(textPane);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String text = textPane.getTextArea().getText();
            if (file.exists()) {
                String message = String.format("%s already exist, would you like overwriting it?", file.getName());
                int n = JOptionPane.showConfirmDialog(textPane, message, EditorFrame.TITLE, JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    FileUtils.writeFile(text, file);
                    textPane.setFile(file);
                    return file;
                }
            } else {
                FileUtils.writeFile(text, file);
                textPane.setFile(file);
                return file;
            }
        }
        return null;
    }

    /**
     * 添加文件打开监听器
     *
     * @param listener 监听器
     */
    public static void addFileOpenListener(FileOpenListener listener) {
        listenerList.add(listener);
    }

    private static void fireFileOpenEvent(TabPane tabPane, File file, boolean ucnf) {
        FileOpenEvent e = new FileOpenEvent(tabPane, ucnf, file);
        for (FileOpenListener listener : listenerList) {
            listener.onFileOpen(e);
        }
    }

}
