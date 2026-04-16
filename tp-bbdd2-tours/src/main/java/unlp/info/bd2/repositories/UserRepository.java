package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepositoryImpl<User> {
    public UserRepository() {
        super(User.class);
    }

    public List<User> getUserSpendingMoreThan(float amount) {
        String hql = "SELECT DISTINCT u FROM User u JOIN u.purchaseList p WHERE p.totalPrice >= :amount";

        return getCurrentSession()
                .createQuery(hql, User.class)
                .setParameter("amount", amount)
                .getResultList();
    }

    public Optional<User> findByUsername(String username) {
        String hql = "FROM User u WHERE u.username = :username";

        return getCurrentSession()
                .createQuery(hql, User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public boolean existsById(Long id) {
        String hql = "SELECT count(u) FROM User u WHERE u.id = :id";
        Long count = getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("id", id)
                .uniqueResult();

        return count != null && count > 0;
    }

    public void flush() {
        getCurrentSession().flush();
    }
}