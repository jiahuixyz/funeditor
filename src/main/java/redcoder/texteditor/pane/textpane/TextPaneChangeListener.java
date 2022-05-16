package redcoder.texteditor.pane.textpane;

/**
 * 接收文本窗格变化的监听器
 */
public interface TextPaneChangeListener {

    /**
     * 处理文本窗格的变化事件
     *
     * @param e 文本窗格变化事件
     */
    void onChange(TextPaneChangeEvent e);
}
