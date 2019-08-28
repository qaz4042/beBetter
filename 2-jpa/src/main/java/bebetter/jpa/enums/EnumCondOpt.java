package bebetter.jpa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *limit order by
 */
@AllArgsConstructor
@Getter
public enum EnumCondOpt {
    //---------------where语句------------
    eq(),//相等
    gt,//大于 greater
    lt,//小于 littler
    isnull,
    nonull,
    like,
    between,
    ge,//大于等于
    le,//小于等于
    in,
//    notin,
    ne,
    exists,

    //-------------select语句-------------
    select,//选择某几列

    ;
//    BiConsumer<Supplier, Object> fun;

}
