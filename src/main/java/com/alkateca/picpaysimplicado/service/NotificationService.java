package com.alkateca.picpaysimplicado.service;
import com.alkateca.picpaysimplicado.dto.NotificationDTO;
import com.alkateca.picpaysimplicado.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private RestTemplate restTemplate;

    public void sendNotification(User payee, String message) throws Exception {

        String email = payee.getEmail();

        NotificationDTO notificationDTO = new NotificationDTO(message, email);

        try {
            ResponseEntity<String> notificationResponse = restTemplate.postForEntity(
                    "https://util.devi.tools/api/v1/notify",
                    notificationDTO,
                    String.class);

            if (notificationResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println(notificationResponse.getBody());
                throw new Exception("Erro ao enviar notificação");
            }

            System.out.println("Notificação enviada com sucessos para " + email);
        } catch (Exception e) {

            throw new Exception("Serviço de notificação indisponível");

        }

    }

}
