package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Review;

@Repository
public class ReviewRepository extends BaseRepositoryImpl<Review> {
    public ReviewRepository() {
        super(Review.class);
    }
}