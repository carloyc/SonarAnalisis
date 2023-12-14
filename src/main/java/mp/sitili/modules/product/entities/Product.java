package mp.sitili.modules.product.entities;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.raiting.entities.Raiting;
import mp.sitili.modules.user.entities.User;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "price")
    private Double price;

    @Column(name = "features", columnDefinition = "TEXT")
    private String features;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product")
    private List<Raiting> raiting;

    @Column(name = "register_product", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp registerProduct;

    @Column(name = "status", nullable = true)
    private Boolean status;

    public Product(Integer id, String name, Integer stock, Double price, String features, Category category, User user, List<Raiting> raiting, Timestamp registerProduct, Boolean status) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.features = features;
        this.category = category;
        this.user = user;
        this.raiting = raiting;
        this.registerProduct = registerProduct;
        this.status = status;
    }

    public Product(Integer id, String name, Integer stock, Double price, String features, Category category, User user, Timestamp registerProduct, Boolean status) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.features = features;
        this.category = category;
        this.user = user;
        this.registerProduct = registerProduct;
        this.status = status;
    }

    public Product(String name, Integer stock, Double price, String features, Category category, User user, List<Raiting> raiting) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.features = features;
        this.category = category;
        this.user = user;
        this.raiting = raiting;
    }

    public Product(String name, Double price, Integer stock, String features, Category category, User user) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.features = features;
        this.category = category;
        this.user = user;
    }

    public Product(String name, Integer stock, Double price, String features, Category category, User user, Timestamp registerProduct, Boolean status) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.features = features;
        this.category = category;
        this.user = user;
        this.registerProduct = registerProduct;
        this.status = status;
    }

    public Product() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Raiting> getRaiting() {
        return raiting;
    }

    public void setRaiting(List<Raiting> raiting) {
        this.raiting = raiting;
    }

    public Timestamp getRegisterProduct() {
        return registerProduct;
    }

    public void setRegisterProduct(Timestamp registerProduct) {
        this.registerProduct = registerProduct;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
