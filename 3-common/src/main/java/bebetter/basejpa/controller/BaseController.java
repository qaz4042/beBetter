package bebetter.basejpa.controller;

import bebetter.basejpa.model.base.BaseUser;
import bebetter.basejpa.model.base.Id_CreateTime;
import bebetter.basejpa.model.vo.Pagein;
import bebetter.basejpa.util.SpringUtils;
import bebetter.jpa.base.BaseDao;
import bebetter.jpa.datetimes.Dates;
import bebetter.jpa.model.Cond;
import bebetter.statics.model.KnowException;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.List;

/**
 * 用户端调用的controller [ 不需登录 ]
 *
 * @param <T> 实体类
 */
public abstract class BaseController<T extends Id_CreateTime> {

    /**
     * 分页
     */
    @RequestMapping("/page")
    public PageImpl<T> page(Pagein page, T model, Dates times) {
        return getDao().page(page, new Cond<>(model), times);
    }

    @RequestMapping("/list")
    public List<T> list(T model) {
        return getDao().list(model);
    }

    @RequestMapping("/one")
    @SneakyThrows
    public T one(@PathVariable Serializable id) {
        return getDao().getOne(id);
    }

//    @RequestMapping("/add")
    public Serializable add(@Validated @RequestBody T model) {
        addOrUpdate(model);
        return model.getId();
    }

    /**
     * 可以重写开启接口
     */
    //@RequestMapping("/update")
    public void update(@Validated @RequestBody T model) {
        addOrUpdate(model);
    }

    //@RequestMapping("/deletes")
    public void deletes(@RequestBody List<Long> ids) {
        this.getDao().deleteInBatch(ids);
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

