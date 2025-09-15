package com.colacco.finance.Controller;

import com.colacco.finance.DTO.UserDTO;
import com.colacco.finance.DTO.UserOutputDTO;
import com.colacco.finance.DTO.UserUpdateDTO;
import com.colacco.finance.Models.User;
import com.colacco.finance.Repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserRepository repository;

    @PostMapping("/register")
    @Transactional
    public void register(@RequestBody @Valid UserDTO dados){
        repository.save(new User(dados));
    }

    @PutMapping("/update")
    @Transactional
    public void update(@RequestBody UserUpdateDTO data){
        User user = repository.getReferenceById(data.id());
        user.update(data);
    }

    @GetMapping("/list")
    public List<UserOutputDTO> listar(){
        return repository.findAll().stream().map(UserOutputDTO::new).toList();
    }
}
