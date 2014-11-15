package com.ofg.infrastructure.metrics.publishing;

import java.util.concurrent.TimeUnit;

/**
 * Defines publishing interval for metrics publisher
 *
 * @param interval - amount of time between publications
 * @param timeUnit - time unit for {@code interval}
 */
public class PublishingInterval {
    public PublishingInterval(long interval, TimeUnit timeUnit) {
        this.interval = interval;
        this.timeUnit = timeUnit;
    }

    public final long getInterval() {
        return interval;
    }

    public final TimeUnit getTimeUnit() {
        return timeUnit;
    }

    private final long interval;
    private final TimeUnit timeUnit;
}
