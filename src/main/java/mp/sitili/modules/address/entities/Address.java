package mp.sitili.modules.address.entities;

import mp.sitili.modules.user.entities.User;
import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "country", nullable = false, length = 255)
    private String country;

    @Column(name = "state", nullable = false, length = 255)
    private String state;

    @Column(name = "city", nullable = false, length = 255)
    private String city;

    @Column(name = "postal_code")
    private Integer postalCode;

    @Column(name = "main_address", nullable = false, length = 255)
    private String mainAddress;

    @Column(name = "street_address1", length = 255)
    private String streetAddress1;

    @Column(name = "street_address2", length = 255)
    private String streetAddress2;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    public Address(Integer id, User user, String country, String state, String city, Integer postalCode, String mainAddress, String streetAddress1, String streetAddress2, String description) {
        this.id = id;
        this.user = user;
        this.country = country;
        this.state = state;
        this.city = city;
        this.postalCode = postalCode;
        this.mainAddress = mainAddress;
        this.streetAddress1 = streetAddress1;
        this.streetAddress2 = streetAddress2;
        this.description = description;
    }

    public Address() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
