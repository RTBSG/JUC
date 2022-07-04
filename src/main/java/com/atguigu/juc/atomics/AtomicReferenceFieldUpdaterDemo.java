package com.atguigu.juc.atomics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 *  AtomicReferenceFieldUpdate 对volatitle修饰的属性进行原子更新
 *  通过修改标记位 致使其他线程无法再次执行
 *
 */
class MyVar
{
    int i=0;
    public volatile Boolean isInit = Boolean.FALSE;
//    更新器绑定属性
    AtomicReferenceFieldUpdater<MyVar,Boolean> FieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MyVar.class,Boolean.class,"isInit");
    public void test(MyVar var){
        boolean flg = FieldUpdater.compareAndSet(var, false, true);
        if (flg) {
            System.out.println("当前线程" + Thread.currentThread().getName() + "开始进行初始化");
            for (int i = 1; i <= 10; i++) {
                System.out.println("初始化完成"+i*10+"%");
            }
            System.out.println("初始化完成");
        }

    }





    
    
   /*
    public void init(MyVar myVar)
    {
        if(FieldUpdater.compareAndSet(myVar,Boolean.FALSE,Boolean.TRUE))
        {
            System.out.println(Thread.currentThread().getName()+"\t"+"---start init");
            try {
                i = 2;
                TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"\t"+"---end init"+"t值为:"+i);
        }else{
            System.out.println(Thread.currentThread().getName()+"\t"+"---抢夺失败，已经有线程在修改中");
        }
    }*/

}


/**
 * @auther zzyy
 * @create 2021-03-22 15:20
 *  多线程并发调用一个类的初始化方法，如果未被初始化过，将执行初始化工作，要求只能初始化一次
 */
public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) {
        MyVar myVar = new MyVar();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                myVar.test(myVar);
            }, "线程" + i).start();
        }

    }

}