package com.jarkirana.kirana.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SearchFilter {

    public Map<String, Object> convertToMapAndFilterNulls(
            LocalDate initiateOn,
            String currency,
            BigDecimal amount,
            LocalDate completedOn,
            String type,
            String to) {
        Map<String, Object> paramMap = new HashMap<>();

        // Add non-null parameters to the map
        if (initiateOn != null) {
            paramMap.put("initiateOn", initiateOn);
        }

        if (currency != null) {
            paramMap.put("currency", currency);
        }

        if (amount != null) {
            paramMap.put("amount", amount);
        }

        if (completedOn != null) {
            paramMap.put("completedOn", completedOn);
        }

        if (type != null) {
            paramMap.put("type", type);
        }

        if (to != null) {
            paramMap.put("to", to);
        }

        // Filter out entries with null values
        paramMap.entrySet().removeIf(entry -> entry.getValue() == null);

        return paramMap;
    }

}
