package com.zwc.gateway.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestConfigChangeListener implements ConfigChangeListener {

    private static final TestConfigChangeListener instance = new TestConfigChangeListener();
    public static TestConfigChangeListener instance (){
        return instance;
    }
    @Override
    public void onChange(ConfigChangeEvent configChangeEvent) {
        String namespace = configChangeEvent.getNamespace();
        Config config = ConfigService.getConfig(namespace);
        Set<String> keys = configChangeEvent.changedKeys();


        for (String key : keys) {
            String property = config.getProperty(key,null);
            System.out.println(property);
        }
    }
}
