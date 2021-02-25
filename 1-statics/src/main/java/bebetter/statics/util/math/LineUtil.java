package bebetter.statics.util.math;

import bebetter.statics.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;

/**
 * 线性工具类
 *
 * @author LiZuBin
 * @date 2021/2/25
 */
public class LineUtil {

    /**
     * 求最短路径
     *
     * @param <T> 实体1
     * @param <D> 两个实体的记录
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortestPathData<T, D extends Number> {
        T data1;
        T data2;
        D distance;
    }

    public static <T, D extends Number> BigDecimal shortestPathData(Collection<ShortestPathData<T, D>> distanceDatas, T start, T end) {
        return shortestPathData(distanceDatas, start, end, Collections.emptySet());
    }

    private static <T, D extends Number> BigDecimal shortestPathData(Collection<ShortestPathData<T, D>> distanceDatas, T start, T end, Set<T> superDatas) {
        return distanceDatas.stream().filter(distanceData -> (
                distanceData.getData1().equals(start) && !superDatas.contains(distanceData.getData2()))
                || (distanceData.getData2().equals(start) && !superDatas.contains(distanceData.getData1()))
        ).map(
                (distanceData) -> {
                    BigDecimal minDistance = BigDecimal.valueOf(distanceData.getDistance().doubleValue());
                    T data1 = distanceData.getData1();
                    T data2 = distanceData.getData2();
                    if (data1.equals(start) && !superDatas.contains(data2)) {
                        return getMinDistance(distanceDatas, end, superDatas, minDistance, start, data2);
                    }
                    if (data2.equals(start) && !superDatas.contains(data1)) {
                        return getMinDistance(distanceDatas, end, superDatas, minDistance, start, data1);
                    }
                    return null;
                }
        ).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(null);
    }

    private static <T, D extends Number> BigDecimal getMinDistance(Collection<ShortestPathData<T, D>> distanceDatas, T end,
                                                                   Set<T> superDatas, BigDecimal minDistance, T eqStartData, T eqEndData) {
        if (eqEndData.equals(end)) {
            return minDistance;
        }
        HashSet<T> superDatasNew = new HashSet<>(superDatas);
        superDatasNew.add(eqStartData);
        BigDecimal subMinDistance = shortestPathData(distanceDatas, eqEndData, end, superDatasNew);

        if (null == subMinDistance) {
            return null;
        }
        minDistance = minDistance.add(subMinDistance);
        return minDistance;
    }
}
