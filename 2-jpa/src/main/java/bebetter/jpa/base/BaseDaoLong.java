package bebetter.jpa.base;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * 最常用方法, page(..) list(..) one(..)  save(..新增和修改) saveAll(..新增和修改) delete(..) deleteById(...)
 *
 * @param <T>  实体类
 * @param <ID> 实体类id 的类型
 */
@NoRepositoryBean
public interface BaseDaoLong<T> extends BaseDao<T> {
}
