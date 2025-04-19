package org.Auth.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Auth.model.UserInfoDto;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoDto> {
    @Override
    public void configure(Map configs, boolean isKey) {}

    @Override
    public byte[] serialize(String args0, UserInfoDto args1) {
        byte[] retVal = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            retVal = objectMapper.writeValueAsString(args1).getBytes();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {}
}
