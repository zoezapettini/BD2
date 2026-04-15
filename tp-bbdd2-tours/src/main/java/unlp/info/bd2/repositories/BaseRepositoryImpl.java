package unlp.info.bd2.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.List;

public abstract class BaseRepositoryImpl <T> implements BaseRepository<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    private Class<T> entityClass;

    public BaseRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession(); //
    }

    public T save(T entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(getCurrentSession().get(entityClass, id));
    }

    public List<T> findAll() {
        return getCurrentSession().createQuery("from " + entityClass.getName(), entityClass).list();
    }

    public T delete(T entity) {
        getCurrentSession().delete(entity);
        return entity;
    }
}
