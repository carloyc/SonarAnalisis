package mp.sitili.modules.product.use_cases.repository;

import mp.sitili.modules.product.entities.Product;
import java.util.List;
import java.util.Map;

public interface IProductRepository {

     List<Map<String, Object>> findAllProducts();

     List<Map<String, Object>> findProduct(Integer product_id);

     List<Map<String, Object>> findAllxVend(String email);

     Integer countProSel(String sellerEmail);

     Product findOnlyById(Integer id);

}
