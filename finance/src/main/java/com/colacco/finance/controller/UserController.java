package com.colacco.finance.controller;

import com.colacco.finance.dto.IdDTO;
import com.colacco.finance.dto.UserDTO;
import com.colacco.finance.dto.UserOutputDTO;
import com.colacco.finance.dto.UserUpdateDTO;
import com.colacco.finance.models.User;
import com.colacco.finance.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping("/delete")
    @Transactional
    public void delete(@RequestBody @Valid IdDTO idDTO){
        User user = repository.getReferenceById(idDTO.id());
        user.delete();
    }

    @GetMapping("/list")
    public List<UserOutputDTO> listar(){
        return repository.findByActiveTrue().stream().map(UserOutputDTO::new).toList();
    }
}
