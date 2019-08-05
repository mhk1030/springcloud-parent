package com.mhk.config;

import com.mhk.filter.MyGateWayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 20:04
 */
@Configuration
public class MyConfig {

    /**
     * 在路由中添加自定义的过滤器
     *
     */
    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder route = routeLocatorBuilder.routes().route(
                r -> r.path("/api/client/**")
                        .filters(f -> f.stripPrefix(2).filter(getMyFilter()))//在路由中添加自定义的过滤器
                        .uri("lb://gateway-client")
                        .order(100)
                        .id("gateway-client1")
        );
        return route.build();
    }

    @Bean
    public GatewayFilter getMyFilter() {
        return  new MyGateWayFilter();
    }

}
