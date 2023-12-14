package mp.sitili.modules.payment_order.use_cases.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import mp.sitili.modules.payment_order.use_cases.http.PaymentIntentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.key.secret}")
    String secretKey;

    public List<PaymentIntent> paymentIntents(List<PaymentIntentDto> paymentIntentDtoList) throws StripeException {
        Stripe.apiKey = secretKey;
        List<PaymentIntent> paymentIntents = new ArrayList<>();

        for (PaymentIntentDto paymentIntentDto : paymentIntentDtoList) {
            List<String> paymentMethodTypes = new ArrayList<>();
            paymentMethodTypes.add("card");
            Map<String, Object> params = new HashMap<>();
            params.put("amount", paymentIntentDto.getAmount());
            params.put("currency", paymentIntentDto.getCurrency());
            params.put("description", paymentIntentDto.getDescription());
            params.put("payment_method_types", paymentMethodTypes);

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            paymentIntents.add(paymentIntent);
        }

        return paymentIntents;
    }


    public List<PaymentIntent> confirmPaymentIntents(List<String> paymentIntentIds) throws StripeException {
        Stripe.apiKey = secretKey;
        List<PaymentIntent> confirmedPaymentIntents = new ArrayList<>();

        for (String paymentIntentId : paymentIntentIds) {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method", "pm_card_visa");
            paymentIntent.confirm(params);
            confirmedPaymentIntents.add(paymentIntent);
        }

        return confirmedPaymentIntents;
    }

    public List<PaymentIntent> cancelPaymentIntents(List<String> paymentIntentIds) throws StripeException {
        Stripe.apiKey = secretKey;
        List<PaymentIntent> cancelledPaymentIntents = new ArrayList<>();
    
        for (String paymentIntentId : paymentIntentIds) {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent.cancel();
            cancelledPaymentIntents.add(paymentIntent);
        }
    
        return cancelledPaymentIntents;
    }

}
