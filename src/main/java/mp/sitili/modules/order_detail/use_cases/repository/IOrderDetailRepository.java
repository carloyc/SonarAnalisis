package mp.sitili.modules.order_detail.use_cases.repository;

import mp.sitili.modules.order_detail.use_cases.dto.*;
import java.util.List;

public interface IOrderDetailRepository {
     VentasDTO totalVentas();

     List<VentasAnualesDTO> totalVentasAnuales();

     List<PedidosAnualesDTO> totalPedidosAnuales();

      List<DetallesSellerDTO> detalle(String sellerEmail);

    boolean detalleUpdate(String estado, int id);

    Integer validarOrden(Integer id);

    RevisionpendientesDTO revisarPendientes(Integer order_id);
}
