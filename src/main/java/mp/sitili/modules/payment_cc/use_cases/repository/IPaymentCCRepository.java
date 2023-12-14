package mp.sitili.modules.payment_cc.use_cases.repository;

import mp.sitili.modules.payment_cc.entities.PaymentCC;
import java.util.List;

public interface IPaymentCCRepository {

     List<PaymentCC> pagoXusuario(String userEmail);
     PaymentCC tarjetaXusuario(String userEmail);
     PaymentCC findById(Integer id);

}
