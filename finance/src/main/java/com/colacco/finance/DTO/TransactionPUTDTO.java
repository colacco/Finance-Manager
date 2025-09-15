package com.colacco.finance.DTO;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionPUTDTO (@NotNull Long id, @NotNull BigDecimal value){}
