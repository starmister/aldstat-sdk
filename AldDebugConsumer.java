package com.aldwx.analytics.javasdk.consumer;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.aldwx.analytics.javasdk.util.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensorsdata.analytics.javasdk.consumer.Consumer;
import com.sensorsdata.analytics.javasdk.exceptions.DebugModeException;
import com.sensorsdata.analytics.javasdk.util.SensorsAnalyticsUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AldDebugConsumer implements Consumer {
    private static final Logger log = LoggerFactory.getLogger(AldDebugConsumer.class);
    final HttpConsumerWrapper httpConsumer;
    final ObjectMapper jsonMapper;

    public AldDebugConsumer(String appKey,boolean debug, boolean writeData) {

        Map<String, String> headers = new HashMap();
        if (!writeData) {
            headers.put("Dry-Run", "true");
        }
        String serverUrl = Constant.onlineServerUrl+appKey;
        if(debug) {
            serverUrl = Constant.debugServerUrl+appKey;
        }
        this.httpConsumer = new HttpConsumerWrapper(serverUrl, headers);
        this.jsonMapper = SensorsAnalyticsUtil.getJsonObjectMapper();
        log.info("Initialize AldDebugConsumer with params:[writeData:{}].", writeData);
    }

    public void send(Map<String, Object> message) {
        List<Map<String, Object>> messageList = new ArrayList();
        messageList.add(message);

        String sendingData;
        try {
            sendingData = this.jsonMapper.writeValueAsString(messageList);
        } catch (JsonProcessingException var9) {
            log.error("Failed to process json.", var9);
            throw new RuntimeException("Failed to serialize data.", var9);
        }

        try {
            synchronized(this.httpConsumer) {
                this.httpConsumer.consume(sendingData);
            }

            log.info("Successfully send data:[{}].", sendingData);
        } catch (IOException var7) {
            log.error("Failed to send message with AldDebugConsumer,message:[{}].", sendingData, var7);
            throw new DebugModeException("Failed to send message with AldDebugConsumer.", var7);
        } catch (HttpConsumerWrapper.HttpConsumerException var8) {
            log.error("Failed send message with server occur error,message:[{}].", sendingData, var8);
            throw new DebugModeException(var8);
        }
    }

    public void flush() {
    }

    public void close() {
        this.httpConsumer.close();
    }
}
