package mp.sitili.modules.order.entities;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.user.entities.User;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "repartidor", nullable = true)
    private String repartidor;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "date_order", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dateOrder;

    public Order(Integer id, User user, String status, String repartidor, Address address, Timestamp dateOrder) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.repartidor = repartidor;
        this.address = address;
        this.dateOrder = dateOrder;
    }

    public Order() {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Timestamp getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Timestamp dateOrder) {
        this.dateOrder = dateOrder;
    }
}
