package redcoder.texteditor;

import org.apache.commons.lang3.SystemUtils;
import redcoder.texteditor.pane.MainPane;
import redcoder.texteditor.pane.ScrollTextPane;
import redcoder.texteditor.utils.FileUtils;
import redcoder.texteditor.utils.ScheduledUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UnsavedNewTextPane {

    private static final String FILENAME = "un-snf";
    private final Map<String, ScrollTextPane> textPanes = new HashMap<>();
    private final File targetDir;
    private final MainPane mainPane;

    public UnsavedNewTextPane(MainPane mainPane) {
        this.mainPane = mainPane;
        targetDir = new File(SystemUtils.getUserDir(), FILENAME);
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ScheduledUtils.scheduleAtFixedRate(() -> {
            try {
                for (Map.Entry<String, ScrollTextPane> entry : textPanes.entrySet()) {
                    String filename = entry.getKey();
                    File f = new File(targetDir, filename);
                    FileUtils.writeFile(entry.getValue().getTextArea().getText(), f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 3, TimeUnit.MINUTES);
    }

    public void addTextPanes(ScrollTextPane textPane) {
        this.textPanes.put(textPane.getFilename(), textPane);
    }

    public void removeTextPane(ScrollTextPane textPane) {
        String filename = textPane.getFilename();
        this.textPanes.remove(filename);
        // delete file
        File f = new File(targetDir, filename);
        f.delete();
    }

    public boolean load() {
        File[] files = targetDir.listFiles(pathname -> !pathname.isDirectory());
        if (files == null || files.length == 0) {
            return false;
        }
        for (File file : files) {
            mainPane.openUnsavedNewFile(file);
        }
        return true;
    }
}
