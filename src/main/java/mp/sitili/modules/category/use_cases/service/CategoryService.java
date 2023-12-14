package mp.sitili.modules.category.use_cases.service;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.dto.ProductosxCategoriaDTO;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.category.use_cases.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CategoryService implements ICategoryRepository {

    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllStatus() {
        return this.categoryRepository.findAllStatus();
    }

    @Override
    public List<Category> categoriasNombre(String name) {
        return this.categoryRepository.categoriasNombre(name);
    }


    @Override
    public boolean getCategoryById(int id) {
        int revision = categoryRepository.getCategoryById(id);
        if(revision != 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean getStatusCategory(int id) {
        boolean revision = this.categoryRepository.getStatusCategory(id);
        return revision;
    }

    @Override
    public void deleteCategory(int id, boolean status) {
        try {
            categoryRepository.deleteCategory(id, status);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + id, e);
        }
    }

    @Override
    public boolean updateCategory(int id, String name) {
        try {
            categoryRepository.updateCategory(id, name);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + id, e);
            return false;
        }
    }

    @Override
    public List<ProductosxCategoriaDTO> proXcat(){
        return categoryRepository.proXcat();
    }

    @Override
    public List<ProductosxCategoriaDTO> proXcatSell(String sellerEmail){
        return categoryRepository.proXcatSell(sellerEmail);
    }

    @Override
    public List<Map<String, Object>> findAllProducts(Integer id) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = categoryRepository.findAllProducts(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + id, e);
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
}
