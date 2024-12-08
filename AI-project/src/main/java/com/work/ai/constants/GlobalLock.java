package com.work.ai.constants;

import java.util.concurrent.locks.ReentrantLock;

public class GlobalLock {

    // 创建一个全局锁
    private static final ReentrantLock doubaoLock = new ReentrantLock();

    public static ReentrantLock getDoubaoLock() {
        return doubaoLock;
    }
}
