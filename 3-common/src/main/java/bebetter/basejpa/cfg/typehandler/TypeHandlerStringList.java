/*
package bebetter.basejpa.cfg.typehandler;


import StringList;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 继承自BaseTypeHandler<Object> 使用Object是为了让StringJsonUtil可以处理任意类型
public class TypeHandlerStringList extends BaseTypeHandler<StringList> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, StringList parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDbValue());
    }

    @Override
    public StringList getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return StringList.of(rs.getString(columnName));
    }

    @Override
    public StringList getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return StringList.of(rs.getString(columnIndex));
    }

    @Override
    public StringList getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return StringList.of(cs.getString(columnIndex));
    }
}
*/
