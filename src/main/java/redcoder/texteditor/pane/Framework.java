package redcoder.texteditor.pane;

import redcoder.texteditor.pane.file.DefaultFileProcessor;
import redcoder.texteditor.pane.file.FileProcessor;
import redcoder.texteditor.pane.file.RecentlyOpenedFiles;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Framework extends WindowAdapter {


    public static final Framework INSTANCE = new Framework();
    public final FileProcessor fileProcessor;
    public final RecentlyOpenedFiles recentlyOpenedFiles;
    private final UnsavedCreatedNewlyFiles unsavedCreatedNewlyFiles;

    private int numWindows = 0;
    private EditorFrame editorFrame;
    private Point lastLocation;

    private Framework() {
        this.fileProcessor = new DefaultFileProcessor();
        this.recentlyOpenedFiles = new RecentlyOpenedFiles();
        this.unsavedCreatedNewlyFiles = new UnsavedCreatedNewlyFiles();
    }

    public void makeNewWindow() {
        EditorFrame frame = new EditorFrame();
        numWindows++;
        frame.init();
        frame.addWindowListener(this);
        if (lastLocation != null) {
            lastLocation.translate(40, 40);
            // if ((lastLocation.x > maxX) || (lastLocation.y > maxY)) {
            //     lastLocation.setLocation(0, 0);
            // }
            frame.setLocation(lastLocation);
        } else {
            lastLocation = frame.getLocation();
            // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        frame.setVisible(true);
    }

    public void closeWindow() {
        if (editorFrame.shouldClose()) {
            editorFrame.setVisible(false);
            editorFrame.dispose();
            numWindows--;
        }
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

    public int getNumWindows() {
        return numWindows;
    }

    public FileProcessor getFileProcessor() {
        return fileProcessor;
    }

    public RecentlyOpenedFiles getRecentlyOpenedFiles() {
        return recentlyOpenedFiles;
    }

    public UnsavedCreatedNewlyFiles getUnsavedCreatedNewlyFiles() {
        return unsavedCreatedNewlyFiles;
    }
}
