package com.jarkirana.kirana.controller;

import com.jarkirana.kirana.models.Transaction;
import com.jarkirana.kirana.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Provider.Service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    // Record a transaction
    /**
     * @param intiateOn
     * @param currency
     * @param amount
     * @param completedOn
     * @param Type
     * @param to
     * @throws Exception
     */
    @PostMapping
    public void recordTransaction(@RequestBody LocalDate intiateOn,
            @RequestBody String currency,
            @RequestBody BigDecimal amount,
            @RequestBody LocalDate completedOn,
            @RequestBody String Type,
            @RequestBody String to) throws Exception {
        transactionService.addTransaction(new Transaction(to, completedOn, completedOn, to, amount, to));
    }

    @GetMapping
    public List<Transaction> getAllTransactionList() {
        return transactionService.getTransactionList();
    }

    // Get transactions for a specific date
    /**
     * @param completedOn
     * @param initiatedOn
     * @param amount
     * @param to
     * @param type
     * @return
     */
    @GetMapping("/filter/completedOn{completedOn}/initiatedOn{initiatedOn}/to/{to}/amount/{amount}")
    public List<Transaction> getTransactionsWithFilter(@PathVariable LocalDate completedOn,
            @PathVariable LocalDate initiatedOn, @PathVariable BigDecimal amount, @PathVariable String to,
            @PathVariable String type) {

        return transactionService.getTransactionListWithFilter(null);
    }

    /**
     * @param intiateOn
     * @param currency
     * @param amount
     * @param completedOn
     * @param Type
     * @param to
     * @throws Exception
     */
    @DeleteMapping
    public void deleteTransaction(@RequestBody LocalDate intiateOn,
            @RequestBody String currency,
            @RequestBody BigDecimal amount,
            @RequestBody LocalDate completedOn,
            @RequestBody String Type,
            @RequestBody String to) throws Exception {

        transactionService.removeTransaction(new Transaction(to, completedOn, completedOn, to, amount, to));
    }

    @GetMapping("/date/{date}/type/{type}")
    public BigDecimal getDailyAmountByType(@RequestParam LocalDate date, String type) throws Exception {
        if (type.length() == 0) {
            throw new Exception("Type neccessary between CREDIT or DEBIT");
        }
        return transactionService.getTotalTransactionsByType(date, type);
    }

}
