package mp.sitili.modules.order.adapters;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.service.AddressService;
import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.dto.EntregasMesDTO;
import mp.sitili.modules.order.use_cases.dto.OrdersDTO;
import mp.sitili.modules.order.use_cases.dto.PaymentCCDTO;
import mp.sitili.modules.order.use_cases.dto.VentasMesDTO;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.service.OrderService;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.dto.PedidosAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasDTO;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.order_detail.use_cases.service.OrderDetailService;
import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_cc.use_cases.service.PaymentCCService;
import mp.sitili.modules.payment_order.entities.PaymentOrder;
import mp.sitili.modules.payment_order.use_cases.methods.PaymentOrderRepositry;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final OrderDetailService orderDetailService;

    private final AddressService addressService;

    private final OrderDetailRepository orderDetailRepository;

    private final PaymentCCRepository paymentCCRepository;

    private final PaymentOrderRepositry paymentOrderRepositry;

    private final ShoppingCarRepository shoppingCarRepository;

    private final PaymentCCService paymentCCService;

    public OrderController(OrderRepository orderRepository, OrderService orderService, UserRepository userRepository, ProductRepository productRepository, OrderDetailService orderDetailService, AddressService addressService, OrderDetailRepository orderDetailRepository, PaymentCCRepository paymentCCRepository, PaymentOrderRepositry paymentOrderRepositry, ShoppingCarRepository shoppingCarRepository, PaymentCCService paymentCCService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderDetailService = orderDetailService;
        this.addressService = addressService;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentCCRepository = paymentCCRepository;
        this.paymentOrderRepositry = paymentOrderRepositry;
        this.shoppingCarRepository = shoppingCarRepository;
        this.paymentCCService = paymentCCService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerTodasLasOrdenes() {
        List<Order> ordenes = orderRepository.findAll();

        if(!ordenes.isEmpty()){
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ordenes, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/deliveryStart")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> contarTodasLasOrdenes() {
        List<Order> ordenes = orderRepository.findAll();

        if(!ordenes.isEmpty()){
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ordenes, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/saleAll")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<VentasDTO> totalVentas() {
        VentasDTO ventas = orderDetailService.totalVentas();

        if(ventas != null){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<VentasAnualesDTO>> totalVentasAnuales() {
        List<VentasAnualesDTO> ventas = orderDetailService.totalVentasAnuales();

        if(ventas != null){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/salesPack")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<PedidosAnualesDTO>> totalPedidosAnuales() {
        List<PedidosAnualesDTO> ventas = orderDetailService.totalPedidosAnuales();

        if(ventas != null){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createOne")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> crearOrden(@RequestPart("productData") Map<String, Object> productData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        //product_id
        //quantity
        //address_id
        //payment_id

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById((Integer) productData.get("product_id"));

        if (user != null && producto.isPresent() && producto.get().getStatus()) {
            if (producto.get().getStock() > 0 && (Integer) productData.get("quantity") > 0 && (Integer) productData.get("quantity") < producto.get().getStock()) {
                Address address = addressService.buscarDir((Integer) productData.get("address_id"), user.getEmail());
                if (address != null) {
                    Optional<PaymentCC> paymentCC = paymentCCRepository.findById(String.valueOf(productData.get("paymentCC_id")));
                    if (paymentCC.isPresent()) {
                        Date date = new Date();
                        Timestamp timestamp = new Timestamp(date.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Timestamp dateOrder = Timestamp.valueOf(sdf.format(timestamp));

                        Order orden = orderRepository.save(new Order((int) orderRepository.count() + 1, user, "Pendiente", "No asignado", address, dateOrder));
                        if (orden.getId() != null && orden.getId() != 0) {
                            OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden, producto.get(), (Integer) productData.get("quantity"), producto.get().getPrice(), "Pendiente"));
                            if (orderDetail.getId() != null && orderDetail.getId() != 0) {
                                producto.get().setStock(producto.get().getStock() - (Integer) productData.get("quantity"));
                                productRepository.save(producto.get());
                                return new ResponseEntity<>("Orden creada con exito, N. Orden " + orden.getId(), HttpStatus.OK);
                            } else {
                                orderRepository.delete(orden);
                                return new ResponseEntity<>("Error al crear OrdenDetail / Falta PAGO", HttpStatus.BAD_REQUEST);
                            }
                        } else {
                            return new ResponseEntity<>("Error al crear Orden", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return new ResponseEntity<>("Forma de pago no encontrada", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>("Direccion no valida", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Cantidad no aceptable", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Usuario o producto no válidos", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createMany")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> crearOrdenes(
            @RequestPart("productData") Map<String, Object> productData,
            @RequestPart("orderDetails") List<Map<String, Object>> orderDetailsData
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);
        if (user != null) {
            Address address = addressService.buscarDir((Integer) productData.get("address_id"), user.getEmail());
            if (address != null) {
                Optional<PaymentCC> paymentCC = paymentCCRepository.findById(String.valueOf(productData.get("paymentCC_id")));
                if (paymentCC.isPresent()) {
                    Order orden = new Order((int) orderRepository.count() + 1, user, "Pendiente", "No asignado", address, new Timestamp(System.currentTimeMillis()));
                    orden = orderRepository.save(orden);

                    List<OrderDetail> validOrderDetails = new ArrayList<>();
                    List<Product> bajarCantidades = new ArrayList<>();

                    for (Map<String, Object> orderDetailData : orderDetailsData) {
                        Integer productId = (Integer) orderDetailData.get("product_id");
                        Integer quantity = (Integer) orderDetailData.get("quantity");

                        Optional<Product> productoDetail = productRepository.findById(productId);
                        if (productoDetail.isPresent() && productoDetail.get().getStatus()) {
                            if (productoDetail.get().getStock() > 0 && quantity > 0 && quantity <= productoDetail.get().getStock()) {
                                OrderDetail orderDetail = new OrderDetail(null, orden, productoDetail.get(), quantity, productoDetail.get().getPrice(), "Pendiente");
                                validOrderDetails.add(orderDetail);
                                productoDetail.get().setStock(productoDetail.get().getStock() - quantity);
                                bajarCantidades.add(productoDetail.get());
                            } else {
                                orderRepository.delete(orden);
                                return new ResponseEntity<>("Cantidad requerida excede el stock, el producto es: " + productoDetail.get().getName(), HttpStatus.BAD_REQUEST);
                            }
                        } else {
                            orderRepository.delete(orden);
                            return new ResponseEntity<>("Producto no encontrado o no disponible", HttpStatus.BAD_REQUEST);
                        }
                    }

                    //Bajar cantidades de productos
                    orderDetailRepository.saveAll(validOrderDetails);
                    productRepository.saveAll(bajarCantidades);

                    return new ResponseEntity<>("Orden creada con éxito, N. Orden " + orden.getId(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Forma de pago no encontrada", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Direccion no valida", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saleCar")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> comprarCarrito(@RequestBody PaymentCCDTO paymentCC) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findById(userEmail).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> carrito = orderService.buscarTodo(userEmail);
        Address direccion = addressService.buscarDir(paymentCC.getAddress_id(), userEmail);
        PaymentCC pago = paymentCCService.findById(paymentCC.getCc_id());

        if (carrito.isEmpty()) {
            return new ResponseEntity<>("El carrito está vacío", HttpStatus.BAD_REQUEST);
        }

        if (direccion == null) {
            return new ResponseEntity<>("Dirección no encontrada", HttpStatus.NOT_FOUND);
        }

        if (pago == null) {
            return new ResponseEntity<>("Forma de pago no asociada", HttpStatus.NOT_FOUND);
        }

        Order orden = new Order((int) orderRepository.count() + 1, user, "Pendiente", "No asignado", direccion, new Timestamp(System.currentTimeMillis()));
        orden = orderRepository.save(orden);

        List<OrderDetail> validOrderDetails = new ArrayList<>();
        List<Product> bajarCantidades = new ArrayList<>();

        for (Map<String, Object> orderDetailData : carrito) {
            Integer productId = (Integer) orderDetailData.get("product_id");
            Integer quantity = (Integer) orderDetailData.get("quantity");

            Optional<Product> productoDetail = productRepository.findById(productId);
            if (productoDetail.isPresent() && productoDetail.get().getStatus()) {
                if (productoDetail.get().getStock() > 0 && quantity > 0 && quantity <= productoDetail.get().getStock()) {
                    OrderDetail orderDetail = new OrderDetail(null, orden, productoDetail.get(), quantity, productoDetail.get().getPrice(), "Prendiente");
                    validOrderDetails.add(orderDetail);
                    productoDetail.get().setStock(productoDetail.get().getStock() - quantity);
                    bajarCantidades.add(productoDetail.get());
                } else {
                    orderRepository.delete(orden);
                    return new ResponseEntity<>("La cantidad excede el stock disponible para el producto: " + productoDetail.get().getName(), HttpStatus.BAD_REQUEST);
                }
            } else {
                orderRepository.delete(orden);
                return new ResponseEntity<>("Producto no encontrado o no disponible", HttpStatus.BAD_REQUEST);
            }
        }

        // Guardar detalles de orden, actualizar cantidades y limpiar carrito
        orderDetailRepository.saveAll(validOrderDetails);
        productRepository.saveAll(bajarCantidades);
        shoppingCarRepository.deleteAllCar(userEmail);
        paymentOrderRepositry.save(new PaymentOrder(paymentCCRepository.count() + 1, orden, pago));

        return new ResponseEntity<>("Compra realizada con éxito", HttpStatus.OK);
    }

    @PostMapping("/delivery")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> asignarRepartidor(@RequestBody OrdersDTO order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);
        Optional<Order> orden = orderRepository.findById(String.valueOf(order.getId()));
        if (user != null && orden.isPresent()) {
            boolean revision = orderService.updateDelivery(orden.get().getId(), order.getRepartidor(), "Trafico");
            if(revision){
                return new ResponseEntity<>("Repartidor Actualizado", HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>("Error al asignar repartidor", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Orden no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/recive")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> pedidoEntregado(@RequestBody OrdersDTO order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);
        Optional<Order> orden = orderRepository.findById(String.valueOf(order.getId()));
        if (user != null && orden.isPresent()) {
            boolean revision = orderService.updateRecive(orden.get().getId(), "Entregado");
            if(revision){
                return new ResponseEntity<>("Estado Actualizado", HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>("Error al asignar estado", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Orden no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sellerEnvs")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<Integer> totalDeEnviosSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        Integer contador = orderService.sellerEnvs(sellerEmail);

        if(contador != 0){
            return new ResponseEntity<>(contador, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(contador, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/sellerSales")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<Double> totalDeVentasSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        Double contador = orderService.sellerSales(sellerEmail);

        if(contador != 0.0){
            return new ResponseEntity<>(contador, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(contador, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/sellerSalesMonth")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<List<VentasMesDTO>> ventasAnioMonth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        List<VentasMesDTO> ventas = orderService.ventasAnioMonth(sellerEmail);

        if(!ventas.isEmpty()){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/sellerDeliveryMonth")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<List<EntregasMesDTO>> enviosAnioMonth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        List<EntregasMesDTO> ventas = orderService.enviosAnioMonth(sellerEmail);

        if(!ventas.isEmpty()){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/listUser")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<OrdersDTO>> listarOrdenesUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<OrdersDTO> orders = orderService.ordersPerUser(userEmail);

        if(!orders.isEmpty()){
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(orders, HttpStatus.NO_CONTENT);
        }
    }


}
