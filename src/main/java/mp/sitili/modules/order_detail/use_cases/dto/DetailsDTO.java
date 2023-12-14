package mp.sitili.modules.order_detail.use_cases.dto;

public interface DetailsDTO {

    Integer getQuantity();
    Double getPrice();
    String getName();
    Double getTotal();
}
