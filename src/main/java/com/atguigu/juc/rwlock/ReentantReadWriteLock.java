package com.atguigu.juc.rwlock;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  ReentrantReadWriteLock 支持并发读 只支持一个写并且读写是互斥的
 *  StampedLock 支持ReentrantReadWriteLock的两种模式 支持并发读,支持一个写.
 *  同时乐观读模式 支持并发读的同时进行获取写锁
 *
 * 锁降级可以保证性能,写锁降级成读锁后可以被多线程共享,至于保证可见性,锁本身的意义就保证了数据,降级没有意义
 * 锁升级 必须只要需要升级的线程持有读锁然后获取写锁才能升级,其他线程不释放自己持有的读锁是无法升级的
 */
public class ReentantReadWriteLock {

    User user = new User(0, "懒狗莎莎");
    ReentrantLock rl = new ReentrantLock();
    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();



    void write(Integer id) {
        reentrantReadWriteLock.writeLock().lock();
        System.out.println("线程" + Thread.currentThread().getName() + "正在写入");
        reentrantReadWriteLock.readLock().lock();
        user.setId(id);
        reentrantReadWriteLock.writeLock().unlock();
        System.out.println("写锁已经释放进行锁降级接下来其他线程可以读取结果");
        try {
            Thread.sleep(3000);

            System.out.println("线程" + Thread.currentThread().getName() + "完成写入 结果为"+user);
            reentrantReadWriteLock.readLock().unlock();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }






    }
   void read() {
       reentrantReadWriteLock.readLock().lock();
       System.out.println("线程" + Thread.currentThread().getName() + "正在读取");
       try {
           Thread.sleep(1000);
           System.out.println("线程" + Thread.currentThread().getName() + "完成读取 结果为"+user);

       } catch (InterruptedException e) {
           e.printStackTrace();
       }finally {
           reentrantReadWriteLock.readLock().unlock();

       }

   }

    public static void main(String[] args) throws InterruptedException {
        ReentantReadWriteLock reentantReadWriteLock = new ReentantReadWriteLock();
        new Thread(() -> {
            reentantReadWriteLock.write(1);
        },"线程1").start();

        new Thread(() -> {
            reentantReadWriteLock.read();
        },"线程2").start();
        new Thread(() -> {
            reentantReadWriteLock.read();
        },"线程4").start();
        new Thread(() -> {
            reentantReadWriteLock.write(222);
        },"线程3再次修改数据").start();


    }
}
@Data
@RequiredArgsConstructor
class  User{
    Integer id;
    String userName;

    public User(Integer id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
