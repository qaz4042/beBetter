package bebetter.jpa.base;

import bebetter.jpa.datetimes.BaseDateTimes;
import bebetter.jpa.model.Cond;
import bebetter.jpa.util.OtherUtil;
import bebetter.statics.util.V;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 最常用方法, page(..) list(..) one(..)  save(..新增和修改) saveAll(..新增和修改) delete(..) deleteById(...)
 *
 * @param <T> 实体类
 */
@NoRepositoryBean
public interface BaseDao<T> extends JpaSpecificationExecutor<T>, Serializable, CrudRepository<T, Serializable>, JpaRepository<T, Serializable>, PagingAndSortingRepository<T, Serializable> {
    Logger logger = LoggerFactory.getLogger(BaseDao.class);

    default PageImpl<T> page(Pageable pageable, Cond<T> cond) {
        return (PageImpl<T>) this.findAll(condToSpecification(cond), pageable);
    }


    @Override
    <S extends T> S save(S entity);

    @Override
    <S extends T> List<S> saveAll(Iterable<S> entities);

    default <Times extends BaseDateTimes> PageImpl<T> page(Pageable pageable, Cond<T> cond, Times times) {
        OtherUtil.setTimes(cond, times);
        return this.page(pageable, cond);
    }

    default List<T> list() {
        return this.list((T) null);
    }

    default List<T> list(T t) {
        return this.list(new Cond<>(t), null);
    }

    default List<T> list(Cond<T> c) {
        return this.list(c, null);
    }

    default <Times extends BaseDateTimes> List<T> list(Cond<T> cond, Times times) {
        OtherUtil.setTimes(cond, times);
        return this.findAll(condToSpecification(cond));
    }

    default T one(T t) {
        return this.one(new Cond<>(t));
    }

    default T one(Cond<T> cond) {
        List<T> list = list(cond, null);
        switch (list.size()) {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                logger.error("{}.getOne查询到{}个对象,返回了第一个.", this.getClass().getSimpleName(), list.size());
                return list.get(0);
        }
    }

    @SuppressWarnings("unused")
    default Cond<T> cond() {
        return new Cond<>();
    }

    default Specification<T> condToSpecification(Cond<T> cond) {
        return new Specification<T>() {
            /**
             * 基本对象的构建
             * 1：通过EntityManager的getCriteriaBuilder或EntityManagerFactory的getCriteriaBuilder方法可以得到CriteriaBuilder对象
             * 2：通过调用CriteriaBuilder的createQuery或createTupleQuery方法可以获得CriteriaQuery的实例
             * 3：通过调用CriteriaQuery的from方法可以获得Root实例
             * 过滤条件
             * 1：过滤条件会被应用到SQL语句的FROM子句中。在criteria 查询中，查询条件通过Predicate或Expression实例应用到CriteriaQuery对象上。
             * 2：这些条件使用 CriteriaQuery .where 方法应用到CriteriaQuery 对象上
             * 3：CriteriaBuilder也作为Predicate实例的工厂，通过调用CriteriaBuilder 的条件方法（ equal，notEqual， gt， ge，lt， le，between，like等）创建Predicate对象。
             * 4：复合的Predicate 语句可以使用CriteriaBuilder的and, or andnot 方法构建
             */
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                query.select() todo
                return cond.buildPredicate(root, query, cb);
            }
        };
    }

    @Transactional
    default void deleteInBatch(List<? extends Serializable> ids) {
        //等用得比较多,再做批量性能优化
        ids.forEach(this::deleteById);
    }
}
