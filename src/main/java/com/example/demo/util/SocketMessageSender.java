package com.example.demo.util;

import com.example.demo.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SocketMessageSender {
    public static void sendSocketMessage(Jedis client, List<String> rooms, String event, Object data) {
        try {
            // Create the message structure
            Map<String, Object> message = Map.of(
                    "nsp", "/",
                    "type", 2,
                    "data", List.of(event, data)
            );
            Map<String, Object> roomMessage = Map.of(
                    "rooms", rooms
            );

            // Combine message and room message
            List<Object> msg = List.of(999, message, roomMessage);

            // Serialize the message with JSON
            byte[] sendMessage = serializeMessageToJson(msg);

            // Publish the message to the Redis channel
            String channelName = "socket.io#/#";
            client.publish(channelName, new String(sendMessage));

        } catch (Exception e) {
            System.err.println("Failed to send socket message: " + e.getMessage());
        }
    }

    private static byte[] serializeMessageToJson(List<Object> msg) throws Exception {
        // Create an ObjectMapper to convert the object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsBytes(msg);
    }

}
