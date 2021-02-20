package bebetter.mybatisplus;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页中时间过滤
 *
 * @author LiZuBin
 * @date 2020/8/27
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WhereTimeProp {
    //过滤属性名
    private String column;
    private String startTime;
    private String endTime;

    public static WhereTimeProp buildByDate(Object startTime, Object endTime) {
        String startTimeFinal = null == startTime ? null : DateUtil.formatDateTime(DateUtil.beginOfDay(DateUtil.parseDate(startTime.toString())));
        String endTimeFinal = null == endTime ? null : DateUtil.formatDateTime(DateUtil.endOfDay(DateUtil.parseDate(endTime.toString())));
        return new WhereTimeProp("create_time", startTimeFinal, endTimeFinal);
    }
}
