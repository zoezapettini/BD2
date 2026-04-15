package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.TourGuideUser;

import java.util.List;


@Repository
public class TourGuideUserRepository extends BaseRepositoryImpl<TourGuideUser> {
    public TourGuideUserRepository() {
        super(TourGuideUser.class);
    }

    public List<TourGuideUser> getTourGuidesWithRating1() {
        String hql = "SELECT DISTINCT g FROM TourGuideUser g " +
                "JOIN g.routes r, Purchase p " +
                "WHERE p.route = r " +
                "AND p.review.rating = 1";

        return getCurrentSession()
                .createQuery(hql, TourGuideUser.class)
                .getResultList();
    }
}