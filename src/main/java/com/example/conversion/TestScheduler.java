package com.example.conversion;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestScheduler {
    @Scheduled(fixedDelay = 5000)
    @SchedulerLock(
            name = "testScheduler",
            lockAtMostFor = "PT2M",
            lockAtLeastFor = "PT1M"
    )
    public void test() throws InterruptedException {

        System.out.println("START " + LocalDateTime.now());

        Thread.sleep(30000);

        System.out.println("END " + LocalDateTime.now());
    }
}

