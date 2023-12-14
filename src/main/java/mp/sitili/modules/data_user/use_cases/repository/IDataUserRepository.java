package mp.sitili.modules.data_user.use_cases.repository;

import mp.sitili.modules.data_user.use_cases.dto.UsuariosxMesDTO;
import java.util.List;

public interface IDataUserRepository {

     boolean setCompany(String userEmail, String company, String phone);

     boolean update(String company, String first_name, String last_name, String phone, Integer id, String user_id);

     List<UsuariosxMesDTO> usuXmes();

}
