package com.atguigu.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @auther zzyy
 * @create 2021-03-01 21:38
 */

@Slf4j
public class T1
{
    static  ThreadLocal<Book> th = new ThreadLocal<>();

    public void set(Book book) {
        th.set(book);
        System.out.println("当前线程"+Thread.currentThread().getName() + "已经set了变量值,当前变量值为" + th.get());
    }
    public void get(){
        System.out.println("当前线程" + Thread.currentThread().getName() + "获取到的变量值为" + th.get());
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
        Book book = new Book();
        T1 t1 = new T1();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                book.setBookName("寄为何寄第"+finalI+"版");
                book.setAuthor("二将军");
                book.setId(finalI);
                book.setPrice(111 + finalI);

                try {
                    t1.set(book);
                    t1.get();
                }catch (Exception e){

                }finally {
                    th.remove();

                }
            },String.valueOf(i)).start();
        }


/*
        new Thread(() -> {
            try {
                for (int i = 1; i <=300; i++) {
                    t1.m1();
                }
            } finally {
//                t1.threadLocal.remove();
            }
            System.out.println(t1.threadLocal.get());
        },"a").start();

        new Thread(() -> {
            try {
                for (int i = 1; i <=5; i++) {
                    t1.m1();
                }
                System.out.println(t1.threadLocal.get());
            } finally {
                t1.threadLocal.remove();
            }
        },"b").start();*/

    }
}
