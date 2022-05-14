package redcoder.texteditor.pane;

import org.apache.commons.lang3.SystemUtils;
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

    public UnsavedNewTextPane() {
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

    /**
     * 加载未保存的新建text pane
     *
     * @param mainTabPane 主窗格
     * @return 加载的text pane数量
     */
    public int load(MainTabPane mainTabPane) {
        File[] files = targetDir.listFiles(pathname -> !pathname.isDirectory());
        if (files == null || files.length == 0) {
            return 0;
        }
        for (File file : files) {
            mainTabPane.openUnsavedNewFile(file);
        }
        return files.length;
    }
}
