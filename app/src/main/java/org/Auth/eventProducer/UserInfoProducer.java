package org.Auth.eventProducer;


import org.Auth.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class UserInfoProducer {

    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String Topic_Name;

    @Autowired
    UserInfoProducer(KafkaTemplate<String,UserInfoDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoDto userInfoDto){
        Message<UserInfoDto> message = MessageBuilder.withPayload(userInfoDto)
                .setHeader(KafkaHeaders.TOPIC,Topic_Name)
                .build();
        kafkaTemplate.send(message);
    }

}
