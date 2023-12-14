package mp.sitili.modules.acept_seller.use_cases.methods;

import mp.sitili.modules.acept_seller.entities.AceptSeller;
import mp.sitili.modules.acept_seller.use_cases.dto.AceptSellerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AceptSellerRepository extends JpaRepository<AceptSeller, String> {

    @Query(value = "SELECT ur.role_id , u.email, u.status FROM users u INNER JOIN user_role ur ON ur.user_id = u.email WHERE ur.role_id LIKE \"%Seller%\" && u.status = false;", nativeQuery = true)
    List<AceptSellerDTO> findAllSellersNa();

}
