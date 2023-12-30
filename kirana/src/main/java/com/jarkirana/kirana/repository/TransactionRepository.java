package com.jarkirana.kirana.repository;

import com.jarkirana.kirana.models.Transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// import java.time.LocalDate;
// import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
    @Query("SELECT e FROM Transaction e WHERE e.initiatedOn = :date")
    List<Transaction> findByDate(@Param("date") LocalDate date);

}
