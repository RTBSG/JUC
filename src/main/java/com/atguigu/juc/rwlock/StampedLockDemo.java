package com.atguigu.juc.rwlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @auther zzyy
 * @create 2021-03-29 11:35
 */
public class StampedLockDemo
{
    static int number = 37;
/*    StampedLock是比ReentrantReadWriteLock更快的一种锁，
支持乐观读、悲观读锁和写锁。和ReentrantReadWriteLock不同的是
允许多 个线程同时获取乐观锁和悲观读锁StampedLock支持多个线程申请乐观读的同时，还允许一个线程申请写锁。
    乐观读并不加锁
    StampedLock的底层并不是基于AQS的。*/
    static StampedLock stampedLock = new StampedLock();

    public void write()
    {
//        独占锁 是排他行的
        long stamp = stampedLock.writeLock();
        System.out.println(Thread.currentThread().getName()+"\t"+"=====写线程准备修改");
//        在持有写锁的情况下再去获取写锁 会造成死锁
//        long stamp1 = stampedLock.writeLock();
        try
        {
            TimeUnit.SECONDS.sleep(2);
            number = number + 13;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("unlock");
            stampedLock.unlockWrite(stamp);
//            stampedLock.unlockWrite(stamp1);

        }
        System.out.println(Thread.currentThread().getName()+"\t"+"=====写线程结束修改");
    }

    //悲观读 允许多个线程进行读操作 乐观读和悲观读都能读
    public void read()
    {
//        非排他性地获取锁，在必要时阻塞直到可用 阻塞等待释放锁
        long stamp = stampedLock.readLock();
        System.out.println(Thread.currentThread().getName()+"\t come in readlock block,4 seconds continue...");
        //暂停4秒钟线程
        for (int i = 0; i <4 ; i++) {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"\t 正在读取中......");
        }
        try
        {
            int result = number;
            System.out.println(Thread.currentThread().getName()+"\t"+" 获得成员变量值result：" + result);
            System.out.println("写线程没有修改值，因为 stampedLock.readLock()读的时候，不可以写，读写互斥");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            stampedLock.unlockRead(stamp);
        }
    }

    //乐观读
    public void tryOptimisticRead()
    {
//        返回标记 如果被占用返回0为false 说明被修改了
        long stamp = stampedLock.tryOptimisticRead();
        //先把数据取得一次
        int result = number;
        //间隔4秒钟，我们很乐观的认为没有其他线程修改过number值，愿望美好，实际情况靠判断。
        System.out.println("4秒前stampedLock.validate值(true无修改，false有修改)"+"\t"+stampedLock.validate(stamp));
        for (int i = 1; i <=4 ; i++) {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"\t 正在读取中......"+i+
                    "秒后stampedLock.validate值(true无修改，false有修改)"+"\t"
                    +stampedLock.validate(stamp));
        }
        if(!stampedLock.validate(stamp)) {
            System.out.println("有人动过--------存在写操作！");
            //有人动过了，需要从乐观读切换到普通读的模式。
            stamp = stampedLock.readLock();
            try {
                System.out.println("从乐观读 升级为 悲观读并重新获取数据");
                //重新获取数据
                result = number;
                System.out.println("重新悲观读锁通过获取到的成员变量值result：" + result);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                stampedLock.unlockRead(stamp);
            }
        }
        System.out.println(Thread.currentThread().getName()+"\t finally value: "+result);
    }

    public static void main(String[] args)
    {
        StampedLockDemo resource = new StampedLockDemo();


/*        //1 悲观读,和ReentrantReadWriteLock一样
        new Thread(() -> {
            //悲观读
            resource.read();
        },"readThread").start();*/


        new Thread(() -> {
            resource.write();

        },"writeThread").start();


   /*   //2 乐观读，成功
        new Thread(() -> {
            //乐观读
            resource.tryOptimisticRead();

        },"readThread").start();


        //6秒钟乐观读取resource.tryOptimisticRead()成功
        try { TimeUnit.SECONDS.sleep(6); } catch (InterruptedException e) { e.printStackTrace(); }
*/
 /*       //3 乐观读，失败，重新转为悲观读，重读数据一次
        new Thread(() -> {
            //乐观读
            resource.tryOptimisticRead();
        },"readThread").start();

        //2秒钟乐观读取resource.tryOptimisticRead()失败
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }



*/
    }
}
