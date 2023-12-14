package mp.sitili.modules.address.use_cases.dto;

public interface AddressDTO {

    Integer getId();

    String getCountry();
    String getState();
    String getCity();
    Integer getPostalCode();
    String getMainAddress();
    String getStreetAddress1();
    String getStreetAddress2();
    String getDescription();
}
