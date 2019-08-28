package bebetter.basejpa.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MathUtils {
    /**
     * 组合计注数
     * 例如 所有选项[
     * [1,2,3,4,5,6],      (在其中选几个)
     * [11,22,33,44,55,66] (在其中选几个)
     * ]
     *
     * @param listlist  例如 {第一行选中:1,2,第二行选中:33,44}   [[1,2],[33,44]]
     * @param canRepeat 计数时号码重复的组合是否重复计数  例如 选中 {第一行选中:3,第二行选中:33}(同为第三个数)是否为一注
     * @return 注数
     */
    public static Integer countSelect(List<List<Integer>> listlist, boolean canRepeat) {
        int rs = 1;
        if (canRepeat || listlist.size() == 1) {
            for (List<Integer> selectList : listlist) {
                rs *= selectList.size();
            }
            return rs;
        } else {
            return countSelectRecursion(listlist, new HashSet<>());
        }
    }

    /**
     * 计算 组合数(排除掉部分元素后,组合的情况总数)
     *
     * @param subSelectListList 二维数组
     * @param removeSet         要排除元素的集合
     */
    private static Integer countSelectRecursion(List<List<Integer>> subSelectListList, Set<Integer> removeSet) {
        Integer sum = 0;
        List<List<Integer>> subListList = subSelectListList.subList(1, subSelectListList.size());
        List<Integer> firstList = subSelectListList.get(0);
        //第一个数组,与要排除的集合  的差集
        List<Integer> firstMinusList = removeSet(firstList, removeSet);
        if (subSelectListList.size() == 1) {
            return firstMinusList.size();
        } else {
            for (Integer value : firstMinusList) {
                HashSet<Integer> subRemoveSet = new HashSet<>(removeSet);
                subRemoveSet.add(value);
                sum += countSelectRecursion(subListList, subRemoveSet);
            }
        }
        return sum;
    }

    /**
     * list 排除掉 removeList中的每一个
     */
    private static List<Integer> removeSet(List<Integer> list, Set<Integer> removeList) {
        return list.stream().filter(o -> !removeList.contains(o)).collect(Collectors.toList());
    }

    /**
     * BigDecimal.equals当精度不一致时不相等    例如   new BigDecimal(100.00).equals(new BigDecimal(100))  == false
     */
    public static boolean equals(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == 0;
    }
}
