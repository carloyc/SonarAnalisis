package mp.sitili.modules.data_user.use_cases.service;

import mp.sitili.modules.category.use_cases.service.CategoryService;
import mp.sitili.modules.data_user.use_cases.dto.UsuariosxMesDTO;
import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.data_user.use_cases.repository.IDataUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DataUserService implements IDataUserRepository {

    private static final Logger LOGGER = Logger.getLogger(DataUserService.class.getName());

    private final DataUserRepository dataUserRepository;

    public DataUserService(DataUserRepository dataUserRepository) {
        this.dataUserRepository = dataUserRepository;
    }

    @Override
    public boolean setCompany(String userEmail, String company, String phone){
        try {
            dataUserRepository.setCompany(userEmail, company, phone);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + userEmail, e);
            return false;
        }
    }

    @Override
    public boolean update(String company, String first_name, String last_name, String phone, Integer id, String user_id){
        try {
            dataUserRepository.update(company, first_name, last_name, phone, id, user_id);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la categoría con ID " + id, e);
            return false;
        }
    }

    @Override
    public List<UsuariosxMesDTO> usuXmes(){
        return dataUserRepository.usuXmes();
    }


}
