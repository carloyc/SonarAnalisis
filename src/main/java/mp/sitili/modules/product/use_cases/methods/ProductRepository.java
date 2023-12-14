package mp.sitili.modules.product.use_cases.methods;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.user.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query(value = "SELECT p.id AS product_id, p.name AS producto,\n" +
            "    p.price AS precio, p.stock AS cantidad, p.features AS comentarios,\n" +
            "    AVG(r.raiting) AS calificacion, p.status AS estado, c.name AS categoria,\n" +
            "    u.email AS vendedor, du.first_name AS nombreVendedor, du.last_name AS apellidoVendedor,\n" +
            "    GROUP_CONCAT(DISTINCT ip.image_url) AS imagenes\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "LEFT JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "LEFT JOIN images_products ip ON p.id = ip.product_id\n" +
            "WHERE p.status = true \n" +
            "GROUP BY \n" +
            "    p.id, p.name, p.price, p.stock, p.features, p.status, c.name, u.email, du.first_name, du.last_name", nativeQuery = true)
     List<Object[]> findAllProducts();

    @Query(value = "SELECT \n" +
            "    p.id AS product_id, \n" +
            "    p.name AS producto,\n" +
            "    p.price AS precio, \n" +
            "    p.stock AS cantidad, \n" +
            "    p.features AS comentarios,\n" +
            "    AVG(r.raiting) AS calificacion, \n" +
            "    p.status AS estado, \n" +
            "    c.name AS categoria,\n" +
            "    u.email AS vendedor, \n" +
            "    du.first_name AS nombreVendedor, \n" +
            "    du.last_name AS apellidoVendedor,\n" +
            "    GROUP_CONCAT(DISTINCT ip.image_url) AS imagenes\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "LEFT JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "LEFT JOIN images_products ip ON p.id = ip.product_id\n" +
            "WHERE p.id = :product_id\n" +
            "GROUP BY p.id, p.name, p.price, p.stock, p.features, p.status, c.name, u.email, du.first_name, du.last_name", nativeQuery = true)
     List<Object[]> findProduct(@Param("product_id") Integer product_id);

    @Query(value = "SELECT p.id AS product_id, p.name AS producto,\n" +
            "    p.price AS precio, p.stock AS cantidad, p.features AS comentarios,\n" +
            "    AVG(r.raiting) AS calificacion, p.status AS estado, c.name AS categoria,\n" +
            "    u.email AS vendedor, du.first_name AS nombreVendedor, du.last_name AS apellidoVendedor,\n" +
            "    GROUP_CONCAT(DISTINCT ip.image_url) AS imagenes\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "LEFT JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "LEFT JOIN images_products ip ON p.id = ip.product_id\n" +
            "WHERE p.user_id = :email\n" +
            "GROUP BY \n" +
            "    p.id, p.name, p.price, p.stock, p.features, p.status, c.name, u.email, du.first_name, du.last_name", nativeQuery = true)
     List<Object[]> findAllxVend(@Param("email") String email);

    @Modifying
    @Query(value = "INSERT INTO product(name, price, stock, features, status, category_id, user_id) " +
            "VALUES(:name, :price, :stock, :features, true, :category, :user)", nativeQuery = true)
    void saveProduct(@Param("name") String name,
                      @Param("price") Double price,
                      @Param("stock") Integer stock,
                      @Param("features") String features,
                      @Param("category") Category category,
                      @Param("user") User user );

    @Query(value = "SELECT COUNT(*) FROM product WHERE user_id = :sellerEmail", nativeQuery = true)
     Integer countProSel(@Param("sellerEmail") String sellerEmail);

    @Query(value = "SELECT * FROM product WHERE id = :id", nativeQuery = true)
     Product findOnlyById(@Param("id") Integer id);

    @Modifying
    @Query(value = "UPDATE product SET status = :status WHERE user_id = :sellerEmail && id = :id", nativeQuery = true)
    void bajaLogica(@Param("id") Integer id,
                    @Param("status") boolean status,
                    @Param("sellerEmail") String sellerEmail);

}
