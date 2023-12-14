package mp.sitili.modules.image_product.use_cases.methods;

import mp.sitili.modules.image_product.entities.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ImageProductRepository extends JpaRepository<ImageProduct, String> {

    @Modifying
    @Query(value = "INSERT INTO images_products (image_url, product_id) VALUES (:image_url, :product_id)", nativeQuery = true)
    void saveImgs(@Param("image_url") String image_url, @Param("product_id") Integer product_id);

    @Modifying
    @Query(value = "DELETE FROM images_products WHERE image_url = :images_products && product_id = :product_id",nativeQuery = true)
    void deleteImage(@Param("images_products") String images_products, @Param("product_id") Integer product_id);

}
