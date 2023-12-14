package mp.sitili.modules.order.use_cases.dto;


import java.sql.Timestamp;

public interface OrdersDTO {

    Integer getId();
    Timestamp getDate_order();
    String getRepartidor();
    String getStatus();

}
