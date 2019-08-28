package bebetter.basejpa.model.base.ext;

import bebetter.basejpa.util.SpringUtils;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import bebetter.statics.util.V;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 对象统一json入库
 */
public class UserType_Json<T> implements UserType, DynamicParameterizedType {
    @SuppressWarnings("WeakerAccess")
    public static final Map<String, Class> ClassMap = new HashMap<>(1024);

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};//, Types.JAVA_OBJECT
    }

    @Override
    public Class returnedClass() {
        return clazz;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtil.equal(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        String dbValue = rs.getString(names[0]);
        if (V.empty(dbValue)) {
            return null;
        }

        return SpringUtils.getBean(Gson.class).fromJson(dbValue, clazz);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        st.setString(index, null == value ? null : JSONUtil.toJsonStr(value));
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return ObjectUtil.clone(value);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return null == value ? null : JSONUtil.toJsonStr(value);
    }

    @Override
    public T assemble(Serializable cached, Object owner) throws HibernateException {
        if (V.empty(cached)) {
            return null;
        }
        return JSONUtil.toBean(cached.toString(), clazz);
    }

    @Override
    public T replace(Object original, Object target, Object owner) throws HibernateException {
        //noinspection unchecked
        return (T) original;
    }

    private Class<T> clazz;

    @Override
    public void setParameterValues(Properties parameters) {
        clazz = getClass((String) parameters.get(RETURNED_CLASS));
    }

    @SneakyThrows
    public Class<T> getClass(String fullName) {
        Class aClass = ClassMap.get(fullName);
        if (null == aClass) {
            aClass = Class.forName(fullName);
            ClassMap.put(fullName, aClass);
        }
        //noinspection unchecked
        return aClass;
    }
}
