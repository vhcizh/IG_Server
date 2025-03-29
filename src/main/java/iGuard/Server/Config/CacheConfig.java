package iGuard.Server.Config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, String> verificationCodeCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES) // 3분 TTL
                .maximumSize(1000)
                .build();
    }
}