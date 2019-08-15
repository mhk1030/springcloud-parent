package com.mhk.filter;

import com.alibaba.fastjson.JSONObject;
import com.mhk.jwt.JWTUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 19:37
 */
@Component
public class MyGlobalFilter implements GlobalFilter {

    @Value("${my.auth.urls}")
    private String[] urls;

    @Value("${my.auth.loginPath}")
    private String loginpage;


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求
        ServerHttpRequest request = exchange.getRequest();
        //获取响应
        ServerHttpResponse response = exchange.getResponse();
        //获取当前的请求路径
        String currentpath = request.getURI().toString();
        //验证当前路径是否是公共资源路径也就是不需要进行登录校验的路径
        List<String> strings = Arrays.asList(urls);

        if(strings.contains(currentpath)){
            return chain.filter(exchange);
        }else{
            //获取请求头中的token
            List<String> list = request.getHeaders().get("token");
            System.out.println("=========="+list);
            //解密Token校验是否超时，如果超时的话需要重新登录============该步骤是校验Token的
            JSONObject jsonObject = null;

            try {
                //解密判断Token是否已经失效
                jsonObject= JWTUtils.decodeJwtTocken(list.get(0));
                //如果不报错说明没有失效,重新加密登录信息
                String token = JWTUtils.generateToken(jsonObject.toJSONString());
                response.getHeaders().set("token",token);
            }catch (JwtException e){
                e.printStackTrace();
                response.getHeaders().set("localhost",loginpage);
                response.setStatusCode(HttpStatus.SEE_OTHER);
                return exchange.getResponse().setComplete();
            }
            String userId = jsonObject.get("id").toString();
            Boolean isok = redisTemplate.opsForHash().hasKey("USERDATAAUTH" + userId, currentpath);
            if(isok){
                return chain.filter(exchange);
            }else{
                throw new RuntimeException("不能访问此资源");
            }
        }


    }
}
