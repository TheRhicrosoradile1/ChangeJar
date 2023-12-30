package com.jarkirana.kirana.services;

import com.jarkirana.kirana.models.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.cglib.core.Local;

public interface TransactionService {

    public Transaction addTransaction(Transaction transaction) throws Exception;

    public List<Transaction> getTransactionList();

    public List<Transaction> getTransactionListWithFilter(Map<String, Object> searchCriteria);

    public void removeTransaction(Transaction transaction);

    public Transaction updateTransaction(Transaction transaction) throws Exception;

    public BigDecimal getTotalTransactionsByType(LocalDate date, String type) throws Exception;
}
