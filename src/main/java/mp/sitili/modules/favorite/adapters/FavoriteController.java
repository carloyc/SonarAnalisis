package mp.sitili.modules.favorite.adapters;

import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.dto.FavoriteDTO;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.favorite.use_cases.service.FavoriteService;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.dto.ProductDTO;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
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
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;

    private final FavoriteService favoriteService;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public FavoriteController(FavoriteRepository favoriteRepository, FavoriteService favoriteService, UserRepository userRepository, ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List> listarFavxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<Map<String, Object>> favoritos = favoriteService.favXusuario(userEmail);

        if(favoritos != null){
            return new ResponseEntity<>(favoritos, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(favoritos, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addFavxUsuarios(@RequestBody FavoriteDTO product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(product.getId());

        if(producto.isEmpty()){
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.BAD_REQUEST);
        }

        Favorite favorite = favoriteService.validarExis(producto.get().getId(), userEmail);

        if(favorite == null){
            if(user != null && producto.isPresent()){
                Favorite fav = favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user, producto.get()));
                if(fav.getId() != null && fav.getId() != 0){
                    return new ResponseEntity<>("Agregado a favoritos", HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }else{
                return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Producto repetido", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> delFavxUsuarios(@RequestBody FavoriteDTO favorite) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);

        Favorite favorito = favoriteService.findById1(favorite.getId());

        if(user != null && favorito != null){
            boolean revision = favoriteService.deleteFav(user.getEmail(), favorito.getId());
            if(revision){
                return new ResponseEntity<>("Eliminado de favoritos", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al eliminar", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Favorito no encontrado", HttpStatus.BAD_REQUEST);
        }

    }


}
