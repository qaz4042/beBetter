package bebetter.basejpa.model.math;

import cn.hutool.core.bean.BeanUtil;
import bebetter.statics.util.V;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

/**
 * ElementTree的一个节点
 *
 * @param <O> O 表示基本对象
 */
class ElementTreeNode<O> extends LinkedHashMap<Object, Object> {
    private static final String FiledName_Children = "children";
    private static final String FiledName_Label = "label";
    private static final String FiledName_Id = "id";//在次相当于code

    ElementTreeNode() {
        super(16);
    }

    ElementTreeNode(O o, List<ElementTreeNode<O>> childrenNode, Object code, Function<O, ?> labelMethod) {
        super(16);
        putAll(BeanUtil.beanToMap(o));
        put(FiledName_Id, code);
        put(FiledName_Label, labelMethod.apply(o));
        put(FiledName_Children, childrenNode);
    }

    Object getCode() {
        //noinspection unchecked
        return get(FiledName_Id);
    }

    List<ElementTreeNode<O>> getChildren() {
        //noinspection unchecked
        return (List<ElementTreeNode<O>>) V.or(get(FiledName_Children), Collections.emptyList());
    }

    public void setChildren(List<ElementTreeNode<O>> childrenNode) {
        put(FiledName_Children, childrenNode);
    }
}
