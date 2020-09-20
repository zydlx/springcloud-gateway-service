package com.zwc.gateway.predicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义断言 ： 通过访问路径最后一位
 * @author 赵育冬
 */
@Component
public class MyPathRoutePredicateFactory extends AbstractRoutePredicateFactory<MyPathRoutePredicateFactory.Config> {
    private static final Log log = LogFactory.getLog(MyPathRoutePredicateFactory.class);


    public MyPathRoutePredicateFactory() {
        super(MyPathRoutePredicateFactory.Config.class);
    }

    /**
     * 读取配置条件
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("path");
    }

    /**
     * 断言逻辑
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(MyPathRoutePredicateFactory.Config config) {

        return (exchange) -> {


            String path = exchange.getRequest().getURI().getPath();


            String lastPath =  splitPath(path);


            if (config.getPath().equals(lastPath)){
                return true;
            }else {
                return false;
            }


        };
    }

    private String splitPath(String path) {
        String[] paths = path.split("/");
        return paths[paths.length-1];
    }


    public static class Config {
       private String path;

        public Config() {
        }

        public String getPath() {
            return path;
        }

        public MyPathRoutePredicateFactory.Config setPath(String path) {
            this.path = path;
            return this;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "path='" + path + '\'' +
                    '}';
        }
    }
}
