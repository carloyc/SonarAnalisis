package mp.sitili.modules.raiting.adapters;

import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.raiting.entities.Raiting;
import mp.sitili.modules.raiting.use_cases.dto.RaitingDTO;
import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.raiting.use_cases.service.RaitingService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/raiting")
public class RaitingController {

    private final RaitingRepository raitingRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final RaitingService raitingService;

    public RaitingController(RaitingRepository raitingRepository, UserRepository userRepository, ProductRepository productRepository, RaitingService raitingService) {
        this.raitingRepository = raitingRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.raitingService = raitingService;
    }

    @PostMapping("/rate")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> calificarProducto(@RequestBody RaitingDTO raiting) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> product = productRepository.findById(raiting.getId());

        if(product.isEmpty()){
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }

        Raiting rate = raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, raiting.getRaiting(), product.get(), user));

        if(rate != null && user != null && product.isPresent()){
            return new ResponseEntity<>("Producto valorado", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Error al valorar producto", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/rateSeller")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<Integer> productosCalificados4() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        Integer productos = raitingService.cal4(sellerEmail);

        if(productos != 0){
            return new ResponseEntity<>(productos, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(productos, HttpStatus.NO_CONTENT);
        }
    }
}
