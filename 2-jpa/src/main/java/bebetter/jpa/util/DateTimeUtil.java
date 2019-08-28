package bebetter.jpa.util;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
@SuppressWarnings("WeakerAccess")
@Slf4j
public class DateTimeUtil {
    /**
     * 格式化
     */
    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
