package com.xchange.conversion;

import com.xchange.domain.Rate;
import com.xchange.domain.Rates;
import com.xchange.integration.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConversionServiceImpl implements ConversionService {

  private final CurrencyRateService currencyRateService;

  @Autowired
  public ConversionServiceImpl(CurrencyRateService currencyRateService) {
    this.currencyRateService = currencyRateService;
  }

  @Override
  public BigDecimal convert(String currency, String toCurrency, BigDecimal amount) {
    Rate rateResult = currencyRateService.getRate(currency, toCurrency);

    if (rateResult == null || rateResult.rate() == null) {
      return null;
    }

    var rate = rateResult.rate();

    return rate.multiply(amount);
  }

  @Override
  public Rates getCurrencyRates(String currency) {
    return currencyRateService.getRates(currency);
  }
}
