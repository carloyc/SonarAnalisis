package mp.sitili.modules.data_user.use_cases.methods;

import mp.sitili.modules.data_user.entities.DataUser;
import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;
import mp.sitili.modules.data_user.use_cases.dto.UsuariosxMesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface DataUserRepository extends JpaRepository<DataUser, Integer> {

    @Modifying
    @Query(value = "INSERT INTO data_users(user_id, first_name, last_name) VALUES(:email, :first_name, :last_name)", nativeQuery = true)
     void asociarUserData(@Param("email") String email, @Param("first_name") String first_name, @Param("last_name") String last_name);

    @Query(value = "SELECT du.id, du.company, du.first_name, du.last_name, du.phone, du.register_date, du.user_id, u.status, ur.role_id FROM data_users du INNER JOIN users u ON du.user_id = u.email INNER JOIN user_role ur ON u.email = ur.user_id", nativeQuery = true)
     List<DataUserDTO> findAllDataUsers();

    @Query(value = "SELECT du.id, du.company, du.first_name, du.last_name, du.phone, du.register_date, du.user_id, u.status, ur.role_id FROM data_users du INNER JOIN users u ON du.user_id = u.email INNER JOIN user_role ur ON u.email = ur.user_id && u.email = :email", nativeQuery = true)
     DataUserDTO findAllDataUser(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE data_users SET company = :company, phone = :phone WHERE user_id = :userEmail", nativeQuery = true)
    void setCompany(@Param("userEmail") String userEmail, @Param("company") String company, @Param("phone") String phone);

    @Modifying
    @Query(value = "UPDATE data_users SET company = :company, first_name = :first_name, last_name = :last_name, phone = :phone WHERE user_id = :user_id && id = :id", nativeQuery = true)
    void update(@Param("company") String company,
                   @Param("first_name") String first_name,
                   @Param("last_name") String last_name,
                   @Param("phone") String phone,
                   @Param("id") Integer id,
                   @Param("user_id") String user_id);

    @Query(value = "SELECT \n" +
            "    COUNT(data_users.register_date) AS \"Cantidad\",\n" +
            "    meses.nombre_mes AS \"Mes\"\n" +
            "FROM \n" +
            "    (\n" +
            "        SELECT 1 AS mes, 'Enero' AS nombre_mes\n" +
            "        UNION SELECT 2 AS mes, 'Febrero' AS nombre_mes\n" +
            "        UNION SELECT 3 AS mes, 'Marzo' AS nombre_mes\n" +
            "        UNION SELECT 4 AS mes, 'Abril' AS nombre_mes\n" +
            "        UNION SELECT 5 AS mes, 'Mayo' AS nombre_mes\n" +
            "        UNION SELECT 6 AS mes, 'Junio' AS nombre_mes\n" +
            "        UNION SELECT 7 AS mes, 'Julio' AS nombre_mes\n" +
            "        UNION SELECT 8 AS mes, 'Agosto' AS nombre_mes\n" +
            "        UNION SELECT 9 AS mes, 'Septiembre' AS nombre_mes\n" +
            "        UNION SELECT 10 AS mes, 'Octubre' AS nombre_mes\n" +
            "        UNION SELECT 11 AS mes, 'Noviembre' AS nombre_mes\n" +
            "        UNION SELECT 12 AS mes, 'Diciembre' AS nombre_mes\n" +
            "    ) AS meses\n" +
            "LEFT JOIN \n" +
            "    data_users ON MONTH(data_users.register_date) = meses.mes\n" +
            "GROUP BY \n" +
            "    meses.mes, meses.nombre_mes\n" +
            "ORDER BY \n" +
            "    meses.mes;", nativeQuery = true)
     List<UsuariosxMesDTO> usuXmes();

}
