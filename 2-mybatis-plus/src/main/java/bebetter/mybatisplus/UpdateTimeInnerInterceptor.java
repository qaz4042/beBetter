package bebetter.mybatisplus;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * UpdateWrapper  LambdaUpdateWrapper .set() 自动填充 updateTime字段简单处理
 */
@Slf4j
public class UpdateTimeInnerInterceptor extends JsqlParserSupport implements InnerInterceptor {

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        if (SqlCommandType.UPDATE != ms.getSqlCommandType()) {
            return;
        }
        if (parameter instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) parameter;
            doUpdateTimeFill(map, ms.getId());
        }
    }

    protected void doUpdateTimeFill(Map<String, Object> map, String msId) {
        //updateById(et), update(et, wrapper);
        Object et = map.getOrDefault(Constants.ENTITY, null);
        if (et != null) {
            //由metaHandler处理
            return;
        }
        TableInfo tableInfo;
        try {
            final String mapperFullName = msId.substring(0, msId.lastIndexOf(StringPool.DOT));
            final String mapperName = mapperFullName.substring(mapperFullName.lastIndexOf(StringPool.DOT) + 1);
            final String entityName = mapperName.substring(0, mapperName.length() - 6);
            tableInfo = TableInfoHelper.getTableInfo(StringUtils.camelToUnderline(entityName));
        } catch (Exception e) {
            log.error("updateTime拦截填充异常", e);
            return;
        }
        if (null == tableInfo) {
            return;
        }
        if (null == tableInfo.getEntityType()) {
            return;
        }
        final TableFieldInfo updateFillField =
                tableInfo.getFieldList().stream()
                        .filter(TableFieldInfo::isWithUpdateFill)
                        .filter(item -> MpSupport.UPDATE_TIME_COLUMN.equals(item.getProperty()))
                        .findFirst().orElse(null);

        if (null == updateFillField || updateFillField.getField().getDeclaringClass() != GeneralEntity.class) {
            return;
        }

        //update.set();
        AbstractWrapper<?, ?, ?> aw = (AbstractWrapper<?, ?, ?>) map.getOrDefault(Constants.WRAPPER, null);
        if (aw instanceof UpdateWrapper) {
            ((UpdateWrapper) aw).set(StrUtil.toUnderlineCase(MpSupport.UPDATE_TIME_COLUMN), new Date());
        } else if (aw instanceof LambdaUpdateWrapper) {
            ((LambdaUpdateWrapper<GeneralEntity>) aw).set(GeneralEntity::getUpdateTime, new Date());
        }
    }

    /*@Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler handler = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = handler.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct != SqlCommandType.UPDATE) {
            return;
        }
        final Object parameterObject = handler.parameterHandler().getParameterObject();
        if (!(parameterObject instanceof Map)) {
            return;
        }
        BoundSql boundSql = handler.boundSql();
        parserMulti(boundSql.getSql(), parameterObject);
    }

    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        final TableInfo tableInfo = TableInfoHelper.getTableInfo(update.getTable().getName());
        if (tableInfo == null || !tableInfo.isWithUpdateFill()) {
            return;
        }
        final List<TableFieldInfo> updateFillFieldList =
                tableInfo.getFieldList().stream()
                        .filter(TableFieldInfo::isWithUpdateFill)
                        .filter(item -> MpSupport.UPDATE_TIME_COLUMN.equals(item.getProperty()))
                        .collect(Collectors.toList());
        if (updateFillFieldList.size() == 0) {
            return;
        }
        AbstractWrapper<?, ?, ?> aw = (AbstractWrapper<?, ?, ?>) ((Map) obj).getOrDefault(Constants.WRAPPER, null);
        if (aw instanceof UpdateWrapper) {
            ((UpdateWrapper) aw).set(MpSupport.UPDATE_TIME_COLUMN, new Date());
        } else if (aw instanceof LambdaUpdateWrapper) {
            ((LambdaUpdateWrapper<GeneralEntity>) aw).set(GeneralEntity::getUpdateTime, new Date());
        }
    }*/
}
