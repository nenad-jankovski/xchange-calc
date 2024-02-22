package com.xchange.api;

import com.xchange.conversion.ConversionService;
import java.math.BigDecimal;
import java.util.Optional;

import com.xchange.domain.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rest Service */
@RestController
@RequestMapping("/api")
public class RestService {

  private final ConversionService conversionService;

  @Autowired
  public RestService(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @GetMapping("/currencies/rates/convert/{fromCurrency}/{toCurrency}/{value}")
  public BigDecimal convert(
      @PathVariable String fromCurrency,
      @PathVariable String toCurrency,
      @PathVariable BigDecimal value) {
    return conversionService.convert(fromCurrency, toCurrency, value);
  }

  @GetMapping("/currencies/{currency}/rates")
  public Rates convert(@PathVariable Optional<String> currency) {
    return conversionService.getCurrencyRates(currency.orElse("EUR"));
  }
}
