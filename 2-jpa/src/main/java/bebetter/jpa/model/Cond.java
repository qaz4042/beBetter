package bebetter.jpa.model;

import bebetter.jpa.enums.EnumCondOpt;
import bebetter.jpa.util.Sfunction;
import bebetter.jpa.util.Ssupplier;
import bebetter.statics.model.KnowException;
import bebetter.statics.util.ClassUtil;
import bebetter.jpa.util.MethodUtil;
import bebetter.statics.util.V;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Condition,构造Where中的 and/or 条件
 * Where中的每项条件
 * 默认是and拼接
 * 不规则树
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "unchecked"})
@Data
public class Cond<T> extends ArrayList<CondNode> implements CondNode {

    //是不是最底层
    private boolean isSub = false;
    //list内是and还是or关系
    private boolean isAnd = true;

    public Cond(T t) {
        this(t, EnumCondOpt.eq);
    }

    public Cond(T t, boolean isLike) {
        this(t, isLike ? EnumCondOpt.like : EnumCondOpt.eq);
    }

    private Cond(T t, EnumCondOpt opt) {
        ClassUtil.props(t, (field, value) -> V.and(value, val -> this.addSubCond(field.getName(), opt, value)));
    }

    public Cond() {
        this(false, true, (Cond<T>[]) null);
    }

    @SuppressWarnings("unchecked")
    private Cond(boolean isSub, boolean isAnd, Cond<T>... conditios) {
        this(isSub, isAnd, null == conditios ? null : Arrays.asList(conditios));
    }

    private Cond(boolean isSub, boolean isAnd, List<Cond<T>> conditios) {
        super(8);
        this.isSub = isSub;
        this.isAnd = isAnd;
        if (null != conditios) {
            addAll(conditios);
        }
    }

    private static <O> Cond<O> and(List<Cond<O>> conditios) {
        return new Cond<>(false, true, conditios);
    }

    //    默认是and拼接
    @SafeVarargs
    public static <O> Cond<O> and(Cond<O>... conditios) {
        return new Cond<>(false, true, conditios);
    }

    @SafeVarargs
    public static <O> Cond<O> or(Cond<O>... conditios) {
        return new Cond<>(false, false, conditios);
    }

    private static <T> Predicate buildSub(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb, SubCond condition) {
        //Number
        Object[] targets = condition.targets;
        Path path = root.get(condition.columnName);
        Object target1 = null != targets && targets.length > 0 ? targets[0] : null;
        Object target2 = null != targets && targets.length > 1 ? targets[1] : null;
        switch (condition.opt) {
            case eq:
            case like:
            case gt:
            case lt:
            case ge:
            case le:
                if (null == target1) {
                    return getFalse(cb);
                }
        }
        switch (condition.opt) {
            //--------------where语句----------------
            case eq:
                return cb.equal(path, target1);
            case ne:
                return cb.notEqual(path, target1);
            case like:
                String s = target1.toString();
                if (!s.contains("%")) {
                    s = "%" + s + "%";
                }
                return cb.like(path, s);
            case gt:
                return cb.greaterThan(path, (Comparable) target1);
            case lt:
                return cb.lessThan(path, (Comparable) target1);
            case ge:
                return cb.greaterThanOrEqualTo(path, (Comparable) target1);
            case le:
                return cb.lessThanOrEqualTo(path, (Comparable) target1);
            case in:
                if (V.empty(targets)) {
                    return getFalse(cb);
                }
                return path.in(targets);
//            case notin:
//                return cb.not(root.get(condition.columnName).in(condition.targets));
//            case exists:
//                return cb.exists(null);
            case between:
                //noinspection unchecked
                return cb.between(path, (Comparable) target1, (Comparable) target2);
            //----------------select语句------------------
            case select:
                query.select(path);
                //.alias(alia_id))
                return null;
            //todo 未完待续
            default:
                throw new KnowException("未知的查询操作.");
        }
    }

    private static Predicate getFalse(CriteriaBuilder cb) {
        return cb.notEqual(cb.literal(1), 1);
    }

