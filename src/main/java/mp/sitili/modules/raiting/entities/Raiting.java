package mp.sitili.modules.raiting.entities;

import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.user.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "raiting")
public class Raiting {
    @Id
    private Integer id;

    @Column(name = "raiting")
    private Double raiting;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Raiting(Integer id, Double raiting, Product product, User user) {
        this.id = id;
        this.raiting = raiting;
        this.product = product;
        this.user = user;
    }

    public Raiting() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getRaiting() {
        return raiting;
    }

    public void setRaiting(Double raiting) {
        this.raiting = raiting;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
