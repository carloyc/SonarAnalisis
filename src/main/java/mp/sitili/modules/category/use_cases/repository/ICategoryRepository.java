package mp.sitili.modules.category.use_cases.repository;


import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.dto.ProductosxCategoriaDTO;
import java.util.List;
import java.util.Map;

public interface ICategoryRepository {

    List<Category> findAllStatus();

    List<Category> categoriasNombre(String name);

    boolean getCategoryById(int id);

    boolean getStatusCategory(int id);

    void deleteCategory(int id, boolean status);

    boolean updateCategory(int id, String name);

     List<ProductosxCategoriaDTO> proXcat();

     List<ProductosxCategoriaDTO> proXcatSell(String sellerEmail);

     List<Map<String, Object>> findAllProducts(Integer id);
}
