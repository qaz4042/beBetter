/*
package bebetter.basejpa.model.math;

import bebetter.statics.util.V;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

*/
/**
 * 用于 实体类一个字段存储多个id ,例如
 * public class User{
 * StringList<Long> roleIds;
 * }
 *//*

@SuppressWarnings("unchecked")
@NoArgsConstructor
public class StringList extends ArrayList<String> {
    //    private List<String> list = new ArrayList<>();
    private static final String Split = ",";//分隔符

    */
/**
     * 入库值  例如: ",10004,1007,1000,"
     *//*

    public String getDbValue() {
        String simpleValue = simpleValue();
        if (V.empty(simpleValue)) {
            return "";
        }
        return Split + simpleValue + Split;
    }

    */
/**
     * 简单拼接  例如: "10004,1007,1000"
     *//*

    private String simpleValue() {
        return String.join(Split, this);
    }


    */
/**
     * 构造
     *
     * @param list 例如:  [10004,1007,1000]
     *//*

    public static StringList of(List list) {
        StringList stringList = new StringList();
        stringList.addAll(tostrList(list));
        return stringList;
    }

    */
/**
     * 构造
     *
     * @param append 例如:  "10004,1007,1000"  或者 ",10004,1007,1000,"
     *//*

    public static StringList of(String append) {
        StringList stringList = new StringList();
        int splitLength = Split.length();

        if (V.noEmpty(append)) {
            if (append.startsWith(Split)) {
                append = append.substring(splitLength);//去头
            }
            if (append.endsWith(Split)) {
                append = append.substring(0, append.length() - splitLength);//去尾
            }

            //String valuesAppend = this.split + values + this.split;
            //去掉头尾
            stringList.addAll(Arrays.stream(append.split(Split)).map(String::trim).collect(Collectors.toList()));
        }
        return stringList;
    }

    @Override
    public boolean add(String o) {
        return super.add(o);
    }

    public boolean add(Serializable o) {
        return add(tostr(o));
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return super.addAll(index, c);
    }

    @Override
    public boolean addAll(Collection c) {
        return super.addAll(tostrList(c));
    }

    @Override
    public String remove(int index) {
        return super.remove(index);
    }

    @Override
    public boolean contains(Object o) {
        return super.contains(tostr(o));
    }

    @Override
    public int indexOf(Object o) {
        return super.indexOf(tostr(o));
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(tostr(o));
    }

    private static String tostr(Object o) {
        return null == o ? null : o.toString();
    }

    private static Collection<String> tostrList(Collection<Object> c) {
        return c.stream().map(StringList::tostr).collect(Collectors.toList());
    }
}
*/
