package mp.sitili.modules.favorite.use_cases.methods;

import mp.sitili.modules.favorite.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FavoriteRepository extends JpaRepository<Favorite, String> {

    @Query(value = "SELECT \n" +
            "    f.id AS fav_id, p.name AS producto,\n" +
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
            "INNER JOIN favorities f ON f.product_id = p.id\n" +
            "WHERE f.user_id = :email && p.status = TRUE\n" +
            "GROUP BY p.id, p.name, p.price, p.features, c.name, f.id", nativeQuery = true)
     List<Object[]> favXusuario(@Param("email") String email);

    @Modifying
    @Query(value = "DELETE FROM favorities WHERE user_id = :user_id AND id = :fav_id", nativeQuery = true)
    void deleteFav(@Param("user_id") String user_id, @Param("fav_id") Integer fav_id);

    @Query(value = "SELECT * FROM favorities WHERE product_id = :product_id && user_id = :user_id", nativeQuery = true)
     Favorite validarExis(@Param("product_id") Integer product_id, @Param("user_id") String user_id);

    @Query(value = "SELECT * FROM favorities WHERE id = :id", nativeQuery = true)
     Favorite findById1(@Param("id") Integer id);

    @Modifying
    @Query(value = "DELETE FROM favorities WHERE product_id = :product_id", nativeQuery = true)
    void deleteTodos(@Param("product_id") Integer product_id);
}
