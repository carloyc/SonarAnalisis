package mp.sitili.modules.image_product.adapters;

import mp.sitili.modules.image_product.entities.ImageProduct;
import mp.sitili.modules.image_product.use_cases.dto.ImageDTO;
import mp.sitili.modules.image_product.use_cases.dto.ImagesDTO;
import mp.sitili.modules.image_product.use_cases.service.ImageProductService;
import mp.sitili.modules.jwt.entities.JwtResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imageProduct")
public class ImageProductController {

    private final ImageProductService imageProductService;

    public ImageProductController(ImageProductService imageProductService) {
        this.imageProductService = imageProductService;
    }

    @DeleteMapping({"/delete"})
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<JwtResponse> borrarImagen(@RequestBody ImageDTO imageProduct){

        boolean revision = imageProductService.deleteImage(imageProduct.getImageUrl(), imageProduct.getId());

        if(revision){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
