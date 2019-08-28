package bebetter.basejpa.model.math;


import bebetter.statics.util.V;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 树状 parent=null或""的为顶级
 *
 * @param <O>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElementTree<O> extends ArrayList<ElementTreeNode<O>> {

    /**
     * list 构造element格式的tree(map/json)
     *
     * @param objs             列表
     * @param codeMethod       获取code的方法
     * @param parentCodeMethod 获取parentCode的方法  (parentCode=code来关联父子节点)
     * @param labelMethod      获取界面展示名字的方法
     */
    public ElementTree(Collection<O> objs,
                       Function<O, ?> codeMethod,
                       Function<O, ?> parentCodeMethod,
                       Function<O, ?> labelMethod) {
        super(16);
        Map<String, List<O>> parentCodeListMap = objs.stream()
                .collect(Collectors.groupingBy(o -> toString(parentCodeMethod.apply(o))));
        //顶级节点(可能好几个)
        addAll(recursion(toString(null), parentCodeListMap, codeMethod, labelMethod));
    }

    //父节点获旗下子节点
    private List<ElementTreeNode<O>> recursion(String parentCode,
                                               Map<String, List<O>> groupByParentCode,
                                               Function<O, ?> codeMethod,
                                               Function<O, ?> labelMethod) {
        List<O> chidrenNode = V.or(groupByParentCode.get(toString(parentCode)), Collections.emptyList());
        return chidrenNode.stream().map((o) -> {
            String code = toString(codeMethod.apply(o));
            //父节点获旗下子节点
            List<ElementTreeNode<O>> childNodes = recursion(code, groupByParentCode, codeMethod, labelMethod);
            return new ElementTreeNode<>(o, childNodes, code, labelMethod);
        }).collect(Collectors.toList());
    }

    /**
     * 根据子节点codes,获取包含他们上层父节点的树  [不包含其他分支]
     *
     * @param tree         完整树
     * @param containCodes codes(可以只有子节点的,也可以包含父节点)
     */
    public ElementTree(ElementTree<O> tree, Collection containCodes) {
        super(16);
        if (V.empty(containCodes)) {
            return;
        }
        Map<Object, Boolean> containsMap = new HashMap<>(256);
        //递归判断是否,每个节点是否包含 (有子节点,父节点也展示)
        tree.forEach(node -> this.recursionContains(node, containsMap, containCodes));

        //递归过滤出需要包含的节点,组成新的树
        addAll(tree.stream().map(node -> this.recursionNewNodeDoFilter(node, containsMap))
                .filter(Objects::nonNull).collect(Collectors.toList())
        );
    }

    /**
     * 根据子节点codes,获取包含他们上层父节点的树  [不包含其他分支]
     *
     * @param tree           完整树
     * @param codeBooleanMap 对应code节点是否展示
     * @param nullRemain     若codeBooleanMap不存在该code,是否展示该节点
     */
    public ElementTree(ElementTree<O> tree, Map<?, Boolean> codeBooleanMap, boolean nullRemain, boolean emptyChildrenRemain) {
        super(16);
        Map<Object, Boolean> containsMap = new HashMap<>(256);
        //递归判断是否,每个节点是否包含 (有子节点,父节点也展示)
        tree.forEach(node -> this.recursionContains(node, containsMap, codeBooleanMap, nullRemain, emptyChildrenRemain));

        //递归过滤出需要包含的节点,组成新的树
        addAll(tree.stream().map(node -> this.recursionNewNodeDoFilter(node, containsMap))
                .filter(Objects::nonNull).collect(Collectors.toList())
        );
    }

    //递归过滤出需要包含的节点,组成新的树
    private ElementTreeNode<O> recursionNewNodeDoFilter(ElementTreeNode<O> node, Map<Object, Boolean> containsMap) {
        Boolean contains = containsMap.get(node.getCode());
        if (null == contains || !contains) {
            return null;
        }
        ElementTreeNode<O> newNode = new ElementTreeNode<>();
        newNode.putAll(node);
        List<ElementTreeNode<O>> newChildren = newNode.getChildren().stream()
                .map(childNode -> recursionNewNodeDoFilter(childNode, containsMap))
                .filter(Objects::nonNull).collect(Collectors.toList());
        newNode.setChildren(newChildren);
        return newNode;
    }

    private boolean recursionContains(ElementTreeNode<O> node, Map<Object, Boolean> containsMap, Collection containCodes) {
        //是否包含自己
        boolean isContainsThis = containCodes.contains(node.getCode());
        //有子节点包含就包含
        Set<Boolean> childrenContains = node.getChildren().stream().map(childNode -> this.recursionContains(childNode, containsMap, containCodes)).collect(Collectors.toSet());

        //最后是否包含
        boolean contains = isContainsThis || childrenContains.stream().anyMatch(Boolean::booleanValue);
        containsMap.put(node.getCode(), contains);
        return contains;
    }

    private boolean recursionContains(ElementTreeNode<O> node, Map<Object, Boolean> containsMap, Map<?, Boolean> codeBooleanMap, boolean nullRemain, boolean emptyChildrenRemain) {
        Boolean flag;
        //是否包含自己
        Boolean thisContains = codeBooleanMap.get(node.getCode());
        //有子节点包含就包含
        Set<Boolean> childrenConstainsSet =
                node.getChildren().stream().map(childNode -> this.recursionContains(childNode, containsMap, codeBooleanMap, nullRemain, emptyChildrenRemain)).collect(Collectors.toSet());
        //是叶子节点
        if (V.empty(childrenConstainsSet)) {
            if (null != thisContains) {
                flag = thisContains;
            } else {
                flag = nullRemain;
            }
        }
        //不是叶子节点
        else {
            flag = childrenConstainsSet.stream().anyMatch(Boolean::booleanValue);
            if (flag && emptyChildrenRemain) {
                flag = true;
            }
        }

        containsMap.put(node.getCode(), flag);
        return flag;
    }

    //因为javaStream不能key为空,默认空字符串与空一致
    private String toString(Object code) {
        if (null == code) {
            code = "";
        }
        return code.toString();
    }

    public ElementTree<O> sort(String propName) {
        sort(this, propName);
        return this;
    }

    private void sort(List<ElementTreeNode<O>> nodes, String propName) {
        //unchecked
        nodes.sort((o1, o2) -> {
            Comparable comparable = (Comparable) o1.get(propName);
            if (null == comparable) {
                return -1;
            }
            if (null == o2.get(propName)) {
                return 1;
            }
            return comparable.compareTo(o2.get(propName));
        });
        nodes.forEach((node) -> {
            if (V.noEmpty(node.getChildren())) {
                sort(node.getChildren(), propName);
            }
        });
    }
}
