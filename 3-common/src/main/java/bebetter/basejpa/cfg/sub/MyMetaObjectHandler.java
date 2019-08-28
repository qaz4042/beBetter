//package bebetter.basejpa.cfg.sub;
//
//import bebetter.basejpa.model.base.BaseModel;
//import V;
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import com.baomidou.mybatisplus.core.toolkit.IdWorker;
//import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
//import org.apache.ibatis.reflection.MetaObject;
//
//import java.time.LocalDateTime;
//
///**
// * 元对象字段填充 填充创建时间 更新时间 以及其他
// */
//public class MyMetaObjectHandler implements MetaObjectHandler {
//
//    public MyMetaObjectHandler() {
//    }
//
//    /**
//     * 注入通用属性
//     */
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        // 获取对象table信息
//        TableInfo tableInfo = TableInfoHelper.getTableInfo(metaObject.getOriginalObject().getClass());
//        // 如果是用户自定义id 并且字段类型是字符串 则 调用原有的IdWorker生成策略设置字符串主键
//        if (empty(tableInfo.getKeyProperty(), metaObject)) {
//            if (tableInfo.getIdType() == IdType.INPUT) {
//                Class getterType = metaObject.getGetterType(tableInfo.getKeyProperty());
//                if (getterType == String.class) {
//                    setFieldValByName(tableInfo.getKeyProperty()Worker.getIdStr(), metaObject);
//                } else if (getterType == Long.class) {
//                    setFieldValByName(tableInfo.getKeyProperty()Worker.getId(), metaObject);
//                } else {
//                    throw new IllegalArgumentException("不支持的主键类型class，现仅支持Long 和 String ");
//                }
//            }
//        }
//
//        // 设置新增通用属性
//        if (empty(BaseModel.field_createTime, metaObject)) {
//            setFieldValByName(BaseModel.field_createTime, LocalDateTime.now(), metaObject);
//        }
//        if (empty(BaseModel.field_deleted, metaObject)) {
//            setFieldValByName(BaseModel.field_deleted, false, metaObject);
//        }
//        // 设置删除字段值
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        if (empty(BaseModel.field_modifyTime, metaObject)) {
//            setFieldValByName(BaseModel.field_modifyTime, LocalDateTime.now(), metaObject);
//        }
//    }
//
//    /**
//     * 判断值是是否是空值
//     *
//     * @param fieldName  属性名
//     * @param metaObject 目录对象
//     * @return boolean
//     */
//    private boolean empty(String fieldName, MetaObject metaObject) {
//        return V.empty(getFieldValByName(fieldName, metaObject));
//    }
//
//}
