package kr.co.dealmungchi.hotdealapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ProfileConfig {

    @Bean
    public String activeProfile(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        
        if (activeProfiles.length == 0) {
            System.setProperty("spring.profiles.active", "dev");
            System.out.println("No profile specified. Default profile 'dev' activated.");
            return "dev";
        }
        
        return String.join(",", activeProfiles);
    }
}