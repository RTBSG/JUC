package com.atguigu.juc.atomics;

import lombok.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @auther zzyy
 * @create 2021-03-22 14:14
 */
public class AtomicMarkableReferenceDemo
{
    static Student st =new Student(1,"zs","12");

    static AtomicMarkableReference atomicMarkableReference = new AtomicMarkableReference(100,false);
   static   AtomicMarkableReference amr = new AtomicMarkableReference(st,false);

    public static void main(String[] args)
    {

        new Thread(()->{
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值"+amr.getReference()+"获取到的原始标记"+amr.isMarked());
            amr.compareAndSet(st, new Student(2, "ww", "22"), false, true);
            System.out.println("线程"+Thread .currentThread().getName()+"正在修改原始值;是否修改成功"+amr.isMarked()+"最新值为"+amr.getReference());


        },"student").start();
        new Thread(()->{
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值"+atomicMarkableReference.getReference()+"获取到的原始标记"+atomicMarkableReference.isMarked());
            boolean marked = atomicMarkableReference.isMarked();
            atomicMarkableReference.compareAndSet(100,101,marked,!marked);
            System.out.println("线程"+Thread .currentThread().getName()+"正在修改原始值;是否修改成功"+atomicMarkableReference.isMarked()+"最新值为"+atomicMarkableReference.getReference());
        },"tttt").start();
        new Thread(() -> {
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值"+atomicMarkableReference.getReference()+"获取到的原始标记"+atomicMarkableReference.isMarked());
            boolean marked = atomicMarkableReference.isMarked();
            atomicMarkableReference.compareAndSet(100,102,marked,!marked);
            },"t1").start();


        new Thread(() -> {
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值"+atomicMarkableReference.getReference()+"获取到的原始标记"+atomicMarkableReference.isMarked());
            boolean marked = atomicMarkableReference.isMarked();
            atomicMarkableReference.compareAndSet(100,103,marked,!marked);


        },"t2").start();
    }


}

@Data
@ToString
@AllArgsConstructor
class Student{
    @NonNull
    Integer id;
    String  name;
    String age;





}