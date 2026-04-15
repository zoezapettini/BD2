package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.ItemService;

@Repository
public class ItemServiceRepository extends BaseRepositoryImpl<ItemService> {
    public ItemServiceRepository() {
        super(ItemService.class);
    }
}