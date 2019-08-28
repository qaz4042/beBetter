package bebetter.basejpa.controller;

import bebetter.basejpa.model.vo.Pagein;
import bebetter.basejpa.util.SpringUtils;
import bebetter.jpa.base.BaseDao;
import bebetter.jpa.datetimes.Dates;
import bebetter.jpa.model.Cond;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.List;

public abstract class BaseAdminController<T> {
    //当controller方法参数为 HttpSession/InputStream/Reader/Principal/HttpMethod/Locale/TimeZone/ZoneId 这些类和他们的子类（比如user）时都是特殊获取

    /**
     * 分页
     */
    @RequestMapping("/page")
    public PageImpl<T> page(Pagein page, T param, Dates times) {
        return getDao().page(page, new Cond<>(param), times);
    }

    @RequestMapping("/list")
    public List<T> list(T param, Dates times) {
        return getDao().list(param);
    }

    @RequestMapping("/one/{id}")
    public T one(@PathVariable String id) {
        return getDao().findById(id).orElse(null);
    }

    @RequestMapping("/add")
    public void add(@Validated @RequestBody T model) {
        addOrUpdate(model);
    }

    @RequestMapping("/update")
    public void update(@Validated @RequestBody T model) {
        addOrUpdate(model);
    }

    @RequestMapping("/deletes")
    public void deletes(@RequestBody List<Long> ids) {
        this.getDao().deleteInBatch(ids);
    }

    void addOrUpdate(@RequestBody @Validated T model) {
        getDao().save(model);
    }

    @SuppressWarnings("WeakerAccess")
    public abstract Class<? extends BaseDao<T>> getDaoClass();

    public BaseDao<T> getDao() {
        return SpringUtils.getBean(getDaoClass());
    }
}

