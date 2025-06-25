package redcoder.texteditor.utils;

import org.junit.Assert;
import org.junit.Test;

public class SystemUtilsTest {

    @Test
    public void getUserDir() {
        Assert.assertEquals("D:\\Project\\text-editor",SystemUtils.getUserDir());
    }

}
