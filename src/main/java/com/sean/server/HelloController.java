package com.sean.server;

import com.sean.server.client.MockRawDataService;
import com.sean.server.endpoint.BreezeDataSocketEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class HelloController {
    @Resource
    private BreezeDataSocketEndpoint endpoint;

    @Resource
    private MockRawDataService service;

    @RequestMapping("/")
    public String index() {
        endpoint.handleMessage("test");
        return "Greetings from Breeze Sport!";
    }



    @RequestMapping("/sendMockJson")
    public String sendMockJson(String wsUrl, String path) {
        service.sendRawData(wsUrl,path);
        return "Greetings from Breeze Sport!";
    }

    @RequestMapping("/sendJson")
    public String sendJson() {
        endpoint.handleMessage("test");
        return "Greetings from Breeze Sport!";
    }
}