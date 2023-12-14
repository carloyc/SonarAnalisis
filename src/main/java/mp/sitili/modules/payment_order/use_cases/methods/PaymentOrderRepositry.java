package mp.sitili.modules.payment_order.use_cases.methods;

import mp.sitili.modules.payment_order.entities.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PaymentOrderRepositry extends JpaRepository<PaymentOrder, String> {
}
