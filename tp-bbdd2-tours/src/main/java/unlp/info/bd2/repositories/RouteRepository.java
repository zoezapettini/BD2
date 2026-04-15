package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

import java.util.List;

@Repository
public class RouteRepository extends BaseRepositoryImpl<Route> {
    public RouteRepository() {
        super(Route.class);
    }

    public List<Route> getRoutesWithStop(Stop stop) {
        // Usamos un JOIN para navegar de la Ruta a sus Paradas
        String hql = "SELECT r FROM Route r JOIN r.stops s WHERE s = :stop";

        return getCurrentSession()
                .createQuery(hql, Route.class)
                .setParameter("stop", stop)
                .getResultList();
    }

    public Long getMaxStopOfRoutes() {
        String hql = "SELECT MAX(size(r.stops)) FROM Route r";

        Number result = getCurrentSession()
                .createQuery(hql, Number.class)
                .uniqueResult();

        return result != null ? result.longValue() : 0L;
    }

    public List<Route> getRoutsNotSell() {
        String hql = "FROM Route r WHERE r NOT IN (SELECT p.route FROM Purchase p)";
        return getCurrentSession()
                .createQuery(hql, Route.class)
                .getResultList();
    }

    public List<Route> getTop3RoutesWithMaxRating() {
        String hql = "SELECT p.route FROM Purchase p " +
                "WHERE p.review IS NOT NULL " +
                "GROUP BY p.route " +
                "ORDER BY AVG(p.review.rating) DESC";

        return getCurrentSession()
                .createQuery(hql, Route.class)
                .setMaxResults(3)
                .getResultList();
    }

    public List<Route> findByPriceLessThan(float price) {
        String hql = "FROM Route r WHERE r.price < :price";
        return getCurrentSession()
                .createQuery(hql, Route.class)
                .setParameter("price", price)
                .getResultList();
    }


}