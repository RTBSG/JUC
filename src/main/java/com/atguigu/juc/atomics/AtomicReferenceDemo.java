package com.atguigu.juc.atomics;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;


@ToString
@AllArgsConstructor
@Getter
class User
{
    String userName;
    int    age;
}


/**
 * @auther zzyy
 * @create 2021-03-19 11:39
 */
public class AtomicReferenceDemo
{
    public static void main(String[] args)
    {

        User z3 = new User("z3",24);
        User li4 = new User("li4",26);
        User li5 = new User("li5", 23);
        AtomicReference<User> ar = new AtomicReference<>();

        new Thread(()->{
            ar.set(z3);
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值:"+ar.get()+"开始尝试修改原始值");
            boolean b = ar.compareAndSet(z3, li4);
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值:"+ar.get()+"是否修改成功"+b);


        },"线程1").start();
        new Thread(()->{
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值:"+ar.get()+"开始尝试修改原始值");
            boolean b = ar.compareAndSet(z3, li5);
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值:"+ar.get()+"是否修改成功"+b);

        },"线程2").start();








        AtomicReference<User> atomicReferenceUser = new AtomicReference<>();

        atomicReferenceUser.set(z3);

        System.out.println(atomicReferenceUser.compareAndSet(z3,li4)+"\t"+atomicReferenceUser.get().toString());
        System.out.println(atomicReferenceUser.compareAndSet(z3,li4)+"\t"+atomicReferenceUser.get().toString());

    }
}
