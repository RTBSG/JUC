package com.atguigu.juc.atomics;

import lombok.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * juc包下实际就两个原子类解决aba问题 AtomicStampedReference和AtomicMarkableReference
 * @auther zzyy
 * @create 2021-03-22 14:14
 */
public class AtomicMarkableReferenceDemo
{

    static Student st =new Student(1,"zs","12");
    static Student st1 =new Student(2,"ww","12");


    static AtomicStampedReference atomicStampedReference = new AtomicStampedReference(100,1);
    static AtomicStampedReference asr = new AtomicStampedReference(st,1);

    static AtomicMarkableReference atomicMarkableReference = new AtomicMarkableReference(100,false);
//    原子变更的是由 AtomicMarkableReference维护的引用对象,并不少去改变原始对象的值
   static   AtomicMarkableReference amr = new AtomicMarkableReference(st,false);

    public static void main(String[] args)
    {    new Thread(() -> {
        System.out.println("当前线程" + Thread.currentThread().getName() + "初始值为" + asr.getReference() + "初始标记为" + asr.getStamp());
        boolean b = asr.compareAndSet(st, st1, 1, 2);
        System.out.println("当前线程" + Thread.currentThread().getName() + "值为" + asr.getReference() + "标记为" + asr.getStamp());
    },"atomic").start();

        new Thread(() -> {
            System.out.println("当前线程" + Thread.currentThread().getName() + "初始值为" + asr.getReference() + "初始标记为" + asr.getStamp());
            boolean b = asr.compareAndSet(st1, 100, 2, 3);
            System.out.println("当前线程" + Thread.currentThread().getName() + "值为" + asr.getReference() + "标记为" + asr.getStamp());
        },"atomic1").start();




        new Thread(() -> {
            System.out.println("当前线程" + Thread.currentThread().getName() + "初始值为" + atomicStampedReference.getReference() + "初始标记为" + atomicStampedReference.getStamp());
            boolean b = atomicStampedReference.compareAndSet(100, 101, 1, 2);
            System.out.println("当前线程" + Thread.currentThread().getName() + "值为" + atomicStampedReference.getReference() + "标记为" + atomicStampedReference.getStamp());
        },"atomic").start();

        new Thread(() -> {
            System.out.println("当前线程" + Thread.currentThread().getName() + "初始值为" + atomicStampedReference.getReference() + "初始标记为" + atomicStampedReference.getStamp());
            boolean b = atomicStampedReference.compareAndSet(101, 100, 2, 3);
            System.out.println("当前线程" + Thread.currentThread().getName() + "值为" + atomicStampedReference.getReference() + "标记为" + atomicStampedReference.getStamp());
        },"atomic1").start();



        new Thread(()->{
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值"+amr.getReference()+"获取到的原始标记"+amr.isMarked());
            boolean ww = amr.compareAndSet(st, st1, false, true);
            System.out.println("线程" + Thread.currentThread().getName() + "正在修改原始值;是否修改成功" + ww + "最新值为" + amr.getReference()+"最新标记为"+amr.isMarked());
        },"student").start();


        new Thread(()->{
            System.out.println("当前线程"+Thread.currentThread().getName()+"获取到的原始值"+amr.getReference()+"获取到的原始标记"+amr.isMarked());
            boolean ww = amr.compareAndSet(st1, st, true, false);
            System.out.println("线程" + Thread.currentThread().getName() + "正在修改原始值;是否修改成功" + ww + "最新值为" + amr.getReference()+"最新标记为"+amr.isMarked());
        },"student1").start();


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