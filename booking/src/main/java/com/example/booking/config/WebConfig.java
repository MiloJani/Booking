package com.example.booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/SavedPhotos/**")
                .addResourceLocations("file:C:/Users/USER/Desktop/SavedPhotos/Businesses");

        registry.addResourceHandler("/SavedPhotos/Rooms/**")
                .addResourceLocations("file:C:/Users/USER/Desktop/SavedPhotos/Rooms/");

    }
}

