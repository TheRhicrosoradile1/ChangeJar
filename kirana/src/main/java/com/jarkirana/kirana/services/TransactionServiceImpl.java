package com.jarkirana.kirana.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarkirana.kirana.models.Transaction;
import com.jarkirana.kirana.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction addTransaction(Transaction transaction) throws Exception {
        // To add a new transaction for the kirana store
        try {
            transactionRepository.save(transaction);
            return transaction;
        } catch (Exception e) {
            throw new Exception("Unable to save transaction " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> getTransactionList() {
        // To get all the transactions for the store
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionListWithFilter(Map<String, Object> searchCriteria) {
        return null;

        // return transactionRepository.getTransactionListWithFilter(null);
    }

    @Override
    public void removeTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) throws Exception {
        // To update an transaction for the kirana store
        try {
            if (transactionRepository.findById(transaction.getId()) != null) {
                transactionRepository.save(transaction);
            } else {
                throw new UnsupportedOperationException(
                        "Cannot Update a non-existing entry add first (/addTransation) to update");
            }
            return transaction;
        } catch (Exception e) {
            throw new Exception("Unable to save transaction " + e.getMessage());
        }
    }

    @Override
    public BigDecimal getTotalTransactionsByType(LocalDate date, String type) throws Exception {
        try {

            BigDecimal total = new BigDecimal("0");
            BigDecimal zero = new BigDecimal("0");
            List<Transaction> transactionList = transactionRepository.findByDate(date);
            if (transactionList != null && !transactionList.isEmpty()) {
                for (Transaction transaction : transactionList) {
                    BigDecimal amount = transaction.getAmount();
                    if (transaction.getInitiatedOn().equals(date)
                            && ((amount.compareTo(zero) < 0 && type == "DEBIT")
                                    || (amount.compareTo(zero) > 0 && type == "CREDIT"))) {
                        total.add(transaction.getAmount());
                    }
                }
                if (type == "DEBIT") {
                    return total.multiply(new BigDecimal(-1));
                } else
                    return total;

            }
        } catch (Exception e) {
            throw new Exception("Unable to get transaction " + e.getMessage());
        }
        return null;

    }

}
