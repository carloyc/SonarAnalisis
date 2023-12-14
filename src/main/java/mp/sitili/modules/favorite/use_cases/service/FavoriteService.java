package mp.sitili.modules.favorite.use_cases.service;


import mp.sitili.modules.data_user.use_cases.service.DataUserService;
import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.favorite.use_cases.repository.IFavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FavoriteService implements IFavoriteRepository {

    private static final Logger LOGGER = Logger.getLogger(DataUserService.class.getName());

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public List<Map<String, Object>> favXusuario(String email) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = favoriteRepository.favXusuario(email);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + email, e);
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("fav_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("comentarios", row[3]);
            productData.put("calificacion", row[4]);
            productData.put("categoria", row[5]);
            String imagenesConcatenadas = (String) row[6];
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
    public Boolean deleteFav(String user_id, Integer fav_id){
        try{
            favoriteRepository.deleteFav(user_id, fav_id);
            return true;
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + user_id, e);
            return false;
        }
    }

    @Override
    public Favorite validarExis(Integer product_id, String user_id){
        return favoriteRepository.validarExis(product_id, user_id);
    }

    @Override
    public Favorite findById1(Integer id){
        return favoriteRepository.findById1(id);
    }
}
