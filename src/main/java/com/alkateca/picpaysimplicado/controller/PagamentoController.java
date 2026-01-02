package com.alkateca.picpaysimplicado.controller;

import com.alkateca.picpaysimplicado.dto.TransferDTO;
import com.alkateca.picpaysimplicado.models.TipoConta;
import com.alkateca.picpaysimplicado.models.User;
import com.alkateca.picpaysimplicado.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PagamentoController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity<User> newUser(@RequestBody User user) {

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferencia(@RequestBody TransferDTO data){


        User payer = userRepository.findById(data.payer()).orElseThrow(() -> new RuntimeException("Pagador não encontrado"));
        User payee = userRepository.findById(data.payee()).orElseThrow(() -> new RuntimeException("Recebedor não encontrado"));


        if(payer.getTipoConta() == TipoConta.LOJISTA){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tipo de conta inválido para essa transação");
        }
        if(payer.getSaldo() < data.value()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Saldo insuficiente");
        }

        payer.sacar(data.value());
        payee.depositar(data.value());

        userRepository.save(payer);
        userRepository.save(payee);

        return ResponseEntity.ok("Transferência realizada");

    }

}
