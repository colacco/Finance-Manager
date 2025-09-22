package com.colacco.finance.dto;

import com.colacco.finance.models.User;

public record UserOutputDTO (Long id, String username, String password){
    public UserOutputDTO(User user) {
        this(user.getId(), user.getUsername(), user.getPassword());
    }
}