package com.zwc.gateway.config.filters;

import com.zwc.gateway.propertity.BlackListProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
/**
 * 黑名单全局过滤器    通过访问路径最后一个值判断
 * @author 赵育冬
 */
@Component
public class BlackListFilter implements GlobalFilter , Ordered {

    /**
     * 获取黑名单清单
     */
    @Autowired
    BlackListProperties blackListProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //判断黑名单过滤是否打开
        if (!blackListProperties.isEnable()){
            //未打开
            return chain.filter(exchange);
        }
        //打开,判断，直接返回响应
        String path = exchange.getRequest().getURI().getPath();
        String lastPath = splitPath(path);
        if (!blackListProperties.getBlackList().contains(lastPath)){
            return chain.filter(exchange);
        }

        //构建响应
        Map<String, String> msg = new HashMap<>();
        msg.put(path,"这个被拉黑了");
        byte[] bytes = msg.toString().getBytes();
        ServerHttpResponse response = exchange.getResponse();
        //设置响应体
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        //设置响应状态
        response.setStatusCode(HttpStatus.OK);
        //设置响应体类型,编码格式防止中文乱码
        response.getHeaders().add("Content-Type","application/json;charset=utf-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 指定过滤器顺序，order越小，优先级越大
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
    private String splitPath(String path) {
        String[] paths = path.split("/");
        return paths[paths.length-1];
    }
}
