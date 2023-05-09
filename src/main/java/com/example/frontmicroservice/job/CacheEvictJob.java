package com.example.frontmicroservice.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheEvictJob {
    @Scheduled(cron = "0 */1 * ? * *")
    @CacheEvict(cacheNames = "cacheGetAllTariff", allEntries = true)
    public void evictCacheGetStatus() {
        log.info("кэш метода getAllTariff очистился");
    }
}
