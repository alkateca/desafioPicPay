package com.alkateca.picpaysimplicado.service;

import com.alkateca.picpaysimplicado.dto.TransferDTO;
import com.alkateca.picpaysimplicado.models.TipoConta;
import com.alkateca.picpaysimplicado.models.User;
import com.alkateca.picpaysimplicado.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Boolean transfer (TransferDTO data) throws Exception {

        User payer = userService.findById(data.payer());
        User payee = userService.findById(data.payee());

        String menssagemDeErro = "";

        try {
            if(payer.getTipoConta() == TipoConta.LOJISTA){
                menssagemDeErro = "Tipo de conta inválido para essa transação" ;
            }
            if(payer.getSaldo() < data.value()){
                menssagemDeErro = "Saldo insuficiente";
            }

            authorizationService.authorizeTransaction(payer, data.value());

            notificationService.sendNotification(payee, "Débito efetuado no valor de " + data.value());

            payer.sacar(data.value());
            payee.depositar(data.value());

            userService.save(payer);
            userService.save(payee);

            return true;
        } catch (Exception e) {

            IO.println(menssagemDeErro);

            return  false;
        }
    }


}

