package mp.sitili.modules.shopping_car.use_cases.repository;

import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import java.util.List;
import java.util.Map;

public interface IShoppingCarRepository {

     List<Map<String, Object>> carXusuario(String email);

     List<Map<String, Object>> carXusuariob(String email);

     Boolean deleteCar(String user_id, Integer product_id);

     ShoppingCar validarExis(Integer product_id, String user_id);

     ShoppingCar findById1(Integer id);

     Boolean updateCar(Integer car_id, Integer quantity);


}
