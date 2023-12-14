package mp.sitili.modules.payment_cc.adapters;

import mp.sitili.modules.order.use_cases.dto.PaymentCCDTO;
import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.dto.PaymentCC1DTO;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_cc.use_cases.service.PaymentCCService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/paymentcc")
public class PaymentCCController {

    private final PaymentCCService paymentCCService;

    private final PaymentCCRepository paymentCCRepository;

    private final UserRepository userRepository;

    public PaymentCCController(PaymentCCService paymentCCService, PaymentCCRepository paymentCCRepository, UserRepository userRepository) {
        this.paymentCCService = paymentCCService;
        this.paymentCCRepository = paymentCCRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/lists")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<PaymentCC>> listarPagoxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<PaymentCC> pago = paymentCCService.pagoXusuario(userEmail);

        if(pago != null){
            return new ResponseEntity<>(pago, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(pago, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<PaymentCC> listarUnaTarjeta() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        PaymentCC pago = paymentCCService.tarjetaXusuario(userEmail);

        if(pago != null){
            return new ResponseEntity<>(pago, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(pago, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<PaymentCC> asociarPago(@RequestBody PaymentCC1DTO paymentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);

        if (user != null && paymentDTO != null) {
            String cc = paymentDTO.getCc();
            String cvv = paymentDTO.getCvv();
            String expiryDate = paymentDTO.getExpiryDate();
            String[] parts = expiryDate.split("/");
            String month = parts[0];
            String year = parts[1];

            PaymentCC pago = paymentCCRepository.save(new PaymentCC((int) (paymentCCRepository.count() + 1), user, cc, month, year, cvv));

            if (pago.getId() != null && pago.getId() != 0) {
                return new ResponseEntity<>(pago, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(pago, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> actualizarPago(@RequestBody PaymentCC1DTO paymentCC) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);

        if(user != null){
            if(paymentCC != null){
                PaymentCC pago = paymentCCRepository.save(new PaymentCC(paymentCC.getId(), user, paymentCC.getCc(), paymentCC.getMonth(), paymentCC.getYear(), paymentCC.getCvv()));
                if(pago.getId() != null){
                    return new ResponseEntity<>("Datos de pago actualizados exitosamente", HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Error al actualizar datos de pago", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Datos Faltantes", HttpStatus.NO_CONTENT);
            }
        }else {
            return new ResponseEntity<>("Usaurio inexistente", HttpStatus.NO_CONTENT);
        }
    }

}
