package mp.sitili.modules.payment_order.adapter;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.service.AddressService;
import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.service.OrderService;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_cc.use_cases.service.PaymentCCService;
import mp.sitili.modules.payment_order.entities.PaymentOrder;
import mp.sitili.modules.payment_order.use_cases.http.PaymentIntentDto;
import mp.sitili.modules.payment_order.use_cases.methods.PaymentOrderRepositry;
import mp.sitili.modules.payment_order.use_cases.service.PaymentService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final PaymentService paymentService;

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final AddressService addressService;

    private final OrderDetailRepository orderDetailRepository;

    private final PaymentCCRepository paymentCCRepository;

    private final PaymentOrderRepositry paymentOrderRepositry;

    private final ShoppingCarRepository shoppingCarRepository;

    private final PaymentCCService paymentCCService;

    public StripeController(OrderService orderService, PaymentService paymentService, OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, AddressService addressService, OrderDetailRepository orderDetailRepository, PaymentCCRepository paymentCCRepository, PaymentOrderRepositry paymentOrderRepositry, ShoppingCarRepository shoppingCarRepository, PaymentCCService paymentCCService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.addressService = addressService;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentCCRepository = paymentCCRepository;
        this.paymentOrderRepositry = paymentOrderRepositry;
        this.shoppingCarRepository = shoppingCarRepository;
        this.paymentCCService = paymentCCService;
    }

    /*
     * @PostMapping("/paymentintent")
     * public ResponseEntity<String> payment(@RequestBody PaymentIntentDto
     * paymentIntentDto) throws StripeException {
     * PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDto);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */

    /*
     * @PostMapping("/confirm/{id}")
     * public ResponseEntity<String> confirm(@PathVariable("id") String id) throws
     * StripeException {
     * PaymentIntent paymentIntent = paymentService.confirm(id);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */

    /*
     * @PostMapping("/cancel/{id}")
     * public ResponseEntity<String> cancel(@PathVariable("id") String id) throws
     * StripeException {
     * PaymentIntent paymentIntent = paymentService.cancel(id);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */

    @PostMapping("/paymentintent")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> payment(@RequestBody List<PaymentIntentDto> paymentIntentDtoList)
            throws StripeException {
        List<PaymentIntent> paymentIntents = paymentService.paymentIntents(paymentIntentDtoList);
        List<String> paymentStrList = paymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> cancel(@RequestBody List<String> paymentIntentIds) throws StripeException {
        List<PaymentIntent> cancelledPaymentIntents = paymentService.cancelPaymentIntents(paymentIntentIds);
        List<String> paymentStrList = cancelledPaymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> confirm(@RequestBody List<String> paymentIntentIds) throws StripeException {
        List<PaymentIntent> confirmedPaymentIntents = paymentService.confirmPaymentIntents(paymentIntentIds);
        List<String> paymentStrList = confirmedPaymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    @GetMapping("/saleCar")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> compraWeb() throws StripeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findById(userEmail).orElse(null);
        if(user == null){
            return new ResponseEntity<>("Error al encontrar al usuario", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> carrito = orderService.buscarTodo(userEmail);
        if(carrito == null){
            return new ResponseEntity<>("Error al encontrar el carrito", HttpStatus.NOT_FOUND);
        }

        Address direccion = addressService.dirActXusuario(userEmail);
        if(direccion == null){
            return new ResponseEntity<>("Error al encontrar la direccion", HttpStatus.NOT_FOUND);
        }

        PaymentCC pago = paymentCCService.tarjetaXusuario(userEmail);
        if(pago == null){
            return new ResponseEntity<>("Error al encontrar el metodo de pago", HttpStatus.NOT_FOUND);
        }

        Order orden = new Order((int) orderRepository.count() + 1, user, "Pendiente", "No asignado", direccion, new Timestamp(System.currentTimeMillis()));
        orden = orderRepository.save(orden);

        List<OrderDetail> validOrderDetails = new ArrayList<>();
        List<Product> bajarCantidades = new ArrayList<>();
        List<PaymentIntentDto> stripeIntent = new ArrayList<>();

        for (Map<String, Object> orderDetailData : carrito) {
            Integer productId = (Integer) orderDetailData.get("product_id");
            Integer quantity = (Integer) orderDetailData.get("quantity");

            Optional<Product> productoDetail = productRepository.findById(productId);
            if (productoDetail.isPresent() && productoDetail.get().getStatus()) {
                if (productoDetail.get().getStock() > 0 && quantity > 0 && quantity <= productoDetail.get().getStock()) {
                    OrderDetail orderDetail = new OrderDetail(null, orden, productoDetail.get(), quantity, productoDetail.get().getPrice(), "Pendiente");
                    validOrderDetails.add(orderDetail);
                    productoDetail.get().setStock(productoDetail.get().getStock() - quantity);
                    bajarCantidades.add(productoDetail.get());

                    PaymentIntentDto paymentIntent = new PaymentIntentDto();
                    paymentIntent.setDescription(productoDetail.get().getName());
                    paymentIntent.setAmount((int) (productoDetail.get().getPrice() * 100));
                    paymentIntent.setCurrency(PaymentIntentDto.Currency.mxn);

                    stripeIntent.add(paymentIntent);
                } else {
                    orderRepository.delete(orden);
                    return new ResponseEntity<>("La cantidad excede el stock disponible para el producto: " + productoDetail.get().getName(), HttpStatus.BAD_REQUEST);
                }
            } else {
                orderRepository.delete(orden);
                return new ResponseEntity<>("Producto no encontrado o no disponible", HttpStatus.BAD_REQUEST);
            }
        }

        List<PaymentIntent> paymentIntents = paymentService.paymentIntents(stripeIntent);

        if (!paymentIntents.isEmpty()) {
            List<String> paymentIntentIds = new ArrayList<>();

            for (PaymentIntent paymentIntent : paymentIntents) {
                paymentIntentIds.add(paymentIntent.getId());
            }

            List<PaymentIntent> confirmedPaymentIntents = paymentService.confirmPaymentIntents(paymentIntentIds);

            if(!confirmedPaymentIntents.isEmpty()){
                orderDetailRepository.saveAll(validOrderDetails);
                productRepository.saveAll(bajarCantidades);
                shoppingCarRepository.deleteAllCar(userEmail);
                paymentOrderRepositry.save(new PaymentOrder(paymentCCRepository.count() + 1, orden, pago));
                return new ResponseEntity<>("Compra realizada con exitoso", HttpStatus.OK);
            }else{
                paymentService.cancelPaymentIntents(paymentIntentIds);
                orderRepository.delete(orden);
                return new ResponseEntity<>("Error al comprobar el pago", HttpStatus.OK);
            }
        } else {
            orderRepository.delete(orden);
            return new ResponseEntity<>("Error al comprobar el pago", HttpStatus.OK);
        }

    }

}
