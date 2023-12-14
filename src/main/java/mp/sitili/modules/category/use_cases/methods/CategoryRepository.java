package mp.sitili.modules.category.use_cases.methods;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.dto.ProductosxCategoriaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "SELECT * FROM categories WHERE status = true", nativeQuery = true)
     List<Category> findAllStatus();

    @Query(value = "SELECT * FROM categories WHERE status = true && name LIKE %:name%", nativeQuery = true)
     List<Category> categoriasNombre(@Param("name") String name);

    @Query(value = "SELECT id FROM categories WHERE id = :id", nativeQuery = true)
     Integer getCategoryById(@Param("id") int id);

    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
     Category getCatById(@Param("id") int id);

    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
     Category getNameCategoryById(@Param("id") int id);

    @Query(value = "SELECT status FROM categories WHERE id = :id", nativeQuery = true)
     boolean getStatusCategory(@Param("id") int id);

    @Modifying
    @Query(value = "INSERT INTO categories (name, status) VALUES (:name, true)", nativeQuery = true)
    void createCategory(@Param("name") String name);

    @Modifying
    @Query(value = "UPDATE categories SET status = :status WHERE id = :id", nativeQuery = true)
    void deleteCategory(@Param("id") int id, @Param("status") boolean status);

    @Modifying
    @Query(value = "UPDATE categories SET name = :name WHERE id = :id", nativeQuery = true)
    void updateCategory(@Param("id") int id, @Param("name") String name);

    @Query(value = "SELECT c.name AS \"Categoria\", COUNT(p.id) AS \"Cantidad\"\n" +
            "FROM categories c \n" +
            "LEFT JOIN product p ON p.category_id = c.id\n" +
            "GROUP BY c.id, c.name", nativeQuery = true)
     List<ProductosxCategoriaDTO> proXcat();

    @Query(value = "SELECT c.name AS \"Categoria\", COUNT(p.id) AS \"Cantidad\"\n" +
            "FROM categories c \n" +
            "LEFT JOIN product p ON p.category_id = c.id\n" +
            "WHERE p.user_id = :sellerEmail\n" +
            "GROUP BY c.id, c.name", nativeQuery = true)
     List<ProductosxCategoriaDTO> proXcatSell(@Param("sellerEmail") String sellerEmail);

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
            "WHERE p.category_id = :id\n" +
            "GROUP BY p.id, p.name, p.price, p.stock, p.features, p.status, c.name, u.email, du.first_name, du.last_name", nativeQuery = true)
     List<Object[]> findAllProducts(@Param("id") Integer id);

}
