package com.atguigu.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * cas自旋锁 配合原子类使用 来保证原子性
 * @auther zzyy
 * @create 2021-03-18 15:34
 */
public class ABADemo
{
    static AtomicInteger atomicInteger = new AtomicInteger(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        new Thread(() -> {
//            时间戳版本
            int stamp = atomicStampedReference.getStamp();
            System.out.println("初始版本号"+stamp);
            boolean b = atomicStampedReference.compareAndSet(100, 101, 1, stamp + 1);
            System.out.println("线程"+Thread.currentThread().getName()+"第一次变更版本 是否变更成功"+b+ "当前值"+atomicStampedReference.getReference()+"当前版本号:"+atomicStampedReference.getStamp());
            boolean b1 = atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println("线程"+Thread.currentThread().getName()+"第二次变更版本  是否变更成功"+b1+ "当前值"+atomicStampedReference.getReference()+"当前版本号:"+atomicStampedReference.getStamp());

        }, "t1").start();



        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println("初始版本号"+stamp);
            boolean b = atomicStampedReference.compareAndSet(100, 101, 1, stamp + 1);
            System.out.println("线程"+Thread.currentThread().getName()+"第一次变更版本  是否变更成功"+b+ "当前值"+atomicStampedReference.getReference()+"当前版本号:"+atomicStampedReference.getStamp());
            boolean b1 = atomicStampedReference.compareAndSet(101, 102, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println("线程"+Thread.currentThread().getName()+"第二次变更版本  是否变更成功"+b1+ "当前值"+atomicStampedReference.getReference()+"当前版本号:"+atomicStampedReference.getStamp());

        },"t2").start();
        ABADemo.abaProblem();
    }


    /**
     * 无版本号限制产生了aba问题
     */
    public static void abaProblem()
    {
        new Thread(() -> {
            atomicInteger.compareAndSet(100,101);
            atomicInteger.compareAndSet(101,100);
        },"t1").start();

        //暂停毫秒
        try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            boolean b = atomicInteger.compareAndSet(100, 20210308);
            System.out.println(Thread.currentThread().getName()+"\t"+"修改成功否："+b+"\t"+atomicInteger.get());
        },"t2").start();
    }
}
