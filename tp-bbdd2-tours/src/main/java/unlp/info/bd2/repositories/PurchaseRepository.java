package unlp.info.bd2.repositories;


import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public class PurchaseRepository extends BaseRepositoryImpl<Purchase> {
    public PurchaseRepository() {
        super(Purchase.class);
    }

    public boolean hasPurchases(Long routeId) {
        String hql = "SELECT count(p) FROM Purchase p WHERE p.route.id = :routeId";
        Long count = getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("routeId", routeId)
                .uniqueResult();
        return count > 0;
    }

    public List<Purchase> getAllPurchasesOfUsername(String username){
        String hql = "FROM Purchase p WHERE p.user.username = :username";
        return getCurrentSession()
                .createQuery(hql, Purchase.class)
                .setParameter("username", username)
                .getResultList();
    }

    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        String hql = "SELECT count(p) FROM Purchase p WHERE p.date BETWEEN :start AND :end";

        return getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .uniqueResult();


    }

    public Optional<Purchase> findByCode(String code) {
        String hql = "FROM Purchase p WHERE p.code = :code";
        return getCurrentSession()
                .createQuery(hql, Purchase.class)
                .setParameter("code", code)
                .uniqueResultOptional();
    }

    public int getCountOfPurchasesInRouteAndDate(Route route, Date date) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(p) FROM Purchase p WHERE p.route = :route AND p.date = :date";
        return session.createQuery(hql, Long.class)
                .setParameter("route", route)
                .setParameter("date", date)
                .uniqueResult()
                .intValue();
    }


}