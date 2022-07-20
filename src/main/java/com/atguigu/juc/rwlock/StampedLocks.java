package com.atguigu.juc.rwlock;

import lombok.SneakyThrows;

import java.util.concurrent.locks.StampedLock;

public class StampedLocks {
    User user = new User(0, "莎莎");
    StampedLock stampedLock = new StampedLock();

    void read() {
        long l = stampedLock.readLock();
        System.out.println(Thread.currentThread().getName() + "正在普通读方式读取数据");
        System.out.println(Thread.currentThread().getName() + "普通读读取数据完成 读取的数据为" + user);
        stampedLock.unlock(l);
    }

    void optimisticRead() {
//        初始获取状态是256; 注意初始标记位没有对应的锁解锁是根据标记位进行解锁的, 初始状态不许解锁
        long l = stampedLock.tryOptimisticRead();

        System.out.println(Thread.currentThread().getName() + "正在乐观读方式读取数据 读取数据"+user);
        if (stampedLock.validate(l)) {
            System.out.println(Thread.currentThread().getName() + "乐观读读取数据完成 读取的数据为" + user);

        } else {
            System.out.println("有人修改了数据需要转换普通读模式");
            l = stampedLock.readLock();
            System.out.println(Thread.currentThread().getName() + "正在以乐观读方式进行数据读取 读取的数据为" + user);
            stampedLock.unlock(l);
        }

    }

    void write(Integer id) {
        long l = stampedLock.writeLock();
        System.out.println(Thread.currentThread().getName() + "正在写入数据");
        user.setId(id);
        try {
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() + "数据写入完成 读取的数据为" + user);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stampedLock.unlockWrite(l);

    }

    public static void main(String[] args) {
        StampedLocks stampedLocks = new StampedLocks();

        new Thread(() -> {
            stampedLocks.optimisticRead();
        }, "线程0").start();

        new Thread(() -> {
            stampedLocks.write(1);
        }, "线程1").start();
        new Thread(() -> {
            stampedLocks.optimisticRead();
        }, "线程2").start();
        new Thread(() -> {
            stampedLocks.optimisticRead();
        }, "线程3").start();
        new Thread(() -> {
            stampedLocks.write(3);
        }, "线程4").start();

    }
}