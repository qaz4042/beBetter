package bebetter.jpa.datetimes;

import bebetter.jpa.datetimes.BaseDateTimes;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Dates extends BaseDateTimes {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startTimes;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate endTimes;
    String timeColumn = "createTime";//默认是createTime

    @Override
    public LocalDateTime getStart() {
        if (null == startTimes) {
            return null;
        }
        return LocalDateTime.of(startTimes, LocalTime.MIN);
    }

    @Override
    public LocalDateTime getEnd() {
        if (null == endTimes) {
            return null;
        }
        return LocalDateTime.of(endTimes, LocalTime.MAX);
    }
}
