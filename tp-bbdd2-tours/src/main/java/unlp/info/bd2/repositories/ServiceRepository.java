package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Service;

import java.util.Optional;


@Repository
public class ServiceRepository extends BaseRepositoryImpl<Service> {
    public ServiceRepository() {
        super(Service.class);
    }

    public Service getMostDemandedService() {
        String hql = "SELECT i.service FROM ItemService i " +
                "GROUP BY i.service " +
                "ORDER BY SUM(i.quantity) DESC";

        return getCurrentSession()
                .createQuery(hql, Service.class)
                .setMaxResults(1)
                .uniqueResult();
    }

    public Optional<Service> findByNameAndSupplierId(String name, Long supplierId) {
        String hql = "FROM Service s WHERE s.name = :name AND s.supplier.id = :supplierId";
        return getCurrentSession()
                .createQuery(hql, Service.class)
                .setParameter("name", name)
                .setParameter("supplierId", supplierId)
                .uniqueResultOptional();
    }
}