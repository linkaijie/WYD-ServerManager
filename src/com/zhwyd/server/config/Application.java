package com.zhwyd.server.config;
import java.nio.charset.Charset;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.ApplicationContext;
import com.zhwyd.server.common.util.ThreadPool;
import com.zhwyd.server.thread.MonitorThread;
public class Application {
    private static String                       webRootPath;
    private static ApplicationContext           context;
    private static HierarchicalINIConfiguration hierarchicalINIConfiguration;
    private ThreadPool                          simpleThreadPool   = null;
    private static Application                  applicationContext = null;

    private Application() {
        this.simpleThreadPool = new ThreadPool(500);
        MonitorThread monitorThread = new MonitorThread();
        monitorThread.start();
    }

    public static Application getManager() {
        synchronized (Application.class) {
            if (null == applicationContext) {
                applicationContext = new Application();
            }
        }
        return applicationContext;
    }

    public static void setWebRootPath(String webRootPath) {
        Application.webRootPath = webRootPath;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        Application.context = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBean(String id) {
        return context.getBean(id);
    }

    public static void config(HierarchicalINIConfiguration hierarchicalINIConfiguration) {
        Application.hierarchicalINIConfiguration = hierarchicalINIConfiguration;
    }

    public static String getConfig(String section, String key) {
        return hierarchicalINIConfiguration.getSection(section).getString(key);
    }

    public static int getConfigInt(String section, String key) {
        return hierarchicalINIConfiguration.getSection(section).getInt(key);
    }

    public static boolean getConfigBoolean(String section, String key) {
        return hierarchicalINIConfiguration.getSection(section).getBoolean(key);
    }

    public static String getConfigFilePath(String section, String key) {
        String filePath = getConfig(section, key);
        if (filePath == null) {
            return null;
        }
        if (FilenameUtils.getPrefix(filePath).isEmpty()) {
            filePath = FilenameUtils.concat(webRootPath, filePath);
        }
        return filePath;
    }

    public static String getDefaultEncoding() {
        String defaultEncoding = Application.getConfig("system", "default_encoding");
        if (defaultEncoding != null) {
            return defaultEncoding;
        } else {
            return "UTF-8";
        }
    }

    public static Charset getDefaultCharset() {
        return Charset.forName(getDefaultEncoding());
    }

    public void setSimpleThreadPool() {
        simpleThreadPool = new ThreadPool(500);
    }

    public ThreadPool getSimpleThreadPool() {
        return simpleThreadPool;
    }
}
