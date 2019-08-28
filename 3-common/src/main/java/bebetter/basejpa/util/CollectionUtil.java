package bebetter.basejpa.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static <T> List<T> addAll(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>(list1.size() + list2.size());
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    public static <KEY, T> Map<KEY, T> listToMap(List<T> list, Function<T, KEY> keySupplier) {
        return list.stream().collect(Collectors.toMap(keySupplier, t -> t, (v1, v2) -> {
            throw new RuntimeException("不能重复|v1=" + v1 + "v2=" + v2);
        }));
    }


    public static <KEY, T> Map<KEY, T> listToLinkMap(List<T> list, Function<T, KEY> keySupplier) {
        return list.stream().collect(Collectors.toMap(keySupplier, t -> t, (v1, v2) -> {
            throw new RuntimeException("不能重复|v1=" + v1 + "v2=" + v2);
        }, LinkedHashMap::new));
    }


    public static <KEY, T> Map<KEY, List<T>> groupBy(List<T> list, Function<T, KEY> keySupplier) {
        Map<KEY, List<T>> map = new HashMap<>();
        List<T> subList;
        for (T t : list) {
            KEY key = keySupplier.apply(t);
            subList = map.computeIfAbsent(key, k -> new ArrayList<>());
            subList.add(t);
        }
        return map;
    }

    /**
     * 随机获得列表中的一定量的不重复元素，返回Set
     *
     * @param inputColl 列表
     * @param count     随机取出的个数
     * @return 随机元素
     * @throws IllegalArgumentException 需要的长度大于给定集合非重复总数
     */
    public static <T> List<T> randomEleSet(List<T> inputColl, int count) {
        List<T> input = new ArrayList<>(inputColl);
        final List<T> result = new ArrayList<>(count);
        int size = input.size();
        for (int i = 0; i < count; i++) {
            T o = input.get((int) (Math.random() * size));
            input.remove(o);
            size--;
            result.add(o);
        }
        return result;
    }
}
