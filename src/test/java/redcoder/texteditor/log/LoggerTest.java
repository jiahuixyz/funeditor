package redcoder.texteditor.log;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerTest {

    private static final Logger logger = Logger.getLogger(LoggerTest.class.getName());

    @BeforeClass
    public static void before(){
        LoggingUtils.resetLogManager();
    }

    @Test
    public void log() throws InterruptedException {
        logger.log(Level.INFO, "hello world", new RuntimeException("error"));
        new Thread(() -> {
            logger.log(Level.INFO, "hello world????????????????????????", new RuntimeException("??????????????????????"));
        }).start();

        TimeUnit.SECONDS.sleep(3);
    }
}
