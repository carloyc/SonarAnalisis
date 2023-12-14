package mp.sitili.modules.shopping_car.use_cases.methods;

import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ShoppingCarRepository extends JpaRepository<ShoppingCar, String> {

    @Query(value = "SELECT \n" +
            "    sc.id AS car_id, p.name AS producto,\n" +
            "    p.price AS precio, \n" +
            "    p.features AS comentarios,\n" +
            "    AVG(r.raiting) AS calificacion,\n" +
            "    c.name AS categoria,\n" +
            "    GROUP_CONCAT(DISTINCT ip.image_url) AS imagenes\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "LEFT JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "LEFT JOIN images_products ip ON p.id = ip.product_id\n" +
            "INNER JOIN shopping_car sc ON sc.product_id = p.id\n" +
            "WHERE sc.user_id = :email && p.status = TRUE\n" +
            "GROUP BY p.id, p.name, p.price, p.features, c.name, sc.id", nativeQuery = true)
     List<Object[]> carXusuario(@Param("email") String email);

    @Query(value = "SELECT \n" +
            "    sc.id AS car_id, sc.quantity AS cantidad , p.name AS producto,\n" +
            "    p.price AS precio, \n" +
            "    p.features AS comentarios,\n" +
            "    AVG(r.raiting) AS calificacion,\n" +
            "    c.name AS categoria,\n" +
            "    GROUP_CONCAT(DISTINCT ip.image_url) AS imagenes\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "LEFT JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "LEFT JOIN images_products ip ON p.id = ip.product_id\n" +
            "INNER JOIN shopping_car sc ON sc.product_id = p.id\n" +
            "WHERE sc.user_id = :email && p.status = TRUE\n" +
            "GROUP BY p.id, p.name, p.price, p.features, c.name, sc.id", nativeQuery = true)
     List<Object[]> carXusuariob(@Param("email") String email);

    @Modifying
    @Query(value = "DELETE FROM shopping_car WHERE user_id = :user_id AND id = :product_id", nativeQuery = true)
    void deleteCar(@Param("user_id") String user_id, @Param("product_id") Integer product_id);

    @Query(value = "SELECT * FROM shopping_car WHERE product_id = :product_id && user_id = :user_id", nativeQuery = true)
     ShoppingCar validarExis(@Param("product_id") Integer product_id, @Param("user_id") String user_id);

    @Query(value = "SELECT * FROM shopping_car WHERE id = :id", nativeQuery = true)
     ShoppingCar findById1(@Param("id") Integer id);

    @Modifying
    @Query(value = "UPDATE shopping_car SET quantity = :quantity WHERE id = :car_id", nativeQuery = true)
    void updateCar(@Param("car_id") Integer car_id, @Param("quantity") Integer quantity);

    @Modifying
    @Query(value = "DELETE FROM shopping_car WHERE user_id = :user_id", nativeQuery = true)
    void deleteAllCar(@Param("user_id") String user_id);

    @Modifying
    @Query(value = "DELETE FROM shopping_car WHERE product_id = :product_id", nativeQuery = true)
    void deleteTodos(@Param("product_id") Integer product_id);

}
