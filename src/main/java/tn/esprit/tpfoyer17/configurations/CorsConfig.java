package tn.esprit.tpfoyer17.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow CORS for your Angular application
        registry.addMapping("/**")  // Apply CORS policy to all endpoints
                .allowedOrigins("http://192.168.73.167:4200")  // Change this to the URL of your Angular app
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow the methods you need
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // Allow cookies (if required)
    }
}
