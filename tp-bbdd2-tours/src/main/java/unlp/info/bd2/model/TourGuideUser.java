package unlp.info.bd2.model;


import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@DiscriminatorValue("GUIDE")
public class TourGuideUser extends User {

    @Column(name ="EDUCATION")
    private String education;

    @ManyToMany(mappedBy = "tourGuideList", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Route> routes = new ArrayList<>();;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
