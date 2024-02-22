package com.xchange.integration;

import com.xchange.domain.Rate;
import com.xchange.domain.Rates;
import org.springframework.cache.annotation.Cacheable;

public interface CurrencyRateService {

  @Cacheable(value = "rates", key = "{#currency}")
  Rates getRates(String currency);

  @Cacheable(value = "rates", key = "{#fromCurrency, #toCurrency}")
  Rate getRate(String fromCurrency, String toCurrency);
}
