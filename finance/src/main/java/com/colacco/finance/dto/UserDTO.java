package com.colacco.finance.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO (@NotBlank String username, @NotBlank String password) {}