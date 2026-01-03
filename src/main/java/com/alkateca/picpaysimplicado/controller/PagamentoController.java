package com.alkateca.picpaysimplicado.controller;

import com.alkateca.picpaysimplicado.dto.TransferDTO;
import com.alkateca.picpaysimplicado.models.TipoConta;
import com.alkateca.picpaysimplicado.models.User;
import com.alkateca.picpaysimplicado.repository.UserRepository;
import com.alkateca.picpaysimplicado.service.TransferService;
import com.alkateca.picpaysimplicado.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PagamentoController {

    @Autowired
    UserService userService;

    @Autowired
    TransferService transferService;



    @PostMapping("/user")
    public ResponseEntity<User> save(@RequestBody User user) {

        userService.save(user);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/transfer")
    @Transactional
    public ResponseEntity<String> transferencia(@RequestBody TransferDTO data) throws Exception {

        if(transferService.transfer(data) == true ){
            return ResponseEntity.ok("Transferência realizada");
        };

        return ResponseEntity.badRequest().build();

    }

}
