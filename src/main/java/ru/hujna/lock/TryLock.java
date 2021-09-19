package ru.hujna.lock;

import lombok.NonNull;

import java.util.concurrent.locks.Lock;

public class TryLock implements AutoCloseable {

    @NonNull
    private final Lock lock;

    private final boolean acquired;

    private TryLock(Lock lock) {
        this.lock = lock;
        acquired = lock.tryLock();
    }

    public static TryLock of(Lock lock) {
        return new TryLock(lock);
    }

    public boolean isAcquired() {
        return acquired;
    }

    @Override
    public void close() {
        if (acquired) {
            lock.unlock();
        }
    }
}
