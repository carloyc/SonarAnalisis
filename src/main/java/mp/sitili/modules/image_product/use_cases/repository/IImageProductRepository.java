package mp.sitili.modules.image_product.use_cases.repository;

public interface IImageProductRepository {

    boolean saveImgs(String image_url, Integer product_id);

    boolean deleteImage(String image_url, Integer product_id);

}
