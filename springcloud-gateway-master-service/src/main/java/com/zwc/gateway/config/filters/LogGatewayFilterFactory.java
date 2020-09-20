package com.zwc.gateway.config.filters;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义局部过滤器
 * @author 赵育冬
 */
public class LogGatewayFilterFactory
        extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {
    /**
     * 构造函数
     */
    public LogGatewayFilterFactory() {
        super(LogGatewayFilterFactory.Config.class);
    }

    /**
     * 读取配置文件中的配置参数  复制到配置类中
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("arg1","arg2");
    }

    /**
     * 过滤器逻辑
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(LogGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {

            return chain.filter(exchange);
        };
    }

    /**
     * 配置类
     */
    @Data
    @NoArgsConstructor
    public static class Config {
        private boolean arg1;
        private boolean arg2;
    }
}
