package mp.sitili.modules.order.use_cases.repository;

import mp.sitili.modules.order.use_cases.dto.EntregasMesDTO;
import mp.sitili.modules.order.use_cases.dto.OrdersDTO;
import mp.sitili.modules.order.use_cases.dto.VentasMesDTO;

import java.util.List;
import java.util.Map;

public interface IOrederRepository {

     boolean updateDelivery(int id, String repartidor, String status);

     boolean updateRecive(int id, String status);

     Integer sellerEnvs(String sellerEmail);

     Double sellerSales(String sellerEmail);

     List<Map<String, Object>> buscarTodo(String userEmail);

    List<VentasMesDTO> ventasAnioMonth(String sellerEmail);

     List<EntregasMesDTO>  enviosAnioMonth(String sellerEmail);

     List<OrdersDTO> ordersPerUser(String userEmail);

}
