package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.DriverUser;

@Repository
public class DriverUserRepository extends BaseRepositoryImpl<DriverUser> {
    public DriverUserRepository() {
        super(DriverUser.class);
    }
}