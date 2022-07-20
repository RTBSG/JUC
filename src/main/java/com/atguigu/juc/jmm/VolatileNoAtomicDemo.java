package com.atguigu.juc.jmm;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther zzyy
 * @create 2021-03-17 14:59
 */
class MyNumber
{
//    单纯volatile 并不能保证线程安全
    volatile int num= 0;
//    使用volatile和atomic 实现可见性,一致性,原子性的保证 atomic内部已经使用了volatile
  AtomicInteger atomicInteger = new AtomicInteger();
    public void addPlusPlus()
    {
        atomicInteger.incrementAndGet();
    }

    public void addInt(){
        num++;
    }
}


public class VolatileNoAtomicDemo
{
    public static void main(String[] args) throws InterruptedException
    {
        MyNumber myNumber = new MyNumber();

        for (int i = 1; i <=10; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myNumber.addPlusPlus();
                    myNumber.addInt();
                }
            },String.valueOf(i)).start();
        }

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println(Thread.currentThread().getName() + "\t cas自旋配合atomic类并发一万次计算的结果" + myNumber.atomicInteger.get());
        System.out.println(Thread.currentThread().getName() + "\t 普通int类并发一万次计算的结果" + myNumber.num);
    }
}

