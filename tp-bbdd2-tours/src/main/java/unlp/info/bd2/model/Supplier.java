package unlp.info.bd2.model;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;


@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BUSINESS_NAME", unique = true)
    private String businessName;

    @Column(name = "AUTHORIZATION_NUMBER")
    private String authorizationNumber;

    //preguntar
    @OneToMany(mappedBy = "supplier",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<Service> services = new ArrayList<>();;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
