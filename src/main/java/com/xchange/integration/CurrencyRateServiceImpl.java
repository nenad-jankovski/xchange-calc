package com.xchange.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xchange.domain.Rate;
import com.xchange.domain.Rates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

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
    var rates = parseResponse(response);

    var result =
        rates.entrySet().stream().map(rate -> new Rate(rate.getKey(), rate.getValue())).findFirst();

    return result.orElse(null);
  }

  @Override
  public Rates getRates(String currency) {
    var parameters = new HashMap<String, String>(1);
    parameters.put("base", currency);

    var response = queryExternalService(parameters);
    return new Rates(parseResponse(response));
  }

  private Map<String, BigDecimal> parseResponse(HttpEntity<String> response) {
    var mapper = new ObjectMapper();
    try {
      var root = mapper.readTree(response.getBody());
      var rates = root.path("data");
      var properties = mapper.readValue(rates.toString(), Properties.class);

      return properties.entrySet().stream()
          .collect(
              Collectors.toMap(
                  e -> (String) e.getKey(), e -> new BigDecimal(e.getValue().toString())));

    } catch (JsonProcessingException e) {
      log.error("Error parsing rates response", e);
    }
    return Collections.emptyMap();
  }

  private HttpEntity<String> queryExternalService(Map<String, String> parameters) {
    var headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    var entity = new HttpEntity<>(headers);

    var builder = UriComponentsBuilder.fromHttpUrl(apiUrl);
    builder.queryParam("apikey", apiKey);

    parameters.forEach(builder::queryParam);

    return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
  }
}
