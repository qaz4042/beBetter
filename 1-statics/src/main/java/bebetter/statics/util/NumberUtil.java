package bebetter.statics.util;

import java.math.BigDecimal;

/**
 * @author LiZuBin
 * @date 2021/2/25
 */
public class NumberUtil extends cn.hutool.core.util.NumberUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Number> T toPrivate(BigDecimal val, Class<T> clazz) {
        if (BigDecimal.class.equals(clazz)) {
            return (T)val;
        }
        if (Integer.class.equals(clazz)) {
            return (T) Integer.valueOf(val.intValue());
        }
        if (Float.class.equals(clazz)) {
            return (T) Float.valueOf(val.intValue());
        }
        if (Double.class.equals(clazz)) {
            return (T) Double.valueOf(val.intValue());
        }
        return null;
    }
}
