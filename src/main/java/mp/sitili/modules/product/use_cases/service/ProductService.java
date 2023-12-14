package mp.sitili.modules.product.use_cases.service;

import mp.sitili.modules.data_user.use_cases.service.DataUserService;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductService implements IProductRepository {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Map<String, Object>> findAllProducts() {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = productRepository.findAllProducts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID ", e);
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("cantidad", row[3]);
            productData.put("comentarios", row[4]);
            productData.put("calificacion", row[5]);
            productData.put("estado", row[6]);
            productData.put("categoria", row[7]);
            productData.put("vendedor", row[8]);
            productData.put("nombreVendedor", row[9]);
            productData.put("apellidoVendedor", row[10]);
            String imagenesConcatenadas = (String) row[11];
            if(imagenesConcatenadas == null){
                productData.put("imagenes", "Aun no cuenta con Imagenes");
            }else{
                List<String> listaImagenes = Arrays.asList(imagenesConcatenadas.split(","));
                productData.put("imagenes", listaImagenes);
            }
            productList.add(productData);
        }
        return productList;
    }

    @Override
    public List<Map<String, Object>> findProduct(Integer product_id) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = productRepository.findProduct(product_id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " , e);
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("cantidad", row[3]);
            productData.put("comentarios", row[4]);
            productData.put("calificacion", row[5]);
            productData.put("estado", row[6]);
            productData.put("categoria", row[7]);
            productData.put("vendedor", row[8]);
            productData.put("nombreVendedor", row[9]);
            productData.put("apellidoVendedor", row[10]);
            String imagenesConcatenadas = (String) row[11];
            if(imagenesConcatenadas == null){
                productData.put("imagenes", "Aun no cuenta con Imagenes");
            }else{
                List<String> listaImagenes = Arrays.asList(imagenesConcatenadas.split(","));
                productData.put("imagenes", listaImagenes);
            }
            productList.add(productData);
        }
        return productList;
    }

    @Override
    public List<Map<String, Object>> findAllxVend(String email) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = productRepository.findAllxVend(email);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + email, e);
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("cantidad", row[3]);
            productData.put("comentarios", row[4]);
            productData.put("calificacion", row[5]);
            productData.put("estado", row[6]);
            productData.put("categoria", row[7]);
            productData.put("vendedor", row[8]);
            productData.put("nombreVendedor", row[9]);
            productData.put("apellidoVendedor", row[10]);
            String imagenesConcatenadas = (String) row[11];
            if(imagenesConcatenadas == null){
                productData.put("imagenes", "Aun no cuenta con Imagenes");
            }else{
                List<String> listaImagenes = Arrays.asList(imagenesConcatenadas.split(","));
                productData.put("imagenes", listaImagenes);
            }
            productList.add(productData);
        }
        return productList;
    }

    @Override
    public Integer countProSel(String sellerEmail){
        return productRepository.countProSel(sellerEmail);
    }

    @Override
    public Product findOnlyById(Integer id){
        return productRepository.findOnlyById(id);
    }
}
