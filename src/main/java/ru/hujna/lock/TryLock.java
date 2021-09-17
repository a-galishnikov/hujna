package ru.hujna.lock;

import lombok.NonNull;

import java.util.concurrent.locks.Lock;

public class TryLock implements AutoCloseable {

    @NonNull
    private final Lock lock;

    private final boolean lockAcquired;

    private TryLock(Lock lock) {
        this.lock = lock;
        lockAcquired = lock.tryLock();
    }

    public static TryLock of(Lock lock) {
        return new TryLock(lock);
    }

    public boolean isLockAcquired() {
        return lockAcquired;
    }

    @Override
    public void close() {
        if (lockAcquired) {
            lock.unlock();
        }
    }
}
