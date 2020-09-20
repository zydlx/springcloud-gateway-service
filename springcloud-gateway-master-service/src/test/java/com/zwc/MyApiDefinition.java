package com.zwc;


import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;


import java.util.Objects;
import java.util.Set;

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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MyApiDefinition that = (MyApiDefinition)o;
            return !Objects.equals(this.apiName, that.apiName) ? false : Objects.equals(this.predicateItems, that.predicateItems);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.apiName != null ? this.apiName.hashCode() : 0;
        result = 31 * result + (this.predicateItems != null ? this.predicateItems.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "GatewayApiDefinition{apiName='" + this.apiName + '\'' + ", predicateItems=" + this.predicateItems + '}';
    }
}
