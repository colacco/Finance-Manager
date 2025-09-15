package com.colacco.finance.DTO;

import jakarta.validation.constraints.NotBlank;

public record UserDTO (@NotBlank String username, @NotBlank String password) {}