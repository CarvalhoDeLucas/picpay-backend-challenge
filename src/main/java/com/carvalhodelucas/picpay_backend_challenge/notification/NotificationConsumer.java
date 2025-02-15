package com.carvalhodelucas.picpay_backend_challenge.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.carvalhodelucas.picpay_backend_challenge.authorization.AuthorizerService;
import com.carvalhodelucas.picpay_backend_challenge.exception.NotificationException;
import com.carvalhodelucas.picpay_backend_challenge.transaction.Transaction;

@Service
public class NotificationConsumer {

    private RestClient restClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6").build();
    }

    // Para ser um metodo assíncrono
    @KafkaListener(topics = "transaction-notification", groupId = "picpay-desafio-backend")
    public void receiveNotification(Transaction transaction) {
        LOGGER.info("Notifying transaction: {}", transaction);

        var response = restClient.get().retrieve().toEntity(Notification.class);

        if (response.getStatusCode().isError() || !response.getBody().message()) {
            throw new NotificationException("Error sending notification!");
        }

        LOGGER.info("Notification has been sent {}...", response.getBody());
    }

}
