package com.payment.service.impl;

import com.payment.constant.PaymentConstant;
import com.payment.constant.TransactionType;
import com.payment.entity.GiftCard;
import com.payment.entity.Transaction;
import com.payment.payload.GiftCardPurchaseDto;
import com.payment.payload.GiftCardRequestDto;
import com.payment.repo.GiftCardRepository;
import com.payment.repo.TransactionRepository;
import com.payment.service.GiftCardService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class GiftCardServiceImpl implements GiftCardService {

    @Autowired
    private GiftCardRepository giftCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public GiftCard buyGiftCard(GiftCardRequestDto requestDto) {
        GiftCard giftCard = new GiftCard();
        BeanUtils.copyProperties(requestDto , giftCard);
        giftCard.setGiftCardStatus(PaymentConstant.PENDING);
        return giftCardRepository.save(giftCard);
    }

    @Override
    @Transactional
    public Boolean purchaseGiftCard(GiftCardPurchaseDto giftCardPurchaseDto) {
        Optional<GiftCard> opGiftCard = giftCardRepository.findByGiftCode(giftCardPurchaseDto.getGiftCode());
        if (opGiftCard.isPresent()){
            GiftCard giftCard = opGiftCard.get();
            if (giftCard.getBalance() >= giftCardPurchaseDto.getAmount()){
                giftCard.setBalance(giftCard.getBalance()-giftCardPurchaseDto.getAmount());
                giftCardRepository.save(giftCard);

                //Transaction Save
                Transaction transaction = new Transaction();
                transaction.setTransactionType(TransactionType.GIFT_CARD);
                transaction.setPurchaseAmount(giftCardPurchaseDto.getAmount());
                transaction.setUserId(giftCardPurchaseDto.getUserId());
                transactionRepository.save(transaction);
                return true;
            }
        }
        return false;
    }

    @Override
    public String updateGiftCard(String giftCode, String orderId, String receipt,String paymentId) {
        Optional<GiftCard> opGiftCard = giftCardRepository.findByGiftCodeAndOrderIdAndReceipt(giftCode ,orderId , receipt );
        if (opGiftCard.isPresent()){
            GiftCard giftCard = opGiftCard.get();
            giftCard.setGiftCardStatus("ACTIVE");
            giftCard.setPaymentId(paymentId);
            giftCard.setExpiry(LocalDate.now().plusMonths(6));
            GiftCard saved = giftCardRepository.save(giftCard);
            return saved.getGiftCardId();
        }else {
            return null;
        }
    }


}
