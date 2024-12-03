package ru.assist.migrate.slack2pachka.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class EventStatCounter {
    final AtomicBoolean isDirty = new AtomicBoolean(false);
    final AtomicLong success = new AtomicLong(0);
    final AtomicLong failure = new AtomicLong(0);
    final AtomicLong started_at = new AtomicLong(System.currentTimeMillis());
    final AtomicLong now = new AtomicLong(System.currentTimeMillis());

    public void addSuccess() {
        isDirty.set(true);
        success.incrementAndGet();
    }

    public void addFailure() {
        isDirty.set(true);
        failure.incrementAndGet();
    }

    public EventStatCounter() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                now.getAndSet(System.currentTimeMillis());
                stat();
                reset();
            }
        }, 1 * 60 * 1000, 60 * 1000);
    }

    private void reset() {
        success.set(0);
        failure.set(0);
        started_at.set(System.currentTimeMillis());
    }

    private void stat() {
        if(!isDirty.get()) return;
        isDirty.set(false);

        long seconds = (now.get() - started_at.get()) / 1000;

        double successPerSecond = (double) success.get() / (double) seconds;
        double failuresPerSecond = (double) failure.get() / (double) seconds;
        oneStringStatePrint(successPerSecond, failuresPerSecond);
    }

    private void oneStringStatePrint(double successPerSecond, double failuresPerSecond) {
        double successPerMinute = successPerSecond * 60;
        double successPerHour = successPerMinute * 60;
        double successPerDay = successPerHour * 24;

        double failuresPerMinute = failuresPerSecond * 60;
        double failuresPerHour = failuresPerMinute * 60;
        double failuresPerDay = failuresPerHour * 24;

        double total = successPerSecond + failuresPerSecond;
        double successPercent;
        if ((long) total == 0) successPercent = 0.0;
        else successPercent = (successPerSecond * 100) / total;

        log.info("success / failures : s {} / {}, m {} / {}, h {} / {}, d {} / {}, {}%",
                (long) successPerSecond, (long) failuresPerSecond,
                (long) successPerMinute, (long) failuresPerMinute,
                (long) successPerHour, (long) failuresPerHour,
                (long) successPerDay, (long) failuresPerDay,
                String.format("%.2f", successPercent));
    }
}
