package mp.sitili.modules.user.use_cases.repository;

import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;
import mp.sitili.modules.user.use_cases.dto.ValidSellerDTO;

import java.util.List;

public interface IUserRepository {

    String sendEmail(String username, String title, String bob);

    public List<SelectVendedorDTO> findSellers();

    public ValidSellerDTO validateCompany(String user_id);

}
