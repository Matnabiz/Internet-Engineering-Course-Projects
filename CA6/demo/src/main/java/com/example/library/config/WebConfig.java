package com.example.library.config;

import com.example.library.security.TokenAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenAuthInterceptor tokenAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenAuthInterceptor)
                .addPathPatterns("/books/details/**","/books/content","/books/add",
                                "/authors/add" , "/authors/details/**",
                                "/cart/show/**","/cart/purchase/**","cart/remove","cart/borrow/add","cart/buy/add",
                                "/search/advanced","/search/year","/search/genre/**","/search/author/**","/search/title/**",
                                "/users/**/purchased-books","/users/**/history","/users/**","/users/**/add-comment","/users/**/add-credit"
                        )         // secure these routes
                .excludePathPatterns("/users/register","/users/login","/users/logout/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
