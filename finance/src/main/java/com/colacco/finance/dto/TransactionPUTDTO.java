package com.colacco.finance.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionPUTDTO (@NotNull Long id, @NotNull BigDecimal value){}
