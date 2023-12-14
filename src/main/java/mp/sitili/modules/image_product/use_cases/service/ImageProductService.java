package mp.sitili.modules.image_product.use_cases.service;

import mp.sitili.modules.data_user.use_cases.service.DataUserService;
import mp.sitili.modules.image_product.use_cases.methods.ImageProductRepository;
import mp.sitili.modules.image_product.use_cases.repository.IImageProductRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ImageProductService implements IImageProductRepository {

    private static final Logger LOGGER = Logger.getLogger(ImageProductService.class.getName());

    private final ImageProductRepository imageProductRepository;

    public ImageProductService(ImageProductRepository imageProductRepository) {
        this.imageProductRepository = imageProductRepository;
    }

    @Override
    public boolean saveImgs(String image_url, Integer product_id) {
        try {
            imageProductRepository.saveImgs(image_url, product_id);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " , e);
            return false;
        }
    }

    @Override
    public boolean deleteImage(String image_url, Integer produc_id) {
        try {
            imageProductRepository.deleteImage(image_url, produc_id);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " , e);
            return false;
        }
    }

}
