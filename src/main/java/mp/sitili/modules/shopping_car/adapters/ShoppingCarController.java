package mp.sitili.modules.shopping_car.adapters;

import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.dto.PorduDTO;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.service.ProductService;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import mp.sitili.modules.shopping_car.use_cases.dto.ShoppCarDTO;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import mp.sitili.modules.shopping_car.use_cases.service.ShoppingCarService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/shoppingCar")
public class ShoppingCarController {

    private final ShoppingCarService shoppingCarService;

    private final ShoppingCarRepository shoppingCarRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ProductService productService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    public ShoppingCarController(ShoppingCarService shoppingCarService,ShoppingCarRepository shoppingCarRepository, UserRepository userRepository, ProductRepository productRepository, ProductService productService) {
        this.shoppingCarService = shoppingCarService;
        this.shoppingCarRepository = shoppingCarRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List> listarCarxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<Map<String, Object>> favoritos = shoppingCarService.carXusuario(userEmail);

        if(favoritos != null){
            return new ResponseEntity<>(favoritos, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(favoritos, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/listb")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List> listarCarxUsuariob() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<Map<String, Object>> favoritos = shoppingCarService.carXusuariob(userEmail);

        if(favoritos != null){
            return new ResponseEntity<>(favoritos, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(favoritos, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addCarxUsuarios(@RequestBody PorduDTO product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(product.getId());

        if(producto.isEmpty()){
            return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ShoppingCar shopp = shoppingCarService.validarExis(producto.get().getId(), userEmail);

        if(shopp == null){
            if(user != null && producto.isPresent()){
                if(product.getStock() < producto.get().getStock() && product.getStock() > 0){
                    ShoppingCar shoppingCar = shoppingCarRepository.save(new ShoppingCar((int) shoppingCarRepository.count() + 1,user , producto.get(), product.getStock()));
                    if(shoppingCar.getId() != null && shoppingCar.getId() != 0){
                        return new ResponseEntity<>("Agregado a carrito de compras", HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }else{
                    return new ResponseEntity<>("Cantidad excedente", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>("Prodcuto repetido", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/create2")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addCarxUsuariosF(@RequestBody PorduDTO product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Favorite fav = favoriteRepository.findById1(product.getId());
        Optional<Product> producto = productRepository.findById(fav.getProduct().getId());
        if(producto.isEmpty()){
            return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ShoppingCar shopp = shoppingCarService.validarExis(producto.get().getId(), userEmail);

        if(shopp == null){
            if(user != null && producto.isPresent()){
                if(product.getStock() < producto.get().getStock() && product.getStock() > 0){
                    ShoppingCar shoppingCar = shoppingCarRepository.save(new ShoppingCar((int) shoppingCarRepository.count() + 1,user , producto.get(), product.getStock()));
                    if(shoppingCar.getId() != null && shoppingCar.getId() != 0){
                        return new ResponseEntity<>("Agregado a carrito de compras", HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }else{
                    return new ResponseEntity<>("Cantidad excedente", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>("Prodcuto repetido", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/createF")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addCarxUsuariosf(@RequestBody PorduDTO product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(product.getId());

        if(producto.isEmpty()){
            return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.NOT_FOUND);
        }

        ShoppingCar shopp = shoppingCarService.validarExis(producto.get().getId(), userEmail);

        if(shopp == null){
            if(user != null && producto.isPresent()){
                    ShoppingCar shoppingCar = shoppingCarRepository.save(new ShoppingCar((int) shoppingCarRepository.count() + 1,user , producto.get(), 1));
                    if(shoppingCar.getId() != null && shoppingCar.getId() != 0){
                        return new ResponseEntity<>("Agregado a carrito de compras", HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
            }else{
                return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Prodcuto repetido", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> delCarxUsuarios(@RequestBody ShoppCarDTO shoppingCar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);

        ShoppingCar shoppingCar1 = shoppingCarService.findById1(shoppingCar.getId());

        if(user != null && shoppingCar1 != null){
            boolean revision = shoppingCarService.deleteCar(user.getEmail(), shoppingCar1.getId());
            if(revision){
                return new ResponseEntity<>("Eliminado de carrito de compras", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al eliminar", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> actualizarCarrito(@RequestBody ShoppCarDTO shoppingCar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        ShoppingCar shoppingCar1 = shoppingCarService.findById1(shoppingCar.getId());
        Product producto = productService.findOnlyById(shoppingCar1.getProduct().getId());


        if(user != null){
            if(shoppingCar1.getId() != null && shoppingCar1.getId() != 0){
                if(producto != null){
                    if(shoppingCar.getQuantity() < producto.getStock()){

                            boolean revision =  shoppingCarService.updateCar(shoppingCar.getId(), shoppingCar.getQuantity());
                            if(revision){
                                return new ResponseEntity<>("Carrito actualizado", HttpStatus.OK);
                            }else{
                                return new ResponseEntity<>("Error al alctualizar carrito", HttpStatus.INTERNAL_SERVER_ERROR);
                            }

                    }else{
                        return new ResponseEntity<>("Cantidad excesiva", HttpStatus.NOT_ACCEPTABLE);
                    }
                }else{
                    return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity<>("Carrito no encontrado", HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>("usuario no existe", HttpStatus.NOT_FOUND);
        }
    }
}
