package com.atguigu.juc.cf;

import java.util.concurrent.*;

/**
 * @auther zzyy
 * @create 2021-03-02 17:54
 */
public class CompletableFutureAPIDemo
{
    public static void main(String[] args) throws Exception {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
/*
//       supplyAsync 带返回值的异步任务的 可指定线程池重载
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 1;
    */
/*     thenApply上个任务执行完成后执行回调方法,并将上个结果作为入参执行,线程使用和上个任务同一个线程,
            而thenApplyAsync 是新建一个线程异步执行 , 可指定线程池进行重载
            thenAccept / thenRun
            thenAccept 同 thenApply 接收上一个任务的返回值作为参数，但是无返回值；
            thenRun 的方法没有入参，也没有返回值*//*

        }).thenApply(f -> {
                   return f + 2;
//            throw new RuntimeException("runtime");

  */
/*          *  whenComplete是当某个任务执行完成后执行的回调方法，
            * 会将执行结果或者执行期间抛出的异常传递给回调方法，如果是正常执行则异常为null，
            * 回调方法对应的CompletableFuture的result和该任务一致，如果该任务正常执行，
               他本身是没有返回值的 ,如果需要返回值使用handle
            * 则get方法返回执行结果是上一层的结果，如果是执行异常，则get方法抛出异常
*//*

        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("0-------result: " + v);
            }else{
                System.out.println("输出异常"+e.getMessage());
            }
 */
/*          exceptionally方法指定某个任务执行异常时执行的回调方法，会将抛出异常作为参数传递到回调方法中，
           如果该任务正常执行则会exceptionally方法返回的CompletionStage的result就是该任务正常执行的结果
           他是针对异常的方法,正常是不会执行的,一旦他执行后 后边的方法仍会正常执行*//*

        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        }).thenApply(
                f->{
                    return "正常执行了";
                }
        ).join());
*/

test9();



        threadPoolExecutor.shutdown();
    }

    public static void test12() throws Exception {
        // 创建异步执行任务:
        CompletableFuture cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start job1,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job1,time->"+System.currentTimeMillis());
            return 1.2;
        });
        CompletableFuture cf2 = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start job2,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job2,time->"+System.currentTimeMillis());
            return 3.2;
        });
        CompletableFuture cf3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
//            throw new RuntimeException("test");

            return 2.2;
        });
        //allof等待所有任务执行完成才执行cf4，如果有一个任务异常终止，则cf4.get时会抛出异常，都是正常执行，cf4.get返回null
        //anyOf是只有一个任务执行完成，无论是正常执行或者执行异常，都会执行cf4，cf4.get的结果就是已执行完成的任务的执行结果
        CompletableFuture cf4=CompletableFuture.allOf(cf,cf2,cf3).whenComplete((a,b)->{
            if(b!=null){
                System.out.println("error stack trace->"+b);

            }else{
                System.out.println("run succ,result->"+a);
            }
        });

        System.out.println("main thread start cf4.get(),time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("cf4 run result->"+cf4.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }

    /**
     * thenCompose某个任务执行完成后，将该任务的执行结果作为方法入参然后执行指定的方法，
     * param CompletableFuture的执行结果
     * return 该方法会返回一个新的CompletableFuture实例
     * @throws Exception
     */
    public static void test9() throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start job1,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job1,time->"+System.currentTimeMillis());
//            throw new RuntimeException("test");
            return 1.2;
        });
