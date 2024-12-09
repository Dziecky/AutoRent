package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ścieżka URL do serwowania obrazów
        registry.addResourceHandler("/images/**")
                // Ścieżka systemowa do katalogu z obrazami
                .addResourceLocations("file:/C:/Users/kprze/autorent/images")
                .setCachePeriod(3600) // Czas cache w sekundach
                .resourceChain(true);
    }
}
