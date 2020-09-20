package com.zwc.gateway.apollo;


import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
@Component
public class MyApiDefinition {
    private String apiName;
    private Set<ApiPathPredicateItem> predicateItems;
    public MyApiDefinition() {
    }

    public MyApiDefinition(String apiName) {
        this.apiName = apiName;
    }

    public String getApiName() {
        return this.apiName;
    }

    public MyApiDefinition setApiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    public Set<ApiPathPredicateItem> getPredicateItems() {
        return this.predicateItems;
    }

    public MyApiDefinition setPredicateItems(Set<ApiPathPredicateItem> predicateItems) {
        this.predicateItems = predicateItems;
        return this;
    }
}
