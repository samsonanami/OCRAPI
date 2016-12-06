package com.fintech.oracle.oracle.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by sasitha on 12/4/16.
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://editor.swagger.i0")
                .allowedMethods("GET", "PUT", "POST");
        super.addCorsMappings(registry);
    }
}
