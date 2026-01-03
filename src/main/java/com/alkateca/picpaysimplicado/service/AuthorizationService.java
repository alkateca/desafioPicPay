package com.alkateca.picpaysimplicado.service;

import com.alkateca.picpaysimplicado.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthorizationService {

    private RestTemplate restTemplate;

    public boolean authorizeTransaction(User payer, Double value) {

        var response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            return "success".equals(response.getBody().get("status"));
        }

        return false;
    }

}
