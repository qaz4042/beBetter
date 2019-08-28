package bebetter.basejpa.controller;


import bebetter.basejpa.model.base.BaseUser;
import bebetter.basejpa.model.vo.Pagein;
import bebetter.basejpa.util.LoginUtil;
import bebetter.basejpa.util.SpringUtils;
import bebetter.jpa.base.BaseDao;
import bebetter.jpa.datetimes.Dates;
import bebetter.jpa.model.Cond;
import bebetter.statics.model.IUserId;
import bebetter.statics.model.KnowException;
import bebetter.statics.util.V;
import cn.hutool.core.util.ClassUtil;
import lombok.SneakyThrows;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户端调用的controller [ 需要先登录 ]
 * BaseUser对象spring自动注入当前用户
 */
@RequiresUser
public abstract class BaseUserController<T extends IUserId<Long, Long>> {

    /**
     * 分页
     */
    @RequestMapping("/page")
    public PageImpl<T> page(BaseUser user, Pagein page, T model, Dates times) {
        model.setUserId(user.getId());
        return getDao().page(page, new Cond<>(model), times);
    }

    @RequestMapping("/list")
    public List<T> list(BaseUser user, T model) {
        model.setUserId(user.getId());
        return getDao().list(model);
    }

    @RequestMapping("/one")
    @SneakyThrows
    public T one(BaseUser user, @PathVariable Long id) {
        //noinspection unchecked,deprecation
        T model = (T) ClassUtil.getTypeArgument(this.getClass(), 1).newInstance();
        model.setId(id);
        model.setUserId(user.getId());
        return getDao().one(model);
    }

    @RequestMapping("/add")
    public Serializable add(BaseUser user, @Validated @RequestBody T model) {
        model.setUserId(user.getId());
        addOrUpdate(model);
        return model.getId();
    }

    /**
     * 注意部分字段不能随意修改
     */
    @RequestMapping("/update")
    public void update(BaseUser user, @Validated @RequestBody T model) {
        model.setUserId(user.getId());
        addOrUpdate(model);
    }

    @SuppressWarnings("unused")
    //@RequestMapping("/deletes") // 默认不允许删除,请重写方法
    public void deletes(BaseUser user, @RequestBody List<Long> ids) {
        List<T> list = this.getDao().list(this.getDao().cond().in(T::getUserId, user.getId()).in(T::getId, ids));//查看本user再做删除
        ids = list.stream().map(T::getId).collect(Collectors.toList());
        if (V.noEmpty(ids)) {
            this.getDao().deleteInBatch(ids);
        }
    }

    private void addOrUpdate(@RequestBody @Validated T model) {
        getDao().save(model);
    }

    @SuppressWarnings("WeakerAccess")
    public abstract Class<? extends BaseDao<T>> getDaoClass();

    public BaseDao<T> getDao() {
        return SpringUtils.getBean(getDaoClass());
    }
}

