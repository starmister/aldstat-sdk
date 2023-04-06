package com.aldwx.analytics.javasdk.consumer;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import com.aldwx.analytics.javasdk.util.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensorsdata.analytics.javasdk.consumer.Consumer;
import com.sensorsdata.analytics.javasdk.util.SensorsAnalyticsUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class AldBatchConsumer implements Consumer {
    private static final Logger log = LoggerFactory.getLogger(AldBatchConsumer.class);
    private static final int MAX_FLUSH_BULK_SIZE = 1000;
    private static final int MAX_CACHE_SIZE = 6000;
    private static final int MIN_CACHE_SIZE = 3000;
    private final List<Map<String, Object>> messageList;
    private final HttpConsumerWrapper httpConsumer;
    private final ObjectMapper jsonMapper;
    private final int bulkSize;
    private final boolean throwException;
    private final int maxCacheSize;


    /**
     * debug true代表测试环境，false代表正式环境
     * @param appKey
     * @param debug
     */
    public AldBatchConsumer(String appKey,boolean debug) {
        this(appKey,debug, 50);
    }

    public AldBatchConsumer(String appKey,boolean debug, int bulkSize) {
        this(appKey,debug, bulkSize, 3);
    }

    public AldBatchConsumer(String appKey,boolean debug, int bulkSize, int timeoutSec) {
        this(appKey,debug, bulkSize, false, timeoutSec);
    }

    public AldBatchConsumer(String appKey,boolean debug, int bulkSize, boolean throwException) {
        this(appKey,debug, bulkSize, throwException, 3);
    }

    public AldBatchConsumer(String appKey,boolean debug, int bulkSize, boolean throwException, int timeoutSec) {
        this(appKey,debug, bulkSize, 0, throwException, timeoutSec);
    }

    public AldBatchConsumer(String appKey,boolean debug, int bulkSize, int maxCacheSize, boolean throwException) {
        this(appKey,debug, bulkSize, maxCacheSize, throwException, 3);
    }

    public AldBatchConsumer(String appKey,boolean debug, int bulkSize, int maxCacheSize, boolean throwException, int timeoutSec) {
        this.messageList = new LinkedList();
        String serverUrl = Constant.onlineServerUrl+appKey;
        if(debug) {
            serverUrl = Constant.debugServerUrl+appKey;
        }
        this.httpConsumer = new HttpConsumerWrapper(serverUrl, Math.max(timeoutSec, 1));
        this.jsonMapper = SensorsAnalyticsUtil.getJsonObjectMapper();
        this.bulkSize = Math.min(1000, Math.max(1, bulkSize));
        if (maxCacheSize > 6000) {
            this.maxCacheSize = 6000;
        } else if (maxCacheSize > 0 && maxCacheSize < 3000) {
            this.maxCacheSize = 3000;
        } else {
            this.maxCacheSize = maxCacheSize;
        }

        this.throwException = throwException;
        log.info("Initialize BatchConsumer with params:[bulkSize:{},timeoutSec:{},maxCacheSize:{},throwException:{}]", new Object[]{bulkSize, timeoutSec, maxCacheSize, throwException});
    }

    public void send(Map<String, Object> message) {
        synchronized(this.messageList) {
            int size = this.messageList.size();
            if (this.maxCacheSize <= 0 || size < this.maxCacheSize) {
                this.messageList.add(message);
                ++size;
                log.info("Successfully save data to cache,The cache current size is {}.", size);
            }

            if (size >= this.bulkSize) {
                log.info("Flush was triggered because the cache size reached the threshold,cache size:{},bulkSize:{}.", size, this.bulkSize);
                this.flush();
            }

        }
    }

    public void flush() {
        synchronized(this.messageList) {
            while(!this.messageList.isEmpty()) {
                List<Map<String, Object>> sendList = this.messageList.subList(0, Math.min(this.bulkSize, this.messageList.size()));

                String sendingData;
                try {
                    sendingData = this.jsonMapper.writeValueAsString(sendList);
                } catch (JsonProcessingException var7) {
                    sendList.clear();
                    log.error("Failed to process json.", var7);
                    if (!this.throwException) {
                        continue;
                    }

                    throw new RuntimeException("Failed to serialize data.", var7);
                }

                log.debug("Will be send data:{}.", sendingData);

                try {
                    this.httpConsumer.consume(sendingData);
                    sendList.clear();
                } catch (Exception var6) {
                    log.error("Failed to send data:{}.", sendingData, var6);
                    if (this.throwException) {
                        throw new RuntimeException("Failed to dump message with BatchConsumer.", var6);
                    }

                    return;
                }

                log.debug("Successfully send data:{}.", sendingData);
            }

            log.info("Finish flush.");
        }
    }

    public void close() {
        this.flush();
        this.httpConsumer.close();
        log.info("Call close method.");
    }
}

