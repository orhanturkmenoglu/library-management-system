package com.library.module.payment.service.gateway;

import com.library.module.payment.dto.response.PaymentLinkResponse;
import com.library.module.payment.enums.PaymentType;
import com.library.module.payment.model.Payment;
import com.library.module.subscription.model.SubscriptionPlan;
import com.library.module.subscription.service.SubscriptionService;
import com.library.module.user.model.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class RazorpayService {

    private final SubscriptionService subscriptionService;
    @Value("${razorpay.key.id}")
    private String razorPayKeyId;

    @Value("${razorpay.secret.key}")
    private String razorPayKeySecret;

    @Value("${razorpay.callback.base-url:http://localhost:5173}")
    private String callbackBaseUrl;


    public PaymentLinkResponse createPaymentLink(User user, Payment payment) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorPayKeyId, razorPayKeySecret);

            Long amountInPaisa = payment.getAmount() * (new BigDecimal(100)).intValue();

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amountInPaisa);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", payment.getDescription());

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());

            if (user.getPhone() != null) {
                customer.put("phone", user.getPhone());
            }

            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            notify.put("sms", user.getPhone() != null);

            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);

            String successUrl = callbackBaseUrl + "/payment-success/" + payment.getId();

            paymentLinkRequest.put("success_url", successUrl);
            paymentLinkRequest.put("callback_method", "get");


            JSONObject notes = new JSONObject();
            notes.put("user_id", user.getId());
            notes.put("payment_id", payment.getId());

            if (payment.getPaymentType() == PaymentType.MEMBERSHIP) {
                notes.put("subscription_id", payment.getSubscription().getId());
                notes.put("plan", payment.getSubscription().getPlan().getPlanCode());
                notes.put("type", PaymentType.FINE);
            } else if (payment.getPaymentType() == PaymentType.FINE) {
                //notes.put("fine_id",payment.getFine().getId());
                notes.put("type", PaymentType.FINE);
            }

            paymentLinkRequest.put("notes", notes);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");

            PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();
            paymentLinkResponse.setPayment_link_url(paymentUrl);
            paymentLinkResponse.setPayment_link_id(paymentLinkId);


            return paymentLinkResponse;

        } catch (RazorpayException razorpayException) {
            log.error("RazorpayService::PaymentLinkResponse createPaymentLink{}", razorpayException.getMessage());
            throw new RuntimeException(razorpayException.getMessage());
        }
    }

    public JSONObject fetchPaymentDetails(String paymentId) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorPayKeyId, razorPayKeySecret);
            com.razorpay.Payment payment = razorpay.payments.fetch(paymentId);

            return payment.toJson();
        } catch (RazorpayException razorpayException) {
            throw new RuntimeException(razorpayException.getMessage());
        }
    }

    public boolean isValidPayment(String paymentId) {
        JSONObject paymentDetails = fetchPaymentDetails(paymentId);
        String status = paymentDetails.optString("status");
        BigDecimal amount = paymentDetails.optBigDecimal("amount", new BigDecimal(0));
        BigDecimal amountInRupees = amount.divide(new BigDecimal(100));

        JSONObject notes = paymentDetails.optJSONObject("notes");

        String paymentType = notes.optString("type");

        if (!"captured".equalsIgnoreCase(status)) {
            log.warn("Payment not captured.Current status : {}", status);
            return false;
        }

        if (paymentType.equals(PaymentType.MEMBERSHIP.toString())) {
            String planCode = notes.optString("plan");
            SubscriptionPlan plan = subscriptionService.getPlanByCode(planCode);

            return amountInRupees == plan.getPrice();
        }else if(paymentType.equals(PaymentType.FINE.toString())){
            Long fineId = notes.getLong("fine_id");
           /* Fine fine = fineRepository.findById(fineId)
                    .orElseThrow(()->new FineException("Fine not found "))*/
            /*return fine.getAmount == amountInRupees;*/
        }
        return false;
    }
}
