package bebetter.jpa.datetimes;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseDateTimes {
    public abstract LocalDateTime getStart();

    public abstract LocalDateTime getEnd();

    public abstract String getTimeColumn();

    public static final String CreateTime = "createTime";//默认是createTime
}
