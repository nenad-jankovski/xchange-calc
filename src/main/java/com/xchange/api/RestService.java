package com.xchange.api;

import com.xchange.conversion.ConversionService;
import java.math.BigDecimal;
import java.util.Optional;

import com.xchange.domain.Rates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rest Service */
@RestController
@RequestMapping("/api/currencies")
public class RestService {

  private final ConversionService conversionService;

  @Autowired
  public RestService(ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @GetMapping("/rates/convert/{fromCurrency}/{toCurrency}/{value}")
  public ResponseEntity<BigDecimal> convert(
      @PathVariable String fromCurrency,
      @PathVariable String toCurrency,
      @PathVariable BigDecimal value) {
    return ResponseEntity.ok(conversionService.convert(fromCurrency, toCurrency, value));
  }

  @GetMapping("/{currency}/rates")
  public ResponseEntity<Rates> convert(@PathVariable Optional<String> currency) {
    return ResponseEntity.ok(conversionService.getCurrencyRates(currency.orElse("EUR")));
  }
}
