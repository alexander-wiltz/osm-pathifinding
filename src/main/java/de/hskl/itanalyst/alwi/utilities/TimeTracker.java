package de.hskl.itanalyst.alwi.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimeTracker {

    private Long startTime;

    public void startTime() {
        startTime = System.currentTimeMillis();
    }

    public void endTime(String className) {
        Long endTime = System.currentTimeMillis() - startTime;
        log.info("[{}]: Time taken: {} ms", className, endTime);
    }
}
