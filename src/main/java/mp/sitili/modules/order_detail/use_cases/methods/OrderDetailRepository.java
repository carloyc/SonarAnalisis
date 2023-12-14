package mp.sitili.modules.order_detail.use_cases.methods;

import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.dto.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    @Query(value = "SELECT COUNT(o.id) AS Vendidos,SUM(od.price * od.quantity) AS Total\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id\n" +
            "WHERE o.status IN ('Entrega', 'Trafico')", nativeQuery = true)
     VentasDTO totalVentas();

    @Query(value = "SELECT \n" +
            "    DAY(o.date_order) AS Day,\n" +
            "    SUM(CASE WHEN DAY(o.date_order) = DAY(NOW()) THEN 1 ELSE 0 END) AS Vendidos_Dia,\n" +
            "    SUM(CASE WHEN DAY(o.date_order) = DAY(NOW()) THEN od.price * od.quantity ELSE 0 END) AS Total_Dia,\n" +
            "    WEEK(o.date_order) AS Week,\n" +
            "    SUM(CASE WHEN WEEK(o.date_order) = WEEK(NOW()) THEN 1 ELSE 0 END) AS Vendidos_Semana,\n" +
            "    SUM(CASE WHEN WEEK(o.date_order) = WEEK(NOW()) THEN od.price * od.quantity ELSE 0 END) AS Total_Semana,\n" +
            "    MONTH(o.date_order) AS Month,\n" +
            "    SUM(CASE WHEN MONTH(o.date_order) = MONTH(NOW()) THEN 1 ELSE 0 END) AS Vendidos_Mes,\n" +
            "    SUM(CASE WHEN MONTH(o.date_order) = MONTH(NOW()) THEN od.price * od.quantity ELSE 0 END) AS Total_Mes,\n" +
            "    YEAR(o.date_order) AS Anio,\n" +
            "    SUM(CASE WHEN YEAR(o.date_order) = YEAR(NOW()) THEN 1 ELSE 0 END) AS Vendidos_Anio,\n" +
            "    SUM(CASE WHEN YEAR(o.date_order) = YEAR(NOW()) THEN od.price * od.quantity ELSE 0 END) AS Total_Anio\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id \n" +
            "WHERE \n" +
            "    (DAY(o.date_order) = DAY(NOW()) OR WEEK(o.date_order) = WEEK(NOW()) OR MONTH(o.date_order) = MONTH(NOW()) OR YEAR(o.date_order) = YEAR(NOW()))\n" +
            "GROUP BY Day, Week, Month, Anio;", nativeQuery = true)
     List<VentasAnualesDTO> totalVentasAnuales();

    @Query(value = "SELECT \n" +
            "    COALESCE(DAY(o.date_order), 0) AS Day,\n" +
            "    COALESCE(SUM(CASE WHEN DAY(o.date_order) = DAY(NOW()) THEN 1 ELSE 0 END), 0) AS Pedidos_Dia,\n" +
            "    COALESCE(WEEK(o.date_order), 0) AS Week,\n" +
            "    COALESCE(SUM(CASE WHEN WEEK(o.date_order) = WEEK(NOW()) THEN 1 ELSE 0 END), 0) AS Pedidos_Semana,\n" +
            "    COALESCE(MONTH(o.date_order), 0) AS Month,\n" +
            "    COALESCE(SUM(CASE WHEN MONTH(o.date_order) = MONTH(NOW()) THEN 1 ELSE 0 END), 0) AS Pedidos_Mes,\n" +
            "    COALESCE(YEAR(o.date_order), 0) AS Anio,\n" +
            "    COALESCE(SUM(CASE WHEN YEAR(o.date_order) = YEAR(NOW()) THEN 1 ELSE 0 END), 0) AS Pedidos_Anio\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id \n" +
            "WHERE \n" +
            "    (DAY(o.date_order) = DAY(NOW()) OR WEEK(o.date_order) = WEEK(NOW()) OR MONTH(o.date_order) = MONTH(NOW()) OR YEAR(o.date_order) = YEAR(NOW()))\n" +
            "GROUP BY Day, Week, Month, Anio;", nativeQuery = true)
     List<PedidosAnualesDTO> totalPedidosAnuales();

    @Query(value = "SELECT od.quantity, od.price, p.name, SUM(od.quantity * od.price) AS total\n" +
            "FROM order_details od\n" +
            "INNER JOIN product p ON p.id = od.product_id\n" +
            "INNER JOIN orders o ON o.id = od.order_id\n" +
            "WHERE o.user_id = :userEmail AND o.id = :id\n" +
            "GROUP BY od.id, p.name, od.quantity, od.price", nativeQuery = true)
     List<DetailsDTO> details(@Param("userEmail")String userEmail, @Param("id") Integer id);

    @Query(value = "SELECT o.id AS orden, od.id AS detalles, p.name AS producto, od.quantity AS cantidad, od.status AS estado\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id\n" +
            "INNER JOIN product p ON p.id = od.product_id\n" +
            "WHERE p.user_id = :sellerEmail", nativeQuery = true)
     List<DetallesSellerDTO> detalle(@Param("sellerEmail") String sellerEmail);

    @Modifying
    @Query(value = "UPDATE order_details SET status = :estado WHERE id = :id", nativeQuery = true)
    void detalleUpdate(@Param("estado") String estado, @Param("id") int id);

    @Query(value = "SELECT o.id AS orden\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id\n" +
            "INNER JOIN product p ON p.id = od.product_id\n" +
            "WHERE od.id = :id", nativeQuery = true)
    Integer validarOrden(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(od.id) AS entregas\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id\n" +
            "INNER JOIN product p ON p.id = od.product_id\n" +
            "WHERE o.id = :order_id && od.status = \"Pendiente\"", nativeQuery = true)
    RevisionpendientesDTO revisarPendientes(@Param("order_id") Integer order_id);
}
