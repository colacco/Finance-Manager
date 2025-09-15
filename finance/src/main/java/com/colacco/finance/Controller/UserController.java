package com.colacco.finance.Controller;

import com.colacco.finance.DTO.UserDTO;
import com.colacco.finance.DTO.UserOutputDTO;
import com.colacco.finance.Models.User;
import com.colacco.finance.Repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
public class UserController {

    @Autowired
    public UserRepository repository;

    @PostMapping("/cadastrar")
    @Transactional
    public void register(@RequestBody @Valid UserDTO dados){
        repository.save(new User(dados));
    }

    @GetMapping("/usuarios")
    public List<UserOutputDTO> listar(){
        return repository.findAll().stream().map(UserOutputDTO::new).toList();
    }
}
