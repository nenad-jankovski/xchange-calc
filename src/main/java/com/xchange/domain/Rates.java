package com.xchange.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public record Rates(Map<String, BigDecimal> rates) implements Serializable {}
