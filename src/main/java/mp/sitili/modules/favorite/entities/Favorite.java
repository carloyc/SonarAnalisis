package mp.sitili.modules.favorite.entities;

import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.user.entities.User;
import javax.persistence.*;

@Entity
@Table(name = "favorities")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Favorite(Integer id, User user, Product product) {
        this.id = id;
        this.user = user;
        this.product = product;
    }

    public Favorite() {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
