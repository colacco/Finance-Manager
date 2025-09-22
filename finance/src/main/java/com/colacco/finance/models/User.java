package com.colacco.finance.models;

import com.colacco.finance.dto.UserDTO;
import com.colacco.finance.dto.UserUpdateDTO;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transacaoList = new ArrayList<>();

    private boolean active;

    public User(UserDTO dados) {
        this.username = dados.username();
        this.password = dados.password();
        this.active = true;
    }

    public void update(UserUpdateDTO data) {
        if (data.username() != null){
            this.username = data.username();
        }

        if (data.password() != null){
            this.password = data.password();
        }
    }

    public void delete() {
        this.active = false;
    }
}