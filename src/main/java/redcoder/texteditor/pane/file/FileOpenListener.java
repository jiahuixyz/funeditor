package redcoder.texteditor.pane.file;

import java.io.File;

/**
 * 接收文件打开事件的监听器
 */
public interface FileOpenListener {

    /**
     * 响应文件打开事件
     *
     * @param e 件打开事件
     */
    void onFileOpen(FileOpenEvent e);

    /**
     * 文件打开事件
     */
    interface FileOpenEvent {

        /**
         * 是否是新创建的且未保存的文件
         */
        boolean isUnSavedNew();

        /**
         * 打开的文件
         */
        File getOpenedFile();
    }
}
