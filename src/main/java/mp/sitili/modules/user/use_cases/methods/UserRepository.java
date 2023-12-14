package mp.sitili.modules.user.use_cases.methods;

import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;
import mp.sitili.modules.user.use_cases.dto.ValidSellerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    @Modifying
    @Query(value = "UPDATE users SET status = :status WHERE email = :email", nativeQuery = true)
    void bajaLogica(@Param("email") String email, @Param("status") boolean status);

    @Query(value = "SELECT * FROM user_role WHERE role_id LIKE %Admin%", nativeQuery = true)
    List<User> findAllAdmins();

    @Query(value = "SELECT * FROM user_role WHERE role_id LIKE %Seller%", nativeQuery = true)
    List<User> findAllSellers();

    @Query(value = "SELECT * FROM user_role WHERE role_id LIKE %User%", nativeQuery = true)
    List<User> findAllUsers();

    @Query(value = "SELECT c.company AS \"Tienda\", CONCAT(c.first_name, ' ', c.last_name) AS \"Vendedor\", u.email AS \"Email\"\n" +
            "FROM data_users c\n" +
            "INNER JOIN users u ON c.user_id = u.email\n" +
            "INNER JOIN user_role ur ON u.email = ur.user_id\n" +
            "INNER JOIN role r ON ur.role_id = r.role_name\n" +
            "WHERE u.status = true AND r.role_description LIKE '%Seller%';" , nativeQuery = true)
    List<SelectVendedorDTO> findSellers();

    @Query(value = "SELECT id, company, first_name, last_name, phone, user_id FROM data_users WHERE user_id = :user_id", nativeQuery = true)
    ValidSellerDTO validateCompany(@Param("user_id") String user_id);

}