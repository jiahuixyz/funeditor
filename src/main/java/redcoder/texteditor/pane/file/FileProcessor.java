package redcoder.texteditor.pane.file;

import redcoder.texteditor.pane.MainTabPane;
import redcoder.texteditor.pane.ScrollTextPane;

import java.io.File;

public interface FileProcessor {

    /**
     * 打开文件，由用户选择打开哪个文件
     *
     * @return true：打开成功，false：打开失败
     */
    boolean openFile(MainTabPane mainTabPane);

    /**
     * 打开指定的文件
     *
     * @param mainTabPane 编辑器主窗格
     * @param file        要打开的文件
     * @param ucnf        是否是新创建的且未保存的文件
     * @return true：打开成功，false：打开失败
     */
    boolean openFile(MainTabPane mainTabPane, File file, boolean ucnf);

    /**
     * 将文本窗格中的内容保存到文件中
     *
     * @param textPane 文本窗格
     * @return true：保存成功，false：保存失败
     */
    boolean saveTextPaneToFile(ScrollTextPane textPane);

    /**
     * 添加文件打开监听器
     *
     * @param listener 监听器
     * @return true-添加成功、false-添加失败
     */
    boolean addFileOpenListener(FileOpenListener listener);
}
