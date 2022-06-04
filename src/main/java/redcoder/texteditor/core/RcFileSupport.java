package redcoder.texteditor.core;

import redcoder.texteditor.utils.FileUtils;
import redcoder.texteditor.utils.SystemUtils;

import java.io.File;

public abstract class RcFileSupport {

    private static final String dir = "rc-text-editor";

    public static File getParentDir() {
        File file = new File(SystemUtils.getUserHome(), dir);
        FileUtils.ensureDirExist(file);
        return file;
    }
}
