package com.xchange.integration;

import java.math.BigDecimal;
import java.util.Map;

public record CurrencyRateServiceResponse(Map<String, BigDecimal> data) {}
