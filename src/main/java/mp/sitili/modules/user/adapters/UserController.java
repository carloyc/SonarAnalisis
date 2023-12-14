package mp.sitili.modules.user.adapters;

import mp.sitili.modules.jwt.entities.JwtRegister;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;
import mp.sitili.modules.user.use_cases.dto.ValidSellerDTO;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final UserRepository userRepository;

    public UserController(PasswordEncoder passwordEncoder, UserService userService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initRoleAndUser(){
        userService.initRoleAndUser();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerTodosUsuarios() {
        List<User> usuarios = userRepository.findAll();

        if(!usuarios.isEmpty()){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAdmins")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerAdministradores() {
        List<User> usuarios = userRepository.findAllAdmins();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listSellers")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerVendedores() {
        List<User> usuarios = userRepository.findAllSellers();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listUsers")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerUsuarios() {
        List<User> usuarios = userRepository.findAllUsers();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/totalUsers")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Long> totalUsuarios() {
        Long total = userRepository.count();

        if(total != 0){
            return new ResponseEntity<>(total, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/validateCompany")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<ValidSellerDTO> companiaNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        ValidSellerDTO dataUser = userService.validateCompany(sellerEmail);

        if(dataUser.getCompany() == null){
            return new ResponseEntity<>(dataUser, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(dataUser, HttpStatus.NO_CONTENT);
        }
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> obtenerUsuarios(@RequestBody JwtRegister jwtRegister) {
        Optional<User> usuarios = userRepository.findById(jwtRegister.getEmail());

        if(usuarios.isPresent()){
            usuarios.get().setPassword(passwordEncoder.encode(jwtRegister.getPassword()));
            userRepository.save(usuarios.get());
            return new ResponseEntity<>("Contraseña Actualizada", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Usuario no Encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> obtenerUsuarios(@RequestBody SelectVendedorDTO user) {
        Optional<User> usuario = userRepository.findById(user.getEmail());

        if(usuario.isPresent()){
            if(usuario.get().getStatus()){
                userRepository.bajaLogica(usuario.get().getEmail(), false);
                return new ResponseEntity<>("Usuario dado de Baja", HttpStatus.OK);
            }else{
                userRepository.bajaLogica(usuario.get().getEmail(), true);
                return new ResponseEntity<>("Usuario dado de Alta", HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
