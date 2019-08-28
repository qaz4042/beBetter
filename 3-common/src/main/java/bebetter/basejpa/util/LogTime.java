package bebetter.basejpa.util;

import bebetter.statics.util.V;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 打印方法执行时间工具类
 */
@Slf4j
public class LogTime {
//    private static final Logger log = LoggerFactory.getLogger(LogTime.class);


    private static Map<Thread, Long> currentTimeMap = new HashMap<>(100);

    /**
     * 1.记录当前时间
     */
    public static Long record() {
        return record(getNowTimeLong());
    }

    /**
     * 2,打印时间差【logTime和recordTime（logTimeAndRecode）之间的时间差】
     *
     * @param extInfo 可以是执行改方法前，处理的事务的名称。例如：getUserById
     */
  /*  private static Long log(String extInfo) {
        return log(false, extInfo);
    }
*/

    /**
     * 3,打印上次时间差，并记录当前时间
     *
     * @param extInfo 同上
     */
    public static Long logAndRecord(String... extInfo) {
        return log(false, true, extInfo);
    }

    private static Long log(boolean systemOut, boolean needRecode, String... extInfo) {
        Long fromPoint = currentTimeMap.get(Thread.currentThread());
        return log(fromPoint, needRecode, extInfo);
    }

    private static Long log(Long fromPoint, boolean needRecode, String... extInfo) {
        long nowPoint = getNowTimeLong();
        String costTime = "";
        if (null != fromPoint) {
            costTime = "|耗时=" + (nowPoint - fromPoint + "ms");
        } else {
            costTime = "|初始时间";
        }
        String extInfoAppend = "";
        if (V.noEmpty(extInfo)) {
            if (extInfo.length == 1) {
                extInfoAppend = extInfo[0];
            } else {
                extInfoAppend = Arrays.toString(extInfo);
            }

        }
        String msg = "stamp=" + nowPoint + costTime + "|信息=" + extInfoAppend;
        log.warn(msg);
        if (needRecode) {
            record(nowPoint);
        }
        return nowPoint;
    }

    private static Long record(Long nowPoint) {
        currentTimeMap.put(Thread.currentThread(), nowPoint);
        return nowPoint;
    }

    private static long getNowTimeLong() {
        return System.currentTimeMillis();
    }
}
