package bebetter.jpa.util;

import bebetter.jpa.datetimes.BaseDateTimes;
import bebetter.jpa.model.Cond;
import bebetter.statics.util.V;

import java.time.LocalDateTime;

public class OtherUtil {
    public static <T, Times extends BaseDateTimes> void setTimes(Cond<T> cond, Times times) {
        if (null != times) {
            String timeColumn = times.getTimeColumn();
            if (V.noEmpty(timeColumn)) {
                LocalDateTime startDates = times.getStart();
                LocalDateTime endDates = times.getEnd();
                if (null != startDates) {
                    cond.gt(timeColumn, startDates);
                }
                if (null != endDates) {
                    cond.lt(timeColumn, endDates);
                }
            }
        }
    }
}
