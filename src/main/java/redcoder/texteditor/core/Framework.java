package redcoder.texteditor.core;

import redcoder.texteditor.core.file.DefaultFileProcessor;
import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.file.RecentlyOpenedFiles;
import redcoder.texteditor.core.file.UnsavedCreatedNewlyFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Framework extends WindowAdapter {

    private static final Framework INSTANCE = new Framework();
    private static final FileProcessor fileProcessor;
    private static final RecentlyOpenedFiles recentlyOpenedFiles;
    private static final UnsavedCreatedNewlyFiles unsavedCreatedNewlyFiles;

    private static int numWindows = 0;
    private static Point lastLocation;
    private static EditorFrame activatedFrame;
    private static final List<EditorFrame> frames;

    static {
        fileProcessor = new DefaultFileProcessor();
        recentlyOpenedFiles = new RecentlyOpenedFiles();
        unsavedCreatedNewlyFiles = new UnsavedCreatedNewlyFiles();
        frames = new ArrayList<>();
    }

    private Framework() {
    }

    public static void makeNewWindow() {
        EditorFrame frame = new EditorFrame();
        numWindows++;
        frames.add(frame);

        frame.init();
        frame.addWindowListener(INSTANCE);
        if (lastLocation != null) {
            lastLocation.translate(40, 40);
            frame.setLocation(lastLocation);
        } else {
            lastLocation = frame.getLocation();
        }
        frame.setVisible(true);
    }

    public static void closeWindow() {
        if (activatedFrame.shouldClose()) {
            activatedFrame.setVisible(false);
            activatedFrame.dispose();
            numWindows--;
            frames.remove(activatedFrame);
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        Object source = e.getSource();
        activatedFrame = (EditorFrame) source;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (numWindows == 0) {
            System.exit(0);
        }
    }

    public static int getNumWindows() {
        return numWindows;
    }

    public static FileProcessor getFileProcessor() {
        return fileProcessor;
    }

    public static RecentlyOpenedFiles getRecentlyOpenedFiles() {
        return recentlyOpenedFiles;
    }

    public static UnsavedCreatedNewlyFiles getUnsavedCreatedNewlyFiles() {
        return unsavedCreatedNewlyFiles;
    }

    public static void switchTheme(String themeName) {
        try {
            if (UIManager.getLookAndFeel().getClass().getName().equals(themeName)) {
                return;
            }

            UIManager.setLookAndFeel(themeName);
            for (EditorFrame frame : frames) {
                SwingUtilities.updateComponentTreeUI(frame);
            }
        } catch (Exception e) {
            System.err.printf("Failed to switch %s theme%n", themeName);
            e.printStackTrace();
        }
    }
}
