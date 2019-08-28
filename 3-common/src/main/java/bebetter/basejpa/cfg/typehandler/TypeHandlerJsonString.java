/*
package bebetter.basejpa.cfg.typehandler;


import bebetter.basejpa.model.math.JsonStr;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 继承自BaseTypeHandler<Object> 使用Object是为了让StringJsonUtil可以处理任意类型
public class TypeHandlerJsonString extends BaseTypeHandler<JsonStr> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonStr parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.value);
    }

    @Override
    public JsonStr getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return new JsonStr(rs.getString(columnName));
    }

    @Override
    public JsonStr getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return new JsonStr(rs.getString(columnIndex));
    }

    @Override
    public JsonStr getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return new JsonStr(cs.getString(columnIndex));
    }
}
*/
