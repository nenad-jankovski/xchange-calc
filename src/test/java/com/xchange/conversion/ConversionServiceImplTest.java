package com.xchange.conversion;

import static org.junit.jupiter.api.Assertions.*;

import com.xchange.domain.Rate;
import com.xchange.domain.Rates;
import com.xchange.integration.CurrencyRateService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConversionServiceImplTest {

  private static ConversionService conversionService;

  @BeforeAll
  public static void init() {
    conversionService = new ConversionServiceImpl(new FakeCurrencyRateService());
  }

  @Test
  @DisplayName("Convert from one currency to other")
  void convert_form_currency_to_currency() {
    var convertedValue = conversionService.convert("EUR", "HRK", BigDecimal.TEN);
    Assertions.assertThat(convertedValue).isNotNull().isEqualTo(BigDecimal.valueOf(67.615658363));

    convertedValue = conversionService.convert("EUR", "HRK", BigDecimal.valueOf(15d));
    Assertions.assertThat(convertedValue).isNotNull().isEqualTo(BigDecimal.valueOf(101.4234875445));

    convertedValue = conversionService.convert("EUR", "AUD", BigDecimal.valueOf(15d));
    Assertions.assertThat(convertedValue).isNull();
  }

  @Test
  @DisplayName("Get currencies conversion rates for EUR")
  void getCurrencyRates_when_currency_EUR() {
    Rates rates = conversionService.getCurrencyRates("EUR");
    Objects.requireNonNull(rates, "Rates cannot be null");
    assertAll(
        () -> Assertions.assertThat(rates).isNotNull(),
        () -> Assertions.assertThat(rates.rates()).isNotNull(),
        () -> Assertions.assertThat(rates.rates()).hasSize(5),
        () -> Assertions.assertThat(rates.rates().get("ZAR")).isNotNull(),
        () -> Assertions.assertThat(rates.rates().get("ILS")).isNull());
  }

  @Test
  @DisplayName("Get currencies conversion rates for CHF")
  void getCurrencyRates_when_currency_CHF() {
    Rates rates = conversionService.getCurrencyRates("CHF");
    Objects.requireNonNull(rates, "Rates cannot be null");
    assertAll(
        () -> Assertions.assertThat(rates).isNotNull(),
        () -> Assertions.assertThat(rates.rates()).isNotNull(),
        () -> Assertions.assertThat(rates.rates()).hasSize(5),
        () -> Assertions.assertThat(rates.rates().get("AUD")).isNotNull(),
        () -> Assertions.assertThat(rates.rates().get("MXN")).isNull());
  }

  /** Create fake Currency rate service for testing */
  private static class FakeCurrencyRateService implements CurrencyRateService {

    @Override
    public Rates getRates(String currency) {
      Map<String, BigDecimal> cannedResponse = new HashMap<>(5);
      if ("EUR".equals(currency)) {
        cannedResponse.put("HRK", new BigDecimal("6.7615658363"));
        cannedResponse.put("CHF", new BigDecimal("0.9385231317"));
        cannedResponse.put("MXN", new BigDecimal("22.0665480427"));
        cannedResponse.put("ZAR", new BigDecimal("16.4098754448"));
        cannedResponse.put("INR", new BigDecimal("74.2597864769"));
      }

      if ("CHF".equals(currency)) {
        cannedResponse.put("THB", new BigDecimal("31.6601423488"));
        cannedResponse.put("CNY", new BigDecimal("7.0175266904"));
        cannedResponse.put("AUD", new BigDecimal("1.5724199288"));
        cannedResponse.put("ILS", new BigDecimal("3.6395907473"));
        cannedResponse.put("INR", new BigDecimal("74.2597864769"));
      }
      return new Rates(cannedResponse);
    }

    @Override
    public Rate getRate(String fromCurrency, String toCurrency) {
      var toCurrencyRate = this.getRates(fromCurrency).rates().get(toCurrency);

      return new Rate(toCurrency, toCurrencyRate);
    }
  }
}
