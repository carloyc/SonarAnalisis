package mp.sitili.modules.data_user.use_cases.dto;

import java.util.Date;

public interface DataUdto {

    Integer getId();
    String getCompany();
    String getFirstName();
    String getLastName();
    String getPhone();
    Date getRegisterDate();
    String getUserId();
    Boolean getStatus();
    String getRoleId();
}
