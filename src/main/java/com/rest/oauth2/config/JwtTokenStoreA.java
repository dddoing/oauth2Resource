package com.rest.oauth2.config;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.context.request.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenStoreA extends JwtTokenStore {
    //
    private JwtAccessTokenConverter jwtTokenEnhancer;

    public JwtTokenStoreA(JwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        //
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();

        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attribs).getRequest();
            try{

                Enumeration aa = request.getHeaderNames();
                Map <String,Object> header = new HashMap<>();
                while (aa.hasMoreElements()) {
                    String a = (String)aa.nextElement();
                    String b = request.getHeader(a);
                    header.put(a,b);
                }
                String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Map<String,Object> map = new HashMap<>();
                if (test.length() != 0) {
                    map = new ObjectMapper().readValue(test, new TypeReference<Map<String,Object>>() {});
                }

                boolean cc = header.containsKey("id")? validate((String)header.get("id"),tokenValue) : validate((String)map.get("id"),tokenValue);
                if (!cc) {
                    throw new InvalidTokenException("error");
                }
            } catch (IOException e) {
            }
        }
        return super.readAccessToken(tokenValue);
    }

    private boolean validate(String id,String token) {
        //
        String[] tokens = token.split("\\.");
        String pay = tokens[1];

        Base64.Decoder decoder = Base64.getDecoder();
        String payValue = new String(decoder.decode(pay));

        Map map = new Gson().fromJson(payValue,Map.class);
        log.info("{}",map.get("jti"));
        log.info("{}",id);

        return map.get(AccessTokenConverter.JTI).equals(id);
    }
}
