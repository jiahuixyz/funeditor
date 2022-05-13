package redcoder.texteditor.openrecently;

import org.apache.commons.lang3.SystemUtils;
import redcoder.texteditor.utils.FileUtils;
import redcoder.texteditor.utils.ScheduledUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OpenedFilesRecently {

    private static final String FILENAME = "ofr.rc";
    private final Set<File> recentlyFiles = new HashSet<>();
    private final File target;

    public OpenedFilesRecently() {
        target = new File(SystemUtils.getUserDir(), FILENAME);
        ScheduledUtils.scheduleAtFixedRate(() -> {
            if (!recentlyFiles.isEmpty()) {
                ArrayList<File> copyList = new ArrayList<>(recentlyFiles);
                recentlyFiles.clear();
                String content = extract(copyList);
                FileUtils.writeFile(content, target, false);
            }
        }, 1, 5, TimeUnit.MINUTES);
    }

    public void addFile(File file) {
        recentlyFiles.add(file);
    }

    public List<String> getRecentlyFile() {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(target))) {
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            // ignore
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String extract(List<File> files) {
        StringBuilder tmp = new StringBuilder();
        for (File file : files) {
            tmp.append(file.getAbsolutePath()).append("\n");
        }
        return tmp.toString();
    }
}
