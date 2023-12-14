package mp.sitili.modules.raiting.use_cases.service;

import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.raiting.use_cases.repository.IRaitingRepository;
import org.springframework.stereotype.Service;

@Service
public class RaitingService implements IRaitingRepository {

    private final RaitingRepository raitingRepository;

    public RaitingService(RaitingRepository raitingRepository) {
        this.raitingRepository = raitingRepository;
    }

    @Override
    public Integer cal4(String sellerEmail){
        return raitingRepository.cal4(sellerEmail);
    }
}
