package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        System.out.println("I am sending a message");

        HelloWorldMessage helloWorldMessage = HelloWorldMessage.builder()
                .id(UUID.randomUUID()).message("I am sending a message").build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, helloWorldMessage);

        System.out.println("Message Send!");
    }

    @Scheduled(fixedRate = 2000)
    public void sendReceiveMessage() throws JMSException {
        System.out.println("I am sending a message");

        HelloWorldMessage helloWorldMessage = HelloWorldMessage.builder()
                .id(UUID.randomUUID()).message("I am sending a message").build();

        Message receiveMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {
            Message message = null;
            try {
                message = session.createTextMessage(objectMapper.writeValueAsString(helloWorldMessage));
                message.setStringProperty("_type", "guru.springframework.sfgjms.model.HelloWorldMessage");
                return message;
            } catch (JsonProcessingException e) {
                throw new JMSException("jms exception" + e.getMessage());
            }
        });

        System.out.println(receiveMessage.getBody(String.class));
    }
}
