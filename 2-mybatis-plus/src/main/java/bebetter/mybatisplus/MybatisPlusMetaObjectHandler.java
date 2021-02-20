package bebetter.mybatisplus;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自动为实体创建时间和更新时间赋值
 *
 * @date 2019-04-17
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        Long id = null;
//        if (metaObject.hasGetter(MpSupport.ID_COLUMN)) {
//            id = (Long) this.getFieldValByName(MpSupport.ID_COLUMN, metaObject);
//        }
        Date now = new Date();
//        Date now = null == id ? new Date() : SnowflakeIdUtil.convertDate(id);
        this.setFieldValByName(MpSupport.CREATE_TIME_COLUMN, now, metaObject);
        if (metaObject.hasSetter(MpSupport.CREATE_DATE_COLUMN)) {
            this.setFieldValByName(MpSupport.CREATE_DATE_COLUMN, Integer.valueOf(DateUtil.format(now, "yyMMdd")), metaObject);
        }
        this.setFieldValByName(MpSupport.UPDATE_TIME_COLUMN, now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName(MpSupport.UPDATE_TIME_COLUMN, new Date(), metaObject);
    }
}
