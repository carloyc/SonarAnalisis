package mp.sitili.modules.order_detail.use_cases.service;

import mp.sitili.modules.image_product.use_cases.service.ImageProductService;
import mp.sitili.modules.order_detail.use_cases.dto.*;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.order_detail.use_cases.repository.IOrderDetailRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderDetailService implements IOrderDetailRepository {

    private static final Logger LOGGER = Logger.getLogger(OrderDetailService.class.getName());

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public VentasDTO totalVentas(){
        return orderDetailRepository.totalVentas();
    }

    @Override
    public List<VentasAnualesDTO> totalVentasAnuales(){
        return orderDetailRepository.totalVentasAnuales();
    }

    @Override
    public List<PedidosAnualesDTO> totalPedidosAnuales(){
        return orderDetailRepository.totalPedidosAnuales();
    }

    @Override
    public  List<DetallesSellerDTO> detalle(String sellerEmail){
        return orderDetailRepository.detalle(sellerEmail);
    }

    @Override
    public boolean detalleUpdate(String estado, int id) {
        try {
            orderDetailRepository.detalleUpdate(estado, id);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " , e);
            return false;
        }
    }

    @Override
    public Integer validarOrden(Integer id){
        return orderDetailRepository.validarOrden(id);
    }

    @Override
    public RevisionpendientesDTO revisarPendientes(Integer order_id){
        return orderDetailRepository.revisarPendientes( order_id);
    }

}
