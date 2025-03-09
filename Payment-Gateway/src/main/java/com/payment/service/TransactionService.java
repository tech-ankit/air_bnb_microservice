package com.payment.service;

import com.payment.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction getTransactionById(String transactionId);
    List<Transaction> gelAllTransactionByUser(String userId);
}
