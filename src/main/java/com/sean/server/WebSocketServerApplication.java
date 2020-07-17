package com.sean.server;

import com.sean.server.endpoint.DataSocketEndpoint;
import com.sean.server.endpoint.BreezeDataSocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication(exclude = {
  ErrorMvcAutoConfiguration.class,
  GsonAutoConfiguration.class,
  MultipartAutoConfiguration.class,
  WebClientAutoConfiguration.class,
  WebSocketAutoConfiguration.class,
})
@Slf4j
@EnableAutoConfiguration
@EnableWebSocket
public class WebSocketServerApplication implements ApplicationRunner {

    @Value("${service.name}")
    private String serviceName;


    public static void main(String[] args) {
        SpringApplication.run(WebSocketServerApplication.class, args);
    }


    @Bean
    public DataSocketEndpoint dataSocketEndpoint() {
        return new DataSocketEndpoint();
    }

    @Bean
    public BreezeDataSocketEndpoint zeroDataSocketEndpoint() {
        return new BreezeDataSocketEndpoint();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("{} coming!", serviceName);

    }
}
