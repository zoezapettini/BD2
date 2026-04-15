package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Stop;

import java.util.List;


@Repository
public class StopRepository extends BaseRepositoryImpl<Stop> {
    public StopRepository() {
        super(Stop.class);
    }

    public List<Stop> findByNameStartingWith(String name) {
        String hql = "FROM Stop s WHERE s.name LIKE :name";
        return getCurrentSession()
                .createQuery(hql, Stop.class)
                .setParameter("name", name + "%")
                .getResultList();
    }
}