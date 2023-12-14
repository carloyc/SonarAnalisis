package mp.sitili.modules.payment_cc.use_cases.methods;

import mp.sitili.modules.payment_cc.entities.PaymentCC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface PaymentCCRepository extends JpaRepository<PaymentCC, String> {

    @Query(value = "SELECT * FROM payments_cc WHERE user_id = :userEmail", nativeQuery = true)
    List<PaymentCC> pagoXusuario(@Param("userEmail") String userEmail);

    @Query(value = "SELECT * \n" +
            "FROM payments_cc \n" +
            "WHERE user_id = :userEmail \n" +
            "ORDER BY id DESC\n" +
            "LIMIT 1;", nativeQuery = true)
    PaymentCC tarjetaXusuario(@Param("userEmail") String userEmail);

    @Query(value = "SELECT * FROM payments_cc WHERE id = :id", nativeQuery = true)
    PaymentCC findById(@Param("id") Integer id);

}
