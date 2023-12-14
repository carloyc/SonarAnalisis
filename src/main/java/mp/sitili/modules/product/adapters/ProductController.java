package mp.sitili.modules.product.adapters;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.image_product.use_cases.dto.ImageDTO;
import mp.sitili.modules.image_product.use_cases.service.ImageProductService;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.dto.PorduDTO;
import mp.sitili.modules.product.use_cases.dto.UserEmailDTO;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.service.ProductService;
import mp.sitili.modules.raiting.entities.Raiting;
import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import mp.sitili.utils.aws.AWSS3ServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductService productService;

    private final UserRepository userRepository;

    private final AWSS3ServiceImp awss3ServiceImp;

    private final FavoriteRepository favoriteRepository;

    private final ShoppingCarRepository shoppingCarRepository;

    private final ImageProductService imageProductService;

    private final RaitingRepository raitingRepository;

    private final UserService userService;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, ProductService productService, UserRepository userRepository, AWSS3ServiceImp awss3ServiceImp, FavoriteRepository favoriteRepository, ShoppingCarRepository shoppingCarRepository, ImageProductService imageProductService, RaitingRepository raitingRepository, UserService userService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.awss3ServiceImp = awss3ServiceImp;
        this.favoriteRepository = favoriteRepository;
        this.shoppingCarRepository = shoppingCarRepository;
        this.imageProductService = imageProductService;
        this.raitingRepository = raitingRepository;
        this.userService = userService;
    }


    @GetMapping("/listAll")
    public ResponseEntity<List> obtenerTodoProductos() {
        List<Map<String, Object>> products = productService.findAllProducts();

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/listProduct")
    public ResponseEntity<List> obtenerProducto(@RequestBody PorduDTO product) {
        List<Map<String, Object>> products = productService.findProduct(product.getId());

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/totalProducts")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<Long> totalProductos() {
        Long total = productRepository.count();

        if(total == 0){
            return new ResponseEntity<>(total, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/listAllVend")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<List> obtenerTodoProductosxVendedor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        return getListResponseEntity(sellerEmail);
    }

    @GetMapping("/totSeller")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<Integer> obtenerTotalProductosxVendedor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        Integer contador = productService.countProSel(sellerEmail);
        System.out.println(contador);

        if(contador != 0){
            return new ResponseEntity<>(contador, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(contador, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/listSeller")
    public ResponseEntity<List> obtenerTodoProductosxVendedorPublico(@RequestBody UserEmailDTO user) {
        String sellerEmail = user.getEmail();
        return getListResponseEntity(sellerEmail);
    }

    private ResponseEntity<List> getListResponseEntity(String sellerEmail) {
        List<Map<String, Object>> products = productService.findAllxVend(sellerEmail);

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/selectSeller")
    public ResponseEntity<List<SelectVendedorDTO>> obtenerVendedores() {
        List<SelectVendedorDTO> vendedores = userService.findSellers();

        if(vendedores != null){
            return new ResponseEntity<>(vendedores, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(vendedores, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> guardarProductoConImagenes(@RequestPart("productData") Map<String, Object> productData,
                                                             @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();
        int contador = 0;


        if (!productData.isEmpty()) {
            int contador2 = files.size();
            String name = (String) productData.get("name");
            int stock = (int) productData.get("stock");
            System.out.println(productData.get("price"));
            String price = (String) productData.get("price");
            Double price1 = Double.valueOf(price);
            String features = (String) productData.get("features");
            int categoryId = (int) productData.get("category_id");
            Category category = categoryRepository.getCatById(categoryId);
            User user = userRepository.findById(String.valueOf(sellerEmail)).orElse(null);
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp registerProduct = Timestamp.valueOf(sdf.format(timestamp));

            Product productSaved = productRepository.save(new Product(name, stock, price1, features, category, user, registerProduct, true));
            if (productSaved.getId() != null && productSaved.getId() != 0) {
                raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 0.0, productSaved, user));
                if (files != null && !files.isEmpty()) {

                    for (MultipartFile file : files) {

                        String key = awss3ServiceImp.uploadFile(file);
                        String url = awss3ServiceImp.getObjectUrl(key);
                        if(imageProductService.saveImgs(url, productSaved.getId())){
                            contador++;
                        }

                    }
                }
                return new ResponseEntity<>("Producto creado exitosamente, se cargaron " + contador + " de "+ contador2 + " imagenes correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error al guardar producto", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Los datos del producto son inválidos", HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> actualizarProductoConImagenes(@RequestPart("productData") Map<String, Object> productData,
                                                                @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();
        int contador = 0;

        if (!productData.isEmpty()) {
            Integer product_id = (Integer) productData.get("product_id");
            String name = (String) productData.get("name");
            int stock = (int) productData.get("stock");
            Object priceObject = productData.get("price");
            Double price1 = null;
            if (priceObject instanceof Integer) {
                Integer priceInt = (Integer) priceObject;
                String priceStr = String.valueOf(priceInt);
                price1 = Double.valueOf(priceStr);
            } else if (priceObject instanceof String) {
                String priceStr = (String) priceObject;
                price1 = Double.valueOf(priceStr);
            }
            String features = (String) productData.get("features");
            Optional<Product> prod = productRepository.findById(product_id);
            if(prod.isEmpty()){
                return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
            }
            User user = userRepository.findById(String.valueOf(sellerEmail)).orElse(null);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp registerProduct = Timestamp.valueOf(sdf.format(timestamp));

            Product productSaved = productRepository.save(new Product(product_id, name, stock, price1, features, prod.get().getCategory(), user, registerProduct, true));

            if (productSaved.getId() != null && productSaved.getId() != 0) {
                System.out.println(files);
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        String key = awss3ServiceImp.uploadFile(file);
                        String url = awss3ServiceImp.getObjectUrl(key);
                        if (imageProductService.saveImgs(url, productSaved.getId())) {
                            contador++;
                        }
                    }
                    return new ResponseEntity<>("Producto actualizado exitosamente, se cargaron " + contador + " de " + files.size() + " imagenes correctamente", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Producto actualizado sin imagenes", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("Error al actualizar producto", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Los datos del producto son inválidos", HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/deleteImages")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> eliminarImages(@RequestBody ImageDTO imageProduct) {
        if (imageProduct != null) {
            Integer product_id = imageProduct.getId();
            String image_url = imageProduct.getImageUrl();

            Optional<Product> productSaved = productRepository.findById(product_id);

            if (productSaved.isPresent() && image_url != null) {

                if (imageProductService.deleteImage(image_url, product_id)) {
                    return new ResponseEntity<>("Imagen eliminada correctamente" ,HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("No se elimino la imagen correctamente" ,HttpStatus.EXPECTATION_FAILED);
                }

            } else {
                return new ResponseEntity<>("El producto no existe", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Los datos del producto son inválidos", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> bajaLogica(@RequestBody PorduDTO product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        Optional<Product> productSaved = productRepository.findById(product.getId());

        if(productSaved.isPresent()){
            if(productSaved.get().getStatus()){
                favoriteRepository.deleteTodos(productSaved.get().getId());
                shoppingCarRepository.deleteTodos(productSaved.get().getId());
                productRepository.bajaLogica(productSaved.get().getId(), false, sellerEmail);
                return new ResponseEntity<>("Producto dado de Baja", HttpStatus.OK);
            }else{
                productRepository.bajaLogica(productSaved.get().getId(), true, sellerEmail);
                return new ResponseEntity<>("Producto dado de Alta", HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }


    }

}
