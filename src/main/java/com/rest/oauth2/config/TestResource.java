package com.rest.oauth2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import java.util.Collections;
import java.util.List;

@Configuration
@Slf4j
public class TestResource {

    @Autowired
    DefaultTokenServices tokenService;

    @Bean
    protected ResourceServerConfiguration adminResources() {
        ResourceServerConfiguration resource = new ResourceServerConfiguration() {
            public void setConfigurers(List<ResourceServerConfigurer> configurers) {
                super.setConfigurers(configurers);
            }
        };
        resource.setConfigurers(Collections.<ResourceServerConfigurer>singletonList(new ResourceServerConfigurerAdapter() {
            @Override
            public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
                resources.resourceId("test").tokenServices(tokenService);
            }

            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest().authenticated();
            }
        }));
        resource.setOrder(3);
        return resource;
    }

    @Bean
    protected ResourceServerConfiguration userResources() {
        ResourceServerConfiguration resource = new ResourceServerConfiguration() {
            public void setConfigurers(List<ResourceServerConfigurer> configurers) {
                super.setConfigurers(configurers);
            }
        };
        resource.setConfigurers(Collections.<ResourceServerConfigurer>singletonList(new ResourceServerConfigurerAdapter() {
            @Override
            public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
                resources.resourceId("test1").tokenServices(tokenService);
            }

            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest().authenticated();
            }
        }));
        resource.setOrder(4);
        return resource;
    }
}
