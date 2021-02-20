package bebetter.mybatisplus;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @date 2020-01-01
 */
public class GeneralServiceImpl<M extends GeneralMapper<T>, T> extends ServiceImpl<M, T> implements GeneralService<T> {

    @Override
    public Page<T> page(Map<String, Object> params) {
        return page(params, (WhereTimeProp) null);
    }

    @Override
    public Page<T> page(PageQuery pageQuery) {
        Page<T> page = MpSupport.getPage(pageQuery);
        QueryWrapper<T> queryWrapper = MpSupport.getQueryWrapper(BeanUtil.beanToMap(pageQuery), currentModelClass());
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<T> page(Page<T> page, T param) {
        return baseMapper.selectPage(page, MpSupport.getQueryWrapperMore(param));
    }

    @Override
    public List<T> list(T entity) {
        return this.list(Wrappers.lambdaQuery(entity));
    }

    @Override
    public Page<T> page(Map<String, Object> params, List<WhereTime<T>> whereTimes) {
        Page<T> page = MpSupport.getPage(params);
        QueryWrapper<T> queryWrapper = MpSupport.getQueryWrapper(params, currentModelClass());
        if (null != whereTimes) {
            whereTimes.forEach(w -> queryWrapper.lambda()
                    .ge(StrUtil.isNotBlank(w.getStartTime()), w.getColumn(), w.getStartTime())
                    .le(StrUtil.isNotBlank(w.getEndTime()), w.getColumn(), w.getEndTime())
            );
        }
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<T> page(Map<String, Object> params, WhereTimeProp whereTime) {
        Page<T> page = MpSupport.getPage(params);
        QueryWrapper<T> queryWrapper = MpSupport.getQueryWrapper(params, currentModelClass());
        if (null != whereTime) {
            String column = ObjectUtil.defaultIfBlank(whereTime.getColumn(), "create_time");
            queryWrapper
                    .ge(StrUtil.isNotBlank(whereTime.getStartTime()), column, whereTime.getStartTime())
                    .le(StrUtil.isNotBlank(whereTime.getEndTime()), column, whereTime.getEndTime()
                    );
        }
        return baseMapper.selectPage(page, queryWrapper);
    }
}
