package com.zwc.gateway.propertity;



import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
/**
 * 黑名单实体类
 * @author 赵育冬
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="black")
public class BlackListProperties {
    private boolean enable;
    private List<String> blackList = new ArrayList<>();

}
