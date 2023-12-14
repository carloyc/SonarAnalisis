package mp.sitili.modules.acept_seller.adapters;

import mp.sitili.modules.acept_seller.entities.AceptSeller;
import mp.sitili.modules.acept_seller.use_cases.dto.AceptSeller1DTO;
import mp.sitili.modules.acept_seller.use_cases.dto.AceptSellerDTO;
import mp.sitili.modules.acept_seller.use_cases.methods.AceptSellerRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aceptSeller")
public class AceptSellerController {

    private final AceptSellerRepository aceptSellerRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public AceptSellerController(AceptSellerRepository aceptSellerRepository, UserRepository userRepository, UserService userService) {
        this.aceptSellerRepository = aceptSellerRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/listSellersNa")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<List<AceptSellerDTO>> obtenerVendedoresNa() {
        List<AceptSellerDTO> usuarios = aceptSellerRepository.findAllSellersNa();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/acept")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<String> aceptarVendedor(@RequestBody AceptSeller1DTO data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        Optional<User> user = userRepository.findById(data.getEmail());

        if (user.isPresent()) {
            AceptSeller aceptSeller = new AceptSeller();
            aceptSeller.setAdmin(userRepository.findById(adminEmail).orElse(null));
            aceptSeller.setSeller(user.get());
            aceptSellerRepository.save(aceptSeller);
            userRepository.bajaLogica(user.get().getEmail(), true);
            userService.sendEmail(user.get().getEmail(), "Aceptado como Vendedor", adminEmail + " te acepto como vendedor en SITILI");
            return new ResponseEntity<>("Usuario aceptado como vendedor", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

}
