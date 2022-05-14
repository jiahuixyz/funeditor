package redcoder.texteditor.pane;

public interface TextPaneGenerator {

    void setInitialCounter(int initialValue);

    void generate(MainTabPane mainTabPane);
}
