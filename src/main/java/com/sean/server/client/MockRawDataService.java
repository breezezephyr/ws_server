package com.sean.server.client;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : sean.cai
 * @version : 1.0.0
 * @since : 2019/11/12 11:55 AM
 */

@Slf4j
@Service
public class MockRawDataService {
    public static WebSocketClient client;

    public void sendRawData(String wsUrl, String path) {
        try {
            client = new WebSocketClient(new URI(wsUrl), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("握手成功");
                }

                @Override
                public void onMessage(String msg) {
                    log.info("收到消息==========" + msg);
                    if (msg.equals("over")) {
                        client.close();
                    }

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("链接已关闭");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    log.info("发生错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client.connect();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            log.info("正在连接...");
        }
        List<String> strings = fetchJson(path);
        strings.stream().forEach(str -> {
            Long start = System.currentTimeMillis();
            client.send(str);
            log.info("send success, cost:{}", System.currentTimeMillis() - start);
            try {
                TimeUnit.MILLISECONDS.sleep(25);
            } catch (InterruptedException e) {
                log.error("sleep error");
            }
        });
    }

    /**
     * 读取数据并保存
     *
     * @param jsonFilePath json文件绝对路径
     */
    public List<String> fetchJson(String jsonFilePath) {
        File jsonFile = new File(jsonFilePath);
        if (!jsonFile.exists()) {
            log.warn("文件不存在, path:{}", jsonFilePath);
            return null;
        }
        try {
            List<String> strings = Files.readLines(jsonFile, Charsets.UTF_8);
            return strings.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("read file failed", e);
        }
        return null;
    }
}
