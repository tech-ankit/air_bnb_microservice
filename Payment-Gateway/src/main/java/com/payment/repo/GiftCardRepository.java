package com.payment.repo;

import com.payment.entity.GiftCard;
import com.payment.service.GiftCardService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, String> {
    Optional<GiftCard> findByGiftCode(String giftCode);

    Optional<GiftCard> findByGiftCodeAndOrderIdAndReceipt(String giftCode, String orderId, String receipt);
}