package bebetter.basejpa.cfg.typehandler;/*
package bebetter.system.base.config.typehandler;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 继承自BaseTypeHandler<Object> 使用Object是为了让JSONUtil可以处理任意类型
public class TypeHandlerJSON extends BaseTypeHandler<JSON> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSON parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSONUtil.toJsonStr(parameter));
    }

    @Override
    public JSON getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return JSONUtil.parse(rs.getString(columnName));
    }

    @Override
    public JSON getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JSONUtil.parse(rs.getString(columnIndex));
    }

    @Override
    public JSON getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return JSONUtil.parse(cs.getString(columnIndex));
    }
}*/
