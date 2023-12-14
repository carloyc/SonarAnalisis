package mp.sitili.modules.order.use_cases.methods;

import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.dto.EntregasMesDTO;
import mp.sitili.modules.order.use_cases.dto.OrdersDTO;
import mp.sitili.modules.order.use_cases.dto.VentasMesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface OrderRepository extends JpaRepository<Order, String> {

    @Modifying
    @Query(value = "UPDATE orders SET repartidor = :repartidor, status = :status WHERE id = :id", nativeQuery = true)
    void updateDelivery(@Param("id") int id, @Param("repartidor") String repartidor, @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE orders SET status = :status WHERE id = :id", nativeQuery = true)
    void updateRecive(@Param("id") int id, @Param("status") String status);

    @Query(value = "SELECT COUNT(o.id)\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od  ON o.id = od.order_id\n" +
            "INNER JOIN product p ON od.product_id = p.id\n" +
            "WHERE p.user_id = :sellerEmail && o.status IN(\"Trafico\", \"Entrega\")", nativeQuery = true)
     Integer sellerEnvs(String sellerEmail);

    @Query(value = "SELECT SUM(od.price * od.quantity)\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id\n" +
            "INNER JOIN product p ON od.product_id = p.id\n" +
            "WHERE p.user_id = :sellerEmail AND o.status IN ('Trafico', 'Entrega');", nativeQuery = true)
     Double sellerSales(String sellerEmail);

    @Query(value = "SELECT id, user_id, product_id, quantity FROM shopping_car WHERE user_id = :userEmail", nativeQuery = true)
     List<Object[]>  buscarTodo(@Param("userEmail") String userEmail);

    @Query(value = "SELECT \n" +
            "    MONTH(o.date_order) AS mes,\n" +
            "    SUM(CASE WHEN YEAR(o.date_order) = YEAR(NOW()) THEN 1 ELSE 0 END) AS vendidos,\n" +
            "    SUM(CASE WHEN YEAR(o.date_order) = YEAR(NOW()) THEN od.price * od.quantity ELSE 0 END) AS ventas\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id \n" +
            "INNER JOIN product p ON od.product_id = p.id\n" +
            "WHERE \n" +
            "    (DAY(o.date_order) = DAY(NOW()) OR WEEK(o.date_order) = WEEK(NOW()) OR MONTH(o.date_order) = MONTH(NOW()) OR YEAR(o.date_order) = YEAR(NOW()))\n" +
            "    AND p.user_id = :sellerEmail AND o.status IN ('Trafico', 'Entrega') \n" +
            "GROUP BY mes", nativeQuery = true)
    List<VentasMesDTO> ventasAnioMonth(@Param("sellerEmail") String sellerEmail);

    @Query(value = "SELECT \n" +
            "    MONTH(o.date_order) AS mes,\n" +
            "    SUM(CASE WHEN YEAR(o.date_order) = YEAR(NOW()) AND o.status IN ('Entrega', 'Trafico') THEN 1 ELSE 0 END) AS entregados,\n" +
            "    SUM(CASE WHEN YEAR(o.date_order) = YEAR(NOW()) AND o.status IN ('Entrega', 'Trafico') THEN od.price * od.quantity ELSE 0 END) AS total\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id \n" +
            "INNER JOIN product p ON od.product_id = p.id\n" +
            "WHERE \n" +
            "    (DAY(o.date_order) = DAY(NOW()) OR WEEK(o.date_order) = WEEK(NOW()) OR MONTH(o.date_order) = MONTH(NOW()) OR YEAR(o.date_order) = YEAR(NOW()))\n" +
            "    AND p.user_id = :sellerEmail\n" +
            "GROUP BY mes", nativeQuery = true)
     List<EntregasMesDTO>  enviosAnioMonth(@Param("sellerEmail") String sellerEmail);

    @Query(value = "SELECT id, date_order, repartidor, status FROM orders WHERE user_id = :userEmail", nativeQuery = true)
     List<OrdersDTO> ordersPerUser(@Param("userEmail") String userEmail);
}
