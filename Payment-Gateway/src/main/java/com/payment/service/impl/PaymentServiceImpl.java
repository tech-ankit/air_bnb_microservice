package com.payment.service.impl;

import com.payment.entity.GiftCard;
import com.payment.payload.GiftCardRequestDto;
import com.payment.payload.PaymentRequestDto;
import com.payment.constant.PaymentConstant;
import com.payment.constant.TransactionType;
import com.payment.entity.Payment;
import com.payment.entity.Transaction;
import com.payment.repo.GiftCardRepository;
import com.payment.repo.PaymentRepo;
import com.payment.repo.TransactionRepository;
import com.payment.service.GiftCardService;
import com.payment.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private GiftCardService giftCardService;

    @Autowired
    private GiftCardRepository giftCardRepository;

    @Value("${razorpay.key_secret}")
    private String SECRET ;

    @Override
    public String bookingPayment(PaymentRequestDto paymentDto) {
        String receipt = "Booking_"+paymentDto.getBookingNumber().trim();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (paymentDto.getAmount() * 100)); // Convert to paise
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);
        orderRequest.put("receipt",receipt);
        Order order = null;
        try {
            order = razorpayClient.orders.create(orderRequest);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }

        //Create Payment Entity For Payment With Payment Status Pending
        Payment payment = new Payment();
        payment.setBookingNumber(paymentDto.getBookingNumber());
        payment.setOrderId(order.get("id"));
        payment.setPaymentStatus("PENDING");
        payment.setReceipt(receipt);
        paymentRepo.save(payment);

        return order.toString();
    }

    @Override
    @Transactional
    public String verifyPayment(Map<String, String> paymentData) {
        try {
            String paymentId = paymentData.get("payment_id");
            String orderId = paymentData.get("order_id");
            String signature = paymentData.get("signature");
            String bookingNumber = paymentData.get("booking_number");
            String receipt = paymentData.get("receipt");
            String userId = paymentData.get("user_id");
            String amt = paymentData.get("amount");
            String transactionType = paymentData.get("transaction_type");

            if (paymentId == null || orderId == null || signature == null || receipt == null) {
                return "Missing Payment Data! Check frontend request.";
            }

            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);
            options.put("razorpay_receipt", receipt);

            // Verify payment signature
            boolean isValid = Utils.verifyPaymentSignature(options, SECRET);
            if (isValid){
                if ("online_payment".equals(transactionType)){
                    Transaction transaction = new Transaction();
                    transaction.setPurchaseAmount(Double.valueOf(amt));
                    transaction.setUserId(userId);
                    transaction.setTransactionType(TransactionType.RAZOR_PAY);
                    transactionRepository.save(transaction);
                    return updatePaymentStatus(bookingNumber,orderId,receipt,paymentId);
                }
                if ("gift_card".equals(transactionType)){
                    Transaction transaction = new Transaction();
                    transaction.setPurchaseAmount(Double.valueOf(amt));
                    transaction.setUserId(userId);
                    transaction.setTransactionType(TransactionType.GIFT_CARD);
                    transactionRepository.save(transaction);
                    return giftCardService.updateGiftCard(bookingNumber,orderId,receipt,paymentId);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "Payment verification failed: " + e.getMessage();
        }
    }

    private String updatePaymentStatus(String bookingNumber, String orderId , String receipt , String paymentId){
        Optional<Payment> opPayment = paymentRepo.findByBookingNumberAndOrderIdAndReceipt(bookingNumber, orderId , receipt);

        if (opPayment.isPresent()){
            Payment payment = opPayment.get();
            payment.setPaymentStatus(PaymentConstant.SUCCESS);
            payment.setPaymentId(paymentId);
            payment.setReceipt(receipt);
            paymentRepo.save(payment);
            return paymentId;
        }else {
            return null;
        }
    }

    @Override
    public String buyGiftCard(GiftCardRequestDto giftCardRequestDto) {
        String receipt = giftCardRequestDto.getGiftCode().trim();
        giftCardRequestDto.setReceipt(receipt);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (giftCardRequestDto.getBalance() * 100)); // Convert to paise
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);
        orderRequest.put("receipt",receipt);
        Order order = null;
        try {
            order = razorpayClient.orders.create(orderRequest);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }

        //Create Gift Card Entity For Payment With Payment Status Pending
        giftCardRequestDto.setOrderId(order.get("id"));
        GiftCard giftCard = giftCardService.buyGiftCard(giftCardRequestDto);
        if (giftCard.getGiftCardId() == null){
            return null;
        }else {
            return order.toString();
        }
    }

}
