package com.jarkirana.kirana.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String paidedTo;
    LocalDate initiatedOn;
    LocalDate completedOn;

    String Type;
    BigDecimal amount;
    String currencyCode;

    public Transaction() {
    }

    /**
     * @param to
     * @param initiatedOn
     * @param completedOn
     * @param type
     * @param amount
     * @param currencyCode
     */
    public Transaction(String paidedTo, LocalDate initiatedOn, LocalDate completedOn, String type, BigDecimal amount,
            String currencyCode) {
        this.paidedTo = paidedTo;
        this.initiatedOn = initiatedOn;
        this.completedOn = completedOn;
        Type = type;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTo() {
        return paidedTo;
    }

    public void setTo(String paidedTo) {
        this.paidedTo = paidedTo;
    }

    public LocalDate getInitiatedOn() {
        return initiatedOn;
    }

    public void setInitiatedOn(LocalDate initiatedOn) {
        this.initiatedOn = initiatedOn;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(LocalDate completedOn) {
        this.completedOn = completedOn;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
