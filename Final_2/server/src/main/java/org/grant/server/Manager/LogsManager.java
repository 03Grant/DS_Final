package org.grant.server.Manager;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogsManager {

    // 这个是线程安全的。
    private static final Logger LOGGER = Logger.getLogger(LogsManager.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            // 设置日志文件的位置和格式
            fileHandler = new FileHandler("/app/logs/server_logs.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logAEMessage(String message) {
        LOGGER.info(formatLogMessage("AE", message));
    }

    public static void logRMMessage(String message) {
        LOGGER.info(formatLogMessage("RM", message));
    }

    public static void logHeartbeatMessage(String message) {
        LOGGER.info(formatLogMessage("Heartbeat", message));
    }

    private static String formatLogMessage(String messageType, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("[%s] [%s] - %s", timestamp, messageType, message);
    }
}