//        thenCompose任务执行完成后，将该任务的执行结果作为方法入参然后执行指定的方法，
//        该方法会返回一个新的CompletableFuture实例
//
        CompletableFuture<String> cf2= cf.thenCompose((param)->{
            System.out.println(Thread.currentThread()+" start job2,time->"+System.currentTimeMillis());
            System.out.println("param" + param);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job2,time->"+System.currentTimeMillis());
            return CompletableFuture.supplyAsync(()->{
                System.out.println(Thread.currentThread()+" start job3,time->"+System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                System.out.println(Thread.currentThread()+" exit job3,time->"+System.currentTimeMillis());
                return "job3 test";
            });
        });
        System.out.println("main thread start cf.get(),time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("cf run result->"+cf.get());
        System.out.println("main thread start cf2.get(),time->"+System.currentTimeMillis());
        System.out.println("cf2 run result->"+cf2.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }



    public  static void test8() throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start job1,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread()+" exit job1,time->"+System.currentTimeMillis());
            return 1.2;
        });
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start job2,time->"+System.currentTimeMillis());

            System.out.println(Thread.currentThread()+" exit job2,time->"+System.currentTimeMillis());
            return 3.2;
        });
        //cf和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3,且有返回值
        // 注意这里的result是cf还是cf2 取决于他们两个谁先执行完,丢弃某些超时请求
        CompletableFuture<Double> cf3=cf.applyToEither(cf2,(result)->{
            System.out.println(Thread.currentThread()+" start job3,time->"+System.currentTimeMillis());
            System.out.println("job3 param result->"+result);
            System.out.println(Thread.currentThread()+" exit job3,time->"+System.currentTimeMillis());
            return result;
        });


        //cf和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3,无返回值
        CompletableFuture cf4=cf.acceptEither(cf2,(result)->{
            System.out.println(Thread.currentThread()+" start job4,time->"+System.currentTimeMillis());
            System.out.println("job4 param result->"+result);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job4,time->"+System.currentTimeMillis());
        });

        //cf4和cf3都执行完成后，执行cf5，无入参，无返回值
        CompletableFuture cf5=cf4.runAfterEither(cf3,()->{
            System.out.println(Thread.currentThread()+" start job5,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("cf5 do something");
            System.out.println(Thread.currentThread()+" exit job5,time->"+System.currentTimeMillis());
        });



        System.out.println("main thread start cf.get(),time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("cf run result->"+cf.get());
        System.out.println("main thread start cf5.get(),time->"+System.currentTimeMillis());
        System.out.println("cf5 run result->"+cf5.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }

    /**
     * 将两个CompletableFuture组合起来，只要其中一个执行完了就会执行某个任务，
     * applyToEither 有参数有返回值 ⚠️谁先执行完参数就是谁
     * acceptEither   有参无返回值
     * runAfterEither   无参无返回值
     * @throws Exception
     */
    public static void test2 ()throws Exception{
        CompletableFuture cf = CompletableFuture.supplyAsync(
                        ()->{
                            System.out.println("进入异步任务1,开始执行");
                            int a = 1;
                            int b = 2;

                            System.out.println("结束异步任务1,开始退出");
                            if (true) {
                                throw new RuntimeException("异步任务出现了异常1");
                            }
                            return a + b;
                        })
                .exceptionally((e)->{
                    if (e != null) {
                        System.out.println("异步任务1出现了异常"+e);
                        return 10;
                    }else{
                        return 2;
                    }})
                .handle((a,e)->{
                    System.out.println("进入异步任务1的handle,当前获取到的参数值为" + a);
                    if (e != null) {
                        a += 1;
                        System.out.println("结束异步任务1的handle,当前获取到的参数值为" + a);
                        return a;
                    }else{
                        a -= 1;
                        System.out.println("结束异步任务1的handle,当前获取到的参数值为" + a);
                        return a;
                    }
                });
        CompletableFuture cf2=CompletableFuture.supplyAsync(() -> {
                    {
                        System.out.println("进入异步任务2,开始执行");

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("结束异步任务2,准备退出");
                        return 3;
                    }
                }).whenComplete((a, e) -> {
                    System.out.println("进入异步任务2的whenComplete,当前获取到的参数值为" + a);
                    if (e != null) {
                        System.out.println("异步任务2的whenComplete捕捉到了异常" + e);
                    }
                    System.out.println("结束异步任务2的whenComplete,当前获取到的参数值为" + a);

                });
        cf.applyToEither(cf2, (result) -> {
            System.out.println("当前参数的值为" + result);

            return result;
        });


    }

    /**
     * thenCombine 有参数有返回值
     * thenAcceptBoth 有参数无返回值
     * runAfterBoth  无参数无返回值
     * 两个任务执行完成后在汇总执行
     * @throws Exception
     */
    public static void test1 ()throws Exception{
        CompletableFuture cf = CompletableFuture.supplyAsync(
                ()->{
                    System.out.println("进入异步任务1,开始执行");
                    int a = 1;
                    int b = 2;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("结束异步任务1,开始退出");
                    if (true) {
                        throw new RuntimeException("异步任务出现了异常1");
                    }
                        return a + b;
                })
        .exceptionally((e)->{
            if (e != null) {
                System.out.println("异步任务1出现了异常"+e);
                return 10;
            }else{
                return 2;
            }})
        .handle((a,e)->{
            System.out.println("进入异步任务1的handle,当前获取到的参数值为" + a);
            if (e != null) {
                a += 1;
                System.out.println("结束异步任务1的handle,当前获取到的参数值为" + a);
                return a;
            }else{
                a -= 1;
                System.out.println("结束异步任务1的handle,当前获取到的参数值为" + a);
                return a;
            }
        }).thenCombine( CompletableFuture.supplyAsync(() -> {
                            {
                                System.out.println("进入异步任务2,开始执行");
                                System.out.println("结束异步任务2,准备退出");
                                return 3;
                            }
                        }).whenComplete((a, e) -> {
                            System.out.println("进入异步任务2的whenComplete,当前获取到的参数值为" + a);
                            if (e != null) {
                                System.out.println("异步任务2的whenComplete捕捉到了异常" + e);
                            }
                            System.out.println("结束异步任务2的whenComplete,当前获取到的参数值为" + a);

                        }),(a,b)->{
                    System.out.println("异步任务1的结果"+a+"异步任务2的结果"+b+"合并结果"+(a+b));
                    return a + b;
        }).thenAccept(e->{
                    System.out.println("全部流程处理结束最终结果"+e);
                })
                ;






    }

    public static void test7() throws Exception {
        ForkJoinPool pool=new ForkJoinPool();
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start job1,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job1,time->"+System.currentTimeMillis());
            return 1.2;
        });
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start job2,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job2,time->"+System.currentTimeMillis());
            return 3.2;
        });
        //cf和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3,且有返回值
        CompletableFuture<Double> cf3=cf.thenCombine(cf2,(a,b)->{
            System.out.println(Thread.currentThread()+" start job3,time->"+System.currentTimeMillis());
            System.out.println("job3 param a->"+a+",b->"+b);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job3,time->"+System.currentTimeMillis());
            return a+b;
        });

        //cf和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3,无返回值
        CompletableFuture cf4=cf.thenAcceptBoth(cf2,(a,b)->{
            System.out.println(Thread.currentThread()+" start job4,time->"+System.currentTimeMillis());
            System.out.println("job4 param a->"+a+",b->"+b);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread()+" exit job4,time->"+System.currentTimeMillis());
        });

        //cf4和cf3都执行完成后，执行cf5，无入参，无返回值
        CompletableFuture cf5=cf4.runAfterBoth(cf3,()->{
            System.out.println(Thread.currentThread()+" start job5,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("cf5 do something");
            System.out.println(Thread.currentThread()+" exit job5,time->"+System.currentTimeMillis());
        });

        System.out.println("main thread start cf.get(),time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("cf run result->"+cf.get());
        System.out.println("main thread start cf5.get(),time->"+System.currentTimeMillis());
        System.out.println("cf5 run result->"+cf5.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }

    public static void test11() throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+"job1 start,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if(true){
                throw new RuntimeException("test");
            }else{
                System.out.println(Thread.currentThread()+"job1 exit,time->"+System.currentTimeMillis());
                return 1.2;
            }
        });
        //cf执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
        CompletableFuture<String> cf2=cf.handle((a,b)->{
            System.out.println(Thread.currentThread()+"job2 start,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if(b!=null){
                System.out.println("error stack trace->");
                b.printStackTrace();
                return "msg1";
            }else{
                System.out.println("run succ,result->"+a);
                return "msg2";
            }

        });
        //等待子任务执行完成
        System.out.println("main thread start wait,time->"+System.currentTimeMillis());
        //get的结果是cf2的返回值，跟cf没关系了
        System.out.println("run result->"+cf2.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }

    public  static void test10() throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+"job1 start,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if(true){
                throw new RuntimeException("test1的异常");
            }else{
                System.out.println(Thread.currentThread()+"job1 exit,time->"+System.currentTimeMillis());
                return 1.2;
            }
        });
        //cf执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
        CompletableFuture<Double> cf2=cf.whenComplete((a,b)->{
            System.out.println(Thread.currentThread()+"job2 start,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if(b!=null){
                System.out.println("error 2 stack trace->");
                b.printStackTrace();
            }else{
                System.out.println("run 2 succ,result->"+a);
            }
            System.out.println(Thread.currentThread()+"job2 exit,time->"+System.currentTimeMillis());
        });
        //等待子任务执行完成
        System.out.println("main thread start wait,time->"+System.currentTimeMillis());
        //如果cf是正常执行的，cf2.get的结果就是cf执行的结果
        //如果cf是执行异常，则cf2.get会抛出异常
        System.out.println("run result->"+cf2.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }

    public  static void m6() throws Exception{

            ForkJoinPool pool=new ForkJoinPool();
            // 创建异步执行任务:
            CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
                System.out.println(Thread.currentThread()+"job1 start,time->"+System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                if(false){
                    throw new RuntimeException("test");
                }else{
                    System.out.println(Thread.currentThread()+"job1 exit,time->"+System.currentTimeMillis());
                    return 1.2;
                }
            },pool);
            //cf执行异常时，将抛出的异常作为入参传递给回调方法
            CompletableFuture<Double> cf2= cf.exceptionally((param)->{
                System.out.println(Thread.currentThread()+" start,time->"+System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                System.out.println("error stack trace->");
                param.printStackTrace();
                System.out.println(Thread.currentThread()+" exit,time->"+System.currentTimeMillis());
                return -1.1;
            });
            //cf正常执行时执行的逻辑，如果执行异常则不调用此逻辑
            CompletableFuture cf3=cf.thenAccept((param)->{
                System.out.println(Thread.currentThread()+"job2 start,time->"+System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                System.out.println("param->"+param);
                System.out.println(Thread.currentThread()+"job2 exit,time->"+System.currentTimeMillis());
            });
            System.out.println("main thread start,time->"+System.currentTimeMillis());
            //等待子任务执行完成,此处无论是job2和job3都可以实现job2退出，主线程才退出，如果是cf，则主线程不会等待job2执行完成自动退出了
            //cf2.get时，没有异常，但是依然有返回值，就是cf的返回值 这里cf2 针对异常才会执行,没有异常时不会执行但是会有返回值 返回未出异常的结果
            System.out.println("run result->"+cf2.get());
//            主线程调用异常被干掉了
            System.out.println("main thread exit,time->"+System.currentTimeMillis());
        }


    /**
     * thenCombine
     */
    public static void m5()
    {
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            return 20;
        }), (r1, r2) -> {
            return r1 + r2;
        }).join());
    }

    /**
     * 对计算速度进行选用
     */
    public static void m4()
    {
        System.out.println(CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        }), r -> {
            return r;
        }).join());

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    /**
     * 对计算结果进行消费
     */
    public static void m3()
    {
        CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApply(f -> {
            return f+2;
        }).thenApply(f -> {
            return f+3;
        }).thenAccept(r -> System.out.println(r));


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {}).join());


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB").join());
    }

    /**
     * 对计算结果进行处理
     */
    public static void m2()
    {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 1;
        }).handle((f,e) -> {
            System.out.println("-----1");
            return f + 2;
        }).handle((f,e) -> {
            System.out.println("-----2");
            return f + 3;
        }).handle((f,e) -> {
            System.out.println("-----3");
            return f + 4;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----result: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        }).join());


        threadPoolExecutor.shutdown();
    }

    /**
     * 获得结果和触发计算
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void m1() throws InterruptedException, ExecutionException
    {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            return 1;
        },threadPoolExecutor);

        //System.out.println(future.get());
        //System.out.println(future.get(2L,TimeUnit.SECONDS));
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        //System.out.println(future.getNow(9999));

        System.out.println(future.complete(-44)+"\t"+future.get());


        threadPoolExecutor.shutdown();
    }
}
