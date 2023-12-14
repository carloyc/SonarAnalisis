package mp.sitili.modules.address.use_cases.service;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.methods.AddressRepository;
import mp.sitili.modules.address.use_cases.repository.IAddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressRepository {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address dirActXusuario(String user_id){
        return addressRepository.dirActXusuario(user_id);
    }

    @Override
    public Address buscarDir(Integer id, String user_id){
        return addressRepository.buscarDir(id, user_id);
    }

    @Override
    public Boolean deleteAddress(Integer id, String user_id){
        try{
            addressRepository.deleteAddress(id, user_id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
