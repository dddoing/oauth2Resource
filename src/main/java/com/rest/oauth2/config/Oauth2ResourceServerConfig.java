package com.rest.oauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableResourceServer
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.jwt.signkey}")
    private String signKey;

    private JwtAuthEntryPoint jwtAuthEntryPoint;

    public Oauth2ResourceServerConfig(JwtAuthEntryPoint jwtAuthEntryPoint) {
        //
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenService()).resourceId(null).authenticationEntryPoint(jwtAuthEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();

        List<RequestMatcher> requestMatchers = new ArrayList<RequestMatcher>();

        requestMatchers.add(new AntPathRequestMatcher("/v1/users"));

        http
                .requestMatcher(new OrRequestMatcher(requestMatchers))
                .authorizeRequests().antMatchers("/v1/users").authenticated();


//        http.authorizeRequests()
//                .anyRequest().permitAll();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStoreA(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signKey);
        return converter;
    }

    @Bean
    public DefaultTokenServices tokenService() {
        //
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    private static class OAuthRequestedMatcher implements RequestMatcher {
        //
        public boolean matches(HttpServletRequest request) {
            //
            return false;
        }
    }
}

