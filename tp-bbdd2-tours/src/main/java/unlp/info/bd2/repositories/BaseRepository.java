package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    T delete(T entity);
}