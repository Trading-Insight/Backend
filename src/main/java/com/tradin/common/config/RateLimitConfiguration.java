package com.tradin.common.config;

import com.tradin.common.filter.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfiguration {

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        RateLimitFilter rateLimitFilter = new RateLimitFilter();
        registrationBean.setFilter(rateLimitFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}