package unlp.info.bd2.repositories;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;

import java.util.List;
import java.util.Optional;


@Repository
public class SupplierRepository extends BaseRepositoryImpl<Supplier> {
    public SupplierRepository() {
        super(Supplier.class);
    }

    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        String hql = "SELECT s FROM ItemService i " +
                "JOIN i.service serv " +
                "JOIN serv.supplier s " +
                "GROUP BY s " +
                "ORDER BY COUNT(i) DESC";

        return getCurrentSession()
                .createQuery(hql, Supplier.class)
                .setMaxResults(n)
                .getResultList();
    }

    public Optional<Supplier> findByAuthorizationNumber(String authNumber) {
        String hql = "FROM Supplier s WHERE s.authorizationNumber = :authNumber";
        return getCurrentSession()
                .createQuery(hql, Supplier.class)
                .setParameter("authNumber", authNumber)
                .uniqueResultOptional();
    }
}