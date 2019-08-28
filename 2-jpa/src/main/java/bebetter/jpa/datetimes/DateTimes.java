package bebetter.jpa.datetimes;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class DateTimes extends BaseDateTimes {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTimes;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endTimes;
    String timeColumn = "createTime";//默认是createTime

    @Override
    public LocalDateTime getStart() {
        return startTimes;
    }

    @Override
    public LocalDateTime getEnd() {
        return endTimes;
    }
}
