package mp.sitili.modules.acept_seller.entities;

import mp.sitili.modules.user.entities.User;
import javax.persistence.*;

@Entity
@Table(name = "acept_sellers")
public class AceptSeller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "email")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "email")
    private User admin;

    public AceptSeller(Integer id, User seller, User admin) {
        this.id = id;
        this.seller = seller;
        this.admin = admin;
    }

    public AceptSeller(User seller, User admin) {
        this.seller = seller;
        this.admin = admin;
    }

    public AceptSeller() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
