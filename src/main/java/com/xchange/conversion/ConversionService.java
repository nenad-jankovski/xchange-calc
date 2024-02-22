package com.xchange.conversion;

import com.xchange.domain.Rates;

import java.math.BigDecimal;

public interface ConversionService {
  BigDecimal convert(String currency, String toCurrency, BigDecimal amount);

  Rates getCurrencyRates(String currency);
}
