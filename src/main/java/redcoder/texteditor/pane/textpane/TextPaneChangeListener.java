package redcoder.texteditor.pane.textpane;

/**
 * 接收文本窗格变化的监听器
 */
public interface TextPaneChangeListener {

    /**
     * 响应文本窗格的变化
     *
     * @param textPane 变化的文本窗格
     */
    void onChange(ScrollTextPane textPane);
}
