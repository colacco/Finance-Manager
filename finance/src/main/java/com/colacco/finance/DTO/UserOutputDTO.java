package com.colacco.finance.DTO;

import com.colacco.finance.Models.User;

public record UserOutputDTO (Long id, String username, String password){
    public UserOutputDTO(User user) {
        this(user.getId(), user.getUsername(), user.getPassword());
    }
}