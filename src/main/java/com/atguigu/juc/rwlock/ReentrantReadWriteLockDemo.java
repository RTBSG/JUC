package com.atguigu.juc.rwlock;


import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
class lock{
    static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    static Map<Integer, Integer> map=new HashMap<Integer, Integer>() ;
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                write(finalI);
            }, String.valueOf(i)).start();


        }
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                read(finalI);
            }, String.valueOf(i)).start();

        }
        }



    @SneakyThrows
    public static void write(int i){
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        map.put(i, i);
        Thread.sleep(200);
        System.out.println("线程"+Thread.currentThread().getName()+"写入完成"+i);
        writeLock.unlock();
    }
    @SneakyThrows
    public static void read(int i){
        reentrantReadWriteLock.readLock().lock();
        System.out.println("线程"+Thread.currentThread().getName()+"正在读取数据"+i);
        map.get(i);
        Thread.sleep(200);
        System.out.println("线程"+Thread.currentThread().getName()+"数据读取完成"+i);
        reentrantReadWriteLock.readLock().unlock();
    }
}


class MyResource
{
    Map<String,String> map = new HashMap<>();
    //=====ReentrantLock 等价于 =====synchronized reentrantlocal 需要手动解锁,能够创建公平锁和非公平锁
    Lock lock = new ReentrantLock();
    //=====ReentrantReadWriteLock 一体两面，读写互斥，读读共享
    ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void write(String key,String value)
    {
        rwLock.writeLock().lock();
        try
        {
            System.out.println(Thread.currentThread().getName()+"\t"+"---正在写入");
            map.put(key,value);
            try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"\t"+"---完成写入");
        }finally {
            rwLock.writeLock().unlock();
        }
    }

    public void read(String key)
    {
        rwLock.readLock().lock();
        try
        {
            System.out.println(Thread.currentThread().getName()+"\t"+"---正在读取");
            String result = map.get(key);
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"\t"+"---完成读取result： "+result);
        }finally {
            rwLock.readLock().unlock();
        }
    }

}




/**
 * @auther zzyy
 * @create 2021-03-28 11:04
 */
public class ReentrantReadWriteLockDemo
{
    public static void main(String[] args)
    {
        MyResource myResource = new MyResource();

        for (int i = 1; i <=3; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.write(finalI +"", finalI +"");
            },String.valueOf(i)).start();
        }

        for (int i = 1; i <=3; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.read(finalI +"");
            },String.valueOf(i)).start();
        }

        for (int i = 1; i <=3; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.write(finalI +"", finalI +"");
            },"马羽成"+String.valueOf(i)).start();
        }

    }
}