    /**
     * 构造 where中的and 语句
     *
     * @param column "对象::getXX" (最终sql可以解析出属性名+属性值)
     * @return Cond 本条件存储器实例(实例内部默认为and连接)
     * 例如: ( user_name = '小明' and user_type = '普通用户')
     * 如果需要or请使用
     * Cond cond1=userDao.cond().eq(user2::getName)
     * Cond cond2=userDao.cond().eq(user2::getType)
     * Cond condOr = Cond.or(cond1,cond2);
     */
    public Cond<T> eq(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.eq);
    }

    /**
     * 构造 where中的and 语句
     *
     * @param column "类::getXX" (最终sql可以解析出属性名)
     * @param value  属性值
     */
    public Cond<T> eq(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.eq, value);
    }

    /**
     * 构造 where中的and 语句
     *
     * @param columnName 属性名
     * @param value      属性值
     */
    public Cond<T> eq(String columnName, Object value) {
        return addSubCond(columnName, EnumCondOpt.eq, value);
    }

    public Cond<T> ne(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.ne);
    }

    public Cond<T> ne(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.ne, value);
    }

    public Cond<T> gt(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.gt);
    }

    public Cond<T> gt(String columnName, Object value) {
        return addSubCond(columnName, EnumCondOpt.gt, value);
    }

    public Cond<T> gt(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.gt, value);
    }

    public Cond<T> lt(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.lt);
    }

    public Cond<T> lt(String columnName, Object value) {
        return addSubCond(columnName, EnumCondOpt.lt, value);
    }

    public Cond<T> lt(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.lt, value);
    }

    public Cond<T> isnull(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.isnull);
    }

    public Cond<T> isnull(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.isnull, value);
    }

    public Cond<T> nonull(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.nonull);
    }

    public Cond<T> nonull(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.nonull, value);
    }

    public Cond<T> like(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.like);
    }

    public Cond<T> like(String columnName, Object value) {
        return addSubCond(columnName, EnumCondOpt.like, value);
    }

    public Cond<T> like(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.like, value);
    }

    public Cond<T> between(Sfunction<T, ?> column, Object... value) {
        return addSubCond(column, EnumCondOpt.between, value);
    }

/*
若条件经常为空 推荐使用  > and < 的组合来完成
    public Cond<T> between(String columnName, Object... value) {
        return addSubCond(columnName, EnumCondOpt.between, value);
    }
*/

    public Cond<T> ge(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.ge);
    }

    public Cond<T> ge(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.ge, value);
    }

    public Cond<T> le(Ssupplier column) {
        return addSubCond(column, EnumCondOpt.le);
    }

    public Cond<T> le(Sfunction<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.le, value);
    }

    /*    一般是多个 public Cond<T> in(Seri_Supplier column)*/

    /**
     * @param column "类::getXX" (最终sql可以解析出属性名)
     * @param value  多个值  ,若值太多,sql"...in(value1,value2...)"太长mysql会报错
     */
    public Cond<T> in(Sfunction<T, ?> column, Object... value) {
        if (null != value && value[0] instanceof Collection) {
            value = ((Collection) value[0]).toArray();
        }
        return addSubCond(column, EnumCondOpt.in, value);
    }

//    public Cond<T> notin(Seri_Supplier column) {
//        return addSubCond(column, EnumCondOpt.notin);
//    }

//    public Cond<T> notin(Seri_Function<T, ?> column, Object value) {
//        return addSubCond(column, EnumCondOpt.notin, value);
//    }

    /*public Cond<T> exists(Seri_Supplier column) {
        return addSubCond(column, EnumCondOpt.notin);
    }
    public Cond<T> exists(Seri_Function<T, ?> column, Object value) {
        return addSubCond(column, EnumCondOpt.notin, value);
    }*/


    private Cond<T> addSubCond(Ssupplier column, EnumCondOpt opt) {
        add(new SubCond(opt, MethodUtil.getLambdaColumnName(column), column.get()));
        return this;
    }

    private Cond<T> addSubCond(String columnName, EnumCondOpt opt, Object... columnValue) {
        add(new SubCond(opt, columnName, columnValue));
        return this;
    }

    private Cond<T> addSubCond(Sfunction<T, ?> column, EnumCondOpt opt, Object... value) {
        add(new SubCond(opt, MethodUtil.getLambdaColumnName(column), value));
        return this;
    }

    public Predicate buildPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return buildPredicate(root, query, cb, this);
    }

    @SuppressWarnings("unchecked")
    private static <T> Predicate buildPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb, Cond<T> condition) {
        Predicate[] predicates = condition.stream().filter(Objects::nonNull).map(cond1 -> {
            if (cond1 instanceof Cond) {
                //递归
                return buildPredicate(root, query, cb, (Cond<T>) cond1);
            }
            if (cond1 instanceof SubCond) {
                return buildSub(root, query, cb, (SubCond) cond1);
            }
            return null;
        }).filter(Objects::nonNull).toArray(Predicate[]::new);
        if (V.empty(predicates)) {
            return null;
        }
        if (condition.isAnd) {
            return cb.and(predicates);
        } else {
            return cb.or(predicates);
        }
    }

    @SafeVarargs
    public final Cond<T> select(Sfunction<T, ?>... column) {
        List<Cond<T>> list = Arrays.stream(column)
                .map(c -> addSubCond(c, EnumCondOpt.select, (Object[]) null))
                .collect(Collectors.toList());
        return and(list);
    }
}


@Data
class SubCond implements CondNode {

    EnumCondOpt opt;
    String columnName;
    Object[] targets;

    SubCond(EnumCondOpt op, String columnName, Object... target) {
        this.opt = op;
        this.columnName = columnName;
        this.targets = target;
    }
}
