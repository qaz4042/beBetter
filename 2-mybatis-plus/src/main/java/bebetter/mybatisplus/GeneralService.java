package bebetter.mybatisplus;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @date 2020-01-01
 */
public interface GeneralService<T> extends IService<T> {
    /**
     * 分页查询
     *
     * @param param 参数 携带分页参数
     */
    Page<T> page(Map<String, Object> param);

    /**
     * 分页查询
     */
    Page<T> page(PageQuery pageQuery);

    /**
     * 分页查询
     *
     * @param page  分页
     * @param param 对象每个属性,属性值中若包含 #%#/#># 等表达式 则拼接成 like/> 等特殊查询
     */
    Page<T> page(Page<T> page, T param);

    /**
     * 实体类字段作为条件查询
     */
    List<T> list(T entity);

    /**
     * 分页查询
     *
     * @param params     分页/属性,等条件
     * @param whereTimes 时间过滤条件
     * @return {@link Page<T>}
     * @date 2020-08-27
     */
    Page<T> page(Map<String, Object> params, List<WhereTime<T>> whereTimes);

    /**
     * 更新对象,只更新部分属性
     *
     * @param entity   实体
     * @param setProp 要更新部分属性
     * @return 如果有更新到1行以上记录, 就返回true 0行更新false
     */
    default boolean updatePropById(T entity, SFunction<T, ?> setProp) {
        return updatePropsById(entity, Collections.singletonList(setProp));
    }

    /**
     * 更新对象,只更新部分属性
     *
     * @param entity   实体
     * @param setProps 要更新部分属性
     * @return 如果有更新到1行以上记录, 就返回true 0行更新false
     */
    default boolean updatePropsById(T entity, List<SFunction<T, ?>> setProps) {
        if (null != entity && CollUtil.isNotEmpty(setProps)) {
            UpdateWrapper<T> wrapper = Wrappers.update();

            //例如  set orderState = 1
            setProps.forEach(prop -> wrapper.lambda().set(prop, prop.apply(entity)));

            //例如  where id = 1
            eqId(entity, wrapper);

            return this.update(wrapper);
        }
        return false;
    }

    default void eqId(T entity, UpdateWrapper<T> wrapper) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String idProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(idProperty, "error: can not execute. because can not find column for id from entity!");
        Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
        if (null == idVal) {
            Assert.notEmpty(idProperty, "error: updateById id can not be null");
        }
        wrapper.eq(idProperty, idVal);
    }

    Page<T> page(Map<String, Object> params, WhereTimeProp whereTime);
}
