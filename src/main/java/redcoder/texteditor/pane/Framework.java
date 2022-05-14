package redcoder.texteditor.pane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Framework extends WindowAdapter {

    public static final Framework FRAMEWORK = new Framework();

    private int numWindows = 0;
    private EditorFrame editorFrame;

    private Framework(){
    }

    public void makeNewWindow(){
        EditorFrame editorFrame = new EditorFrame();
        editorFrame.init();
        editorFrame.addWindowListener(this);
        numWindows++;
    }

    public void closeWindow() {
        editorFrame.setVisible(false);
        editorFrame.dispose();
        numWindows--;
    }

    @Override
    public void windowActivated(WindowEvent e) {
        Object source = e.getSource();
        editorFrame = (EditorFrame) source;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (numWindows == 0) {
            System.exit(0);
        }
    }
}
