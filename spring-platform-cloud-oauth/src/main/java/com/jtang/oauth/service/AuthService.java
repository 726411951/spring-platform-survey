package com.jtang.oauth.service;

import com.alibaba.fastjson.JSON;
import core.client.ServiceConstants;
import core.model.AuthToken;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class AuthService {

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    private RedissonClient redissonClient;


    /** 用户认证申请令牌，将令牌存储到redis */
    public AuthToken login(String username, String password, String clientId, String clientSecret) {

        //请求spring security申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if(authToken == null){
            throw new RuntimeException();
        }
        //用户身份令牌
        String accessToken = authToken.getAccess_token();
        //存储到redis中的内容
        String jsonString = JSON.toJSONString(authToken);
        //将令牌存储到redis
        boolean result = this.saveToken(accessToken, jsonString, tokenValiditySeconds);
        if (!result) {
            throw new RuntimeException();
        }
        return authToken;

    }
    //存储到令牌到redis

    /**
     *
     * @param access_token 用户身份令牌
     * @param content  内容就是AuthToken对象的内容
     * @param ttl 过期时间
     * @return
     */
    private boolean saveToken(String access_token,String content,long ttl){
        String key = "user_token:" + access_token;
        redissonClient.getQueue(key).add(content);
//        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return true;
    }

    /** 申请令牌 */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret){
        //从eureka中获取认证服务的地址（因为spring security在认证服务中）
        //从eureka中获取认证服务的一个实例的地址
        ServiceInstance serviceInstance = loadBalancerClient.choose(ServiceConstants.ACCOUNT_SERVICE);
        //此地址就是http://ip:port
        URI uri = serviceInstance.getUri();
        //令牌申请的地址 http://localhost:40400/auth/oauth/token
        String authUrl = uri+ "/auth/oauth/token";
        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization",httpBasic);

        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);
        //String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables

        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
//            @Override
//            public void handleError(ClientHttpResponse response) throws IOException {
//                if(response.getRawStatusCode()!= 400 && response.getRawStatusCode()!=401){
//                    super.handleError(response);
//                }
//            }
//        });

//        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
//
//        //申请令牌信息
//        Map bodyMap = exchange.getBody();
//        if(bodyMap == null ||
//            bodyMap.get("access_token") == null ||
//                bodyMap.get("refresh_token") == null ||
//                bodyMap.get("jti") == null){
//            return null;
//        }
        AuthToken authToken = new AuthToken();
//        //用户身份令牌
//        authToken.setAccess_token((String) bodyMap.get("jti"));
//        //刷新令牌
//        authToken.setRefresh_token((String) bodyMap.get("refresh_token"));
//        //jwt令牌
//        authToken.setJwt_token((String) bodyMap.get("access_token"));
        return authToken;
    }



    /** 获取httpbasic的串 */
    private String getHttpBasic(String clientId,String clientSecret){
        String string = clientId+":"+clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }
}