package com.xchange.integration;

import com.xchange.domain.Rate;
import com.xchange.domain.Rates;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class CurrencyRateServiceImpl implements CurrencyRateService {
  private final RestTemplate restTemplate;
  private final String apiUrl;
  private final String apiKey;

  @Autowired
  public CurrencyRateServiceImpl(
      RestTemplate restTemplate,
      @Value("${conversion.api.url}") String apiUrl,
      @Value("${conversion.api.apikey}") String apiKey) {
    this.restTemplate = restTemplate;
    this.apiUrl = apiUrl;
    this.apiKey = apiKey;
  }

  @Override
  public Rate getRate(String fromCurrency, String toCurrency) {
    var parameters = new HashMap<String, String>(2);
    parameters.put("base_currency", fromCurrency);
    parameters.put("currencies", toCurrency);

    var response = queryExternalService(parameters);

    var result =
        Objects.requireNonNull(response.getBody()).data().entrySet().stream()
            .map(rate -> new Rate(rate.getKey(), rate.getValue()))
            .findFirst();

    return result.orElse(null);
  }

  @Override
  public Rates getRates(String currency) {
    var parameters = new HashMap<String, String>(1);
    parameters.put("base", currency);

    var response = queryExternalService(parameters);
    return new Rates(Objects.requireNonNull(response.getBody()).data());
  }

  private ResponseEntity<CurrencyRateServiceResponse> queryExternalService(
      Map<String, String> parameters) {
    var headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    var entity = new HttpEntity<>(headers);

    var builder = UriComponentsBuilder.fromHttpUrl(apiUrl);
    builder.queryParam("apikey", apiKey);

    parameters.forEach(builder::queryParam);

    return restTemplate.exchange(
        builder.toUriString(), HttpMethod.GET, entity, CurrencyRateServiceResponse.class);
  }
}
