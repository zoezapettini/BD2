package unlp.info.bd2.services;


import org.springframework.transaction.annotation.Transactional;
import unlp.info.bd2.model.*;
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.utils.ToursException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class ToursServiceImpl implements ToursService {

    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final ServiceRepository serviceRepository;
    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final StopRepository stopRepository;
    private final DriverUserRepository driverUserRepository;
    private final TourGuideUserRepository tourGuideUserRepository;
    private final ItemServiceRepository itemServiceRepository;
    private final ReviewRepository reviewRepository;

    public ToursServiceImpl(
            UserRepository userRepository,
            RouteRepository routeRepository,
            ServiceRepository serviceRepository,
            PurchaseRepository purchaseRepository,
            SupplierRepository supplierRepository,
            StopRepository stopRepository,
            DriverUserRepository driverUserRepository,
            TourGuideUserRepository tourGuideUserRepository,
            ItemServiceRepository itemServiceRepository,
            ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.routeRepository = routeRepository;
        this.serviceRepository = serviceRepository;
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.stopRepository = stopRepository;
        this.driverUserRepository = driverUserRepository;
        this.tourGuideUserRepository = tourGuideUserRepository;
        this.itemServiceRepository = itemServiceRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ToursException("El nombre de usuario ya existe");
        }
        User user = new User();
        user.setUsername(username);
        user.setBirthdate(birthdate);
        user.setPassword(password);
        user.setName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        Route route = new Route();
        route.setName(name);
        route.setPrice(price);
        route.setTotalKm(totalKm);
        route.setMaxNumberUsers(maxNumberOfUsers);
        route.setStops(stops);
        return routeRepository.save(route);
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException {
        Optional<Service> existingService = serviceRepository.findByNameAndSupplierId(name, supplier.getId());
        if (existingService.isPresent()) {
            return existingService.get();
        }
        Service service = new Service(name, price, description, supplier);
        supplier.addService(service);
        return this.serviceRepository.save(service);
    }

    @Override
    @Transactional
    public Supplier createSupplier (String businessName, String authorizationNumber) throws ToursException {
        if (supplierRepository.findByAuthorizationNumber(authorizationNumber).isPresent()) {
            throw new ToursException("Constraint Violation");
        }
        Supplier supplier = new Supplier();
        supplier.setBusinessName(businessName);
        supplier.setAuthorizationNumber(authorizationNumber);
        return supplierRepository.save(supplier);
    }


    @Override
    @Transactional
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ToursException("No existe el servicio con el ID proporcionado"));
        service.setPrice(newPrice);
        return serviceRepository.save(service);
    }


    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        return this.createPurchase(code, new Date(), route, user);
    }
    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        try{
            if(this.purchaseRepository.getCountOfPurchasesInRouteAndDate(route,date) < route.getMaxNumberUsers()){
                Purchase purchase = new Purchase(code, date, route, user);
                user.addPurchase(purchase);
                return this.purchaseRepository.save(purchase);
            } else{
                throw new ToursException("No hay lugares disponibles");
            }
        } catch (Exception e){
            throw new ToursException("No puede realizarse la compra");
        }
    }

    @Override
    @Transactional
    public Stop createStop (String name, String description){
        Stop stop = new Stop();
        stop.setName(name);
        stop.setDescription(description);
        return stopRepository.save(stop);
    }


    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        if (purchase.getReview() != null) {
            throw new ToursException("La compra ya tiene una reseña asociada.");
        }
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setPurchase(purchase);
        purchase.setReview(review);
        reviewRepository.save(review);
        return review;
    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException {
        TourGuideUser guide = new TourGuideUser();
        this.mapCommonUserData(guide, username, password, fullName, email, birthdate, phoneNumber);
        guide.setActive(true);
        guide.setEducation(education);

        return tourGuideUserRepository.save(guide);
    }

    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException {
        DriverUser driver = new DriverUser();
        this.mapCommonUserData(driver, username, password, fullName, email, birthdate, phoneNumber);
        driver.setActive(true);
        driver.setExpedient(expedient);

        return driverUserRepository.save(driver);
    }

    @Override
    @Transactional
    public void deleteRoute(Route route) throws ToursException {
        if (purchaseRepository.hasPurchases(route.getId())) {
            throw new ToursException("No puede eliminarse una ruta con compras asociadas");
        }
        routeRepository.delete(route);
    }
    private void mapCommonUserData(User user, String username, String password, String fullName, String email, Date birthdate, String phoneNumber) {
        user.setUsername(username);
        user.setPassword(password);
        user.setName(fullName);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setPhoneNumber(phoneNumber);
        user.setPurchaseList(new ArrayList<>());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException{
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername (String username) throws ToursException{
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public User updateUser(User user) throws ToursException {
        if (!userRepository.existsById(user.getId())) {
            throw new ToursException("El usuario a eliminar no existe.");
        }
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException {
        User persistentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ToursException("El usuario a eliminar no existe."));
        if (!persistentUser.isActive()) {
            throw new ToursException("El usuario se encuentra desactivado");
        }

        if(!user.canBeDeleted()){
            throw  new ToursException("El usuario no puede ser desactivado");
        }
        if (persistentUser.getPurchaseList() == null || persistentUser.getPurchaseList().isEmpty()) {
            userRepository.delete(persistentUser);
        } else {
            persistentUser.setActive(false);
            userRepository.save(persistentUser);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return stopRepository.findByNameStartingWith(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return routeRepository.findByPriceLessThan(price);
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        DriverUser driver = (DriverUser) userRepository.findByUsername(username)
                .filter(u -> u instanceof DriverUser)
                .orElseThrow(() -> new ToursException("No existe el chofer con username: " + username));

        Route route = routeRepository.findById(idRoute)
                .orElseThrow(() -> new ToursException("No existe la ruta con ID: " + idRoute));

        if (!route.getDriverList().contains(driver)) {
            route.getDriverList().add(driver);
        }

        driver.addRoutes(route);
        routeRepository.save(route);
    }

    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        TourGuideUser guide = (TourGuideUser) userRepository.findByUsername(username)
                .filter(u -> u instanceof TourGuideUser)
                .orElseThrow(() -> new ToursException(String.format("No existe el guía con username: %s", username)));

        Route route = routeRepository.findById(idRoute)
                .orElseThrow(() -> new ToursException(String.format("No existe la ruta con ID: %d", idRoute)));

        if (route.getTourGuideList() == null) {
            route.setTourGuideList(new ArrayList<>());
        }

        if (!route.getTourGuideList().contains(guide)) {
            route.getTourGuideList().add(guide);
        }
        guide.addRoute(route);

        routeRepository.save(route);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService();
        item.setQuantity(quantity);
        item.setPurchase(purchase);
        item.setService(service);
        purchase.addItemService(item);
        service.addItemService(item);
        return this.itemServiceRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return purchaseRepository.findByCode(code);
    }

    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchaseRepository.delete(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return purchaseRepository.getAllPurchasesOfUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float amount) {
        return userRepository.getUserSpendingMoreThan(amount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return supplierRepository.getTopNSuppliersInPurchases(n);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return purchaseRepository.getCountOfPurchasesBetweenDates(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        return routeRepository.getRoutesWithStop(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        return routeRepository.getMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell() {
        return routeRepository.getRoutsNotSell();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxRating() {
        return routeRepository.getTop3RoutesWithMaxRating();
    }

    @Override
    @Transactional(readOnly = true)
    public unlp.info.bd2.model.Service getMostDemandedService() {
        return serviceRepository.getMostDemandedService();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return tourGuideUserRepository.getTourGuidesWithRating1();
    }


}