package mp.sitili.modules.image_product.entities;

import mp.sitili.modules.product.entities.Product;
import javax.persistence.*;

@Entity
@Table(name = "images_products")
public class ImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ImageProduct(Integer id, String imageUrl, Product product) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.product = product;
    }

    public ImageProduct() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
