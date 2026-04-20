package unlp.info.bd2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.services.*;

@Configuration
@ComponentScan(basePackages = "unlp.info.bd2")
public class AppConfig {

    @Bean
    @Primary
    public ToursService createService(
            UserRepository userRepository,
            RouteRepository routeRepository,
            ServiceRepository serviceRepository,
            PurchaseRepository purchaseRepository,
            SupplierRepository supplierRepository,
            StopRepository stopRepository,
            DriverUserRepository driverUserRepository,
            TourGuideUserRepository tourGuideUserRepository,
            ItemServiceRepository itemServiceRepository,
            ReviewRepository reviewRepository
    ) {
        return new ToursServiceImpl(
                userRepository,
                routeRepository,
                serviceRepository,
                purchaseRepository,
                supplierRepository,
                stopRepository,
                driverUserRepository,
                tourGuideUserRepository,
                itemServiceRepository,
                reviewRepository
        );
    }
}
