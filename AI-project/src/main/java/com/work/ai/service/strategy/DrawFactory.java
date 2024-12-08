package com.work.ai.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DrawFactory {
    @Autowired
    ApplicationContext applicationContext;

    public static Map<Integer,DrawService> map = new ConcurrentHashMap<>();


    public static DrawService getService(Integer apiType) {
        return map.get(apiType);
    }

    @PostConstruct
    public void init() {
        Map<String, DrawService> beansOfType = applicationContext.getBeansOfType(DrawService.class);
        for (Map.Entry<String, DrawService> entry : beansOfType.entrySet()) {
            DrawService drawService = entry.getValue();
            map.put(drawService.getType(), drawService);
        }
    }

}
