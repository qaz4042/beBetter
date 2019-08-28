/*
package bebetter.basejpa.cfg.typehandler;


import IEnumUploadModule;
import UploadVo;
import UploadFile;
import FileUtil2;
import V;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 继承自BaseTypeHandler<Object> 使用Object是为了让StringJsonUtil可以处理任意类型
public class TypeHandlerUploadFile extends BaseTypeHandler<UploadFile> {

    @Override
    @SneakyThrows
    public void setNonNullParameter(PreparedStatement ps, int i, UploadFile uploadFile, JdbcType jdbcType) throws SQLException {
        //相对路径
        String url = uploadFile.getUrl();
        if (V.noEmpty(uploadFile.getBase64())) {
            url = FileUtil2.uploadFile(new UploadVo(uploadFile.getBase64(), uploadFile.getOriName()), IEnumUploadModule.codeObjMap.get(uploadFile.getModule()));
        }
        ps.setString(i, url);
    }

    @Override
    public UploadFile getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return UploadFile.of(rs.getString(columnName));
    }

    @Override
    public UploadFile getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return UploadFile.of(rs.getString(columnIndex));
    }

    @Override
    public UploadFile getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return UploadFile.of(cs.getString(columnIndex));
    }
}
*/
