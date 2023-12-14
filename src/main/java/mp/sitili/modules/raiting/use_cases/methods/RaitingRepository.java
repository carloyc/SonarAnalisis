package mp.sitili.modules.raiting.use_cases.methods;

import mp.sitili.modules.raiting.entities.Raiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RaitingRepository extends JpaRepository<Raiting, String> {

    @Query(value = "SELECT COUNT(product_id)  AS \"Total\" \n" +
            "FROM (\n" +
            "    SELECT p.id AS product_id, AVG(r.raiting) AS avg_rating\n" +
            "    FROM raiting r\n" +
            "    INNER JOIN product p ON r.product_id = p.id\n" +
            "    WHERE p.user_id = :sellerEmail\n" +
            "    GROUP BY p.id\n" +
            "    HAVING AVG(r.raiting) > 4\n" +
            ") AS productos_con_rating_mayor_a_4", nativeQuery = true)
     Integer cal4(@Param("sellerEmail") String sellerEmail);

}
