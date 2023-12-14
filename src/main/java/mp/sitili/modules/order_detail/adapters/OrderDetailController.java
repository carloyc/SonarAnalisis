package mp.sitili.modules.order_detail.adapters;

import mp.sitili.modules.order.use_cases.dto.OrdersDTO;
import mp.sitili.modules.order.use_cases.service.OrderService;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.dto.DetailsDTO;
import mp.sitili.modules.order_detail.use_cases.dto.DetallesSellerDTO;
import mp.sitili.modules.order_detail.use_cases.dto.RevisionpendientesDTO;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.order_detail.use_cases.service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orderDetail")
public class OrderDetailController {

    private final List<String> shippingCompanies = new ArrayList<>();

    private final SecureRandom secureRandom = new SecureRandom();

    private final OrderDetailRepository orderDetailRepository;

    private final OrderDetailService orderDetailService;

    private final OrderService orderService;

    public OrderDetailController(OrderDetailRepository orderDetailRepository, OrderDetailService orderDetailService, OrderService orderService) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
    }


    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerTodasLasOrdenDetails() {
        List<OrderDetail> ordenes = orderDetailRepository.findAll();

        if(!ordenes.isEmpty()){
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ordenes, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/listDetails")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<DetailsDTO>> obtenerDetallesOrden(@RequestBody OrdersDTO order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<DetailsDTO> detalles = orderDetailRepository.details(userEmail, order.getId());

        if(!detalles.isEmpty()){
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(detalles, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/listOrderDetails")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<List<DetallesSellerDTO>> listarDetallesOrdenesSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<DetallesSellerDTO> detalles = orderDetailService.detalle(userEmail);

        if(!detalles.isEmpty()){
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(detalles, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/statusOrderDetails")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> cambiarEstadoOrderDetails(@RequestBody OrdersDTO orderDetail) {

        shippingCompanies.add("UPS");
        shippingCompanies.add("FedEx");
        shippingCompanies.add("DHL");
        shippingCompanies.add("USPS");
        shippingCompanies.add("Amazon Logistics");
        shippingCompanies.add("Shopify Shipping");
        shippingCompanies.add("ShipStation");
        shippingCompanies.add("WappiCorpsDelivery");

        boolean detalles = orderDetailService.detalleUpdate("Trafico", orderDetail.getId());

        Integer order_id = orderDetailService.validarOrden(orderDetail.getId());
        RevisionpendientesDTO orden = orderDetailService.revisarPendientes(order_id);
        if (orden.getEntregas() == 0) {
            int indiceAleatorio = secureRandom.nextInt(shippingCompanies.size());
            String empresaRepartoAleatoria = shippingCompanies.get(indiceAleatorio);
            orderService.updateDelivery(order_id, empresaRepartoAleatoria, "Trafico");
        }

        if(detalles){
            return new ResponseEntity<>("Estado de detalle modificado", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Error en modificar el estado del detalle", HttpStatus.BAD_REQUEST);
        }
    }

}
