package com.zwc.gateway.apollo;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
@Component
public class RefreshConfigListener implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;

    @ApolloConfigChangeListener(value = {"sentinel-rule"})
    private void onGatewayConfigChange(ConfigChangeEvent changeEvent){
        ConfigChange change = changeEvent.getChange("sentinel-dashboard-gateway-api-group");
        if (change ==null){
            return;
        }
        String newValue = change.getNewValue();

        Set<ApiDefinition> apiDefinitions = new HashSet<>();

        Set<MyApiDefinition> apiDefinitions1 = JSON.parseObject(newValue, new TypeReference<Set<MyApiDefinition>>() {
        });

        for (MyApiDefinition myApiDefinition : apiDefinitions1) {

            Set<ApiPredicateItem> apiPredicateItems = new HashSet<>();
            Set<ApiPathPredicateItem> predicateItems = myApiDefinition.getPredicateItems();
            for (ApiPathPredicateItem predicateItem : predicateItems) {
                apiPredicateItems.add(predicateItem);
            }

            ApiDefinition apiDefinition = new ApiDefinition();
            apiDefinition.setApiName(myApiDefinition.getApiName());
            apiDefinition.setPredicateItems(apiPredicateItems);
            apiDefinitions.add(apiDefinition);
        }

        GatewayApiDefinitionManager.loadApiDefinitions(apiDefinitions);
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
