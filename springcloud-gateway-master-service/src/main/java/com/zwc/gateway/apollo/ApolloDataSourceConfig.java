package com.zwc.gateway.apollo;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 *  namespaceName 对应 Apollo 的命名空间名称
 *  ruleKey 对应规则存储的 key
 *  defaultRules 对应连接不上 Apollo 时的默认规则
 * @author 赵育冬
 */
@Configuration
public class ApolloDataSourceConfig {

    private String namespaceName = "sentinel-rule";
    private String gatewayRuleKey = "sentinel-dashboard-gateway-flow-rules";
    private String apiKey = "sentinel-dashboard-gateway-api-group";
    private String degradeRuleKey = "sentinel-dashboard-degrade-rules";
    private String systemRuleKey = "sentinel-dashboard-system-rules";
    private String defaultRules = "";
    private String defaultApis = "";
    private String clusterClientConfigKey = "sentinel-dashboard-clusterClientConfig";

    @PostConstruct
    public void init(){
        //注册网关流控规则
        ReadableDataSource<String, Set<GatewayFlowRule>> gatewayRuleDataSource = new ApolloDataSource<>(
                namespaceName, gatewayRuleKey, defaultRules,
                source -> JSON.parseObject(source, new TypeReference<Set<GatewayFlowRule>>() {
                }));
        GatewayRuleManager.register2Property(gatewayRuleDataSource.getProperty());
        //注册网关熔断降级规则
        ReadableDataSource<String, List<DegradeRule>> degradeRule = new ApolloDataSource<>(
                namespaceName, degradeRuleKey, defaultRules,
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                }));
        DegradeRuleManager.register2Property(degradeRule.getProperty());
        //遗留问题,已解决但不完美
        /*ReadableDataSource<String, Set<ApiDefinition>> gatewayApiDataSource  = new ApolloDataSource<>(
                namespaceName, apiKey, defaultApis,
                new Converter<String, Set<ApiDefinition>>() {
                    @Override
                    public Set<ApiDefinition> convert(String source) {

                        return JSON.parseObject(source, new TypeReference<Set<ApiDefinition>>() {

                        });
                    }
                });
        GatewayApiDefinitionManager.register2Property(gatewayApiDataSource.getProperty());*/

        //注册系统规则
        ReadableDataSource<String, List<SystemRule>> systemRule = new ApolloDataSource<>(
                namespaceName, systemRuleKey, defaultRules,
                source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {
                }));
        SystemRuleManager.register2Property(systemRule.getProperty());
        ConfigService.getConfig("application").addChangeListener(TestConfigChangeListener.instance());
        ConfigService.getConfig("sentinel-rule").addChangeListener(TestConfigChangeListener.instance());

//        String clientConfigDataId = "cluster-client-config";
    // 初始化一个配置ClusterClientConfig的 Apollo 数据源
     /*   ReadableDataSource<String, ClusterClientConfig> clusterClientConfig =
                new ApolloDataSource<>(namespaceName, clusterClientConfigKey, clientConfigDataId,
                        source -> JSON.parseObject(source, new TypeReference<ClusterClientConfig>() {}));
        ClusterClientConfigManager.registerClientConfigProperty(clusterClientConfig.getProperty());*/
    }
}
