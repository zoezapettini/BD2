package unlp.info.bd2.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue("DRIVER")
public class DriverUser extends User {

    @Column(name ="EXPEDIENT")
    private String expedient;

    @ManyToMany(mappedBy = "driverList", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Route> routes = new ArrayList<>();;

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routs) {
        this.routes = routs;
    }

    public void addRoutes(Route route){
        this.routes.add(route);
    }

    @Override
    public boolean canBeDeleted() {
        return this.routes.isEmpty();
    }

}
