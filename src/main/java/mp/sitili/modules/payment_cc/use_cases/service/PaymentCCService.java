package mp.sitili.modules.payment_cc.use_cases.service;

import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_cc.use_cases.repository.IPaymentCCRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentCCService implements IPaymentCCRepository {

    private final PaymentCCRepository paymentCCRepository;

    public PaymentCCService(PaymentCCRepository paymentCCRepository) {
        this.paymentCCRepository = paymentCCRepository;
    }

    @Override
    public List<PaymentCC> pagoXusuario(String userEmail) {
        return paymentCCRepository.pagoXusuario(userEmail);
    }

    @Override
    public PaymentCC tarjetaXusuario(String userEmail){
        return paymentCCRepository.tarjetaXusuario(userEmail);
    }

    @Override
    public PaymentCC findById(Integer id){
        return paymentCCRepository.findById(id);
    }
}
