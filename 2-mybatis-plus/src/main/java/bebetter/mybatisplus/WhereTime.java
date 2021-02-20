package bebetter.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
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
public class WhereTime<T> {
    //过滤属性名
    private SFunction<T, Object> column;
    private String startTime;
    private String endTime;
}
