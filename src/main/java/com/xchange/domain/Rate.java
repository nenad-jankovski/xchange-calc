package com.xchange.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public record Rate(String currency, BigDecimal rate) implements Serializable {}
