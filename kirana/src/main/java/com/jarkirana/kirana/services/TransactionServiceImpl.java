package com.jarkirana.kirana.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
// import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarkirana.kirana.models.Rates;
import com.jarkirana.kirana.models.Transaction;
import com.jarkirana.kirana.repository.TransactionRepository;

import com.fasterxml.jackson.core.type.TypeReference;

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

    private static String readResponse(HttpURLConnection connection)

            throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private static Rates parseResponseBodyAsExchangeRates(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseBody, new TypeReference<Rates>() {
        });
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
                        if (transaction.getCurrencyCode() == "INR") {
                            total.add(transaction.getAmount());
                        } else {
                            String apiUrl = "https://api.fxratesapi.com/latest";
                            URL url = new URL(apiUrl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            Rates rates = null;
                            try {
                                connection.setRequestMethod("GET");
                                connection.setRequestProperty("Accept",

                                        "application/json");

                                int responseCode = connection.getResponseCode();
                                if (responseCode == 200) {
                                    String responseBody = readResponse(connection);
                                    rates = parseResponseBodyAsExchangeRates(responseBody);
                                } else {
                                    throw new IOException("API call failed with response code: " + responseCode);
                                }
                            } finally {
                                connection.disconnect();
                            }
                            if (rates.isSuccess()) {
                                BigDecimal dollarConversiontedAmount = new BigDecimal(
                                        transaction.getAmount().toString());
                                BigDecimal dollarConversionFactor = new BigDecimal(
                                        rates.getRates().get(transaction.getCurrencyCode()));
                                BigDecimal iNRConversionFactor = new BigDecimal(rates.getRates().get("INR"));
                                dollarConversiontedAmount.divide(dollarConversionFactor);
                                dollarConversiontedAmount.multiply(iNRConversionFactor);
                                total.add(dollarConversiontedAmount);
                            } else {
                                throw new Exception("Unable to get conversion rates");
                            }
                        }
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
