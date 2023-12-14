package mp.sitili.modules.data_user.use_cases.dto;

import java.util.Date;

public interface DataUserDTO {
    Integer getId();
    String getCompany();
    String getFirst_name();
    String getLast_name();
    String getPhone();
    Date getRegister_date();
    String getUser_id();
    Boolean getStatus();
    String getRole_id();

}
