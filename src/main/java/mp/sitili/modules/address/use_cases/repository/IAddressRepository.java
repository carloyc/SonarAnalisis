package mp.sitili.modules.address.use_cases.repository;

import mp.sitili.modules.address.entities.Address;

public interface IAddressRepository {

     Address dirActXusuario(String user_id);

     Boolean deleteAddress(Integer id, String user_id);
     Address buscarDir(Integer id, String user_id);

}
