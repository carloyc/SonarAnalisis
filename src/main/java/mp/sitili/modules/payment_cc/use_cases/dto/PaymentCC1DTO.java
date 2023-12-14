package mp.sitili.modules.payment_cc.use_cases.dto;

public interface PaymentCC1DTO {

    String getCc();
    String getCvv();
    String getExpiryDate();

    Integer getId();

    String getMonth();
    String getYear();


}
