**atomic原子类**

        通过原子类对原本不安全的属性进行包装返回一个原子类型的引用、
    
        通过操作原子类型的引用来保证数据的安全性.
    
        AtomicInteger,AtomicReference,AtomicReferenceFieldUpdater...
    
        等等这些atomic类底层都使用了cas因此都保证了原子性,一致性,可见性.
    
        但是仍然会有ABA问题的产生.于是引出下边两个类
    
        AtomicStampedReference和AtomicMarkableReference
    
        通过附带标记来解决ABA,两者用法基本一致,
    
        区别在于一个是通过boolean类型的mark区分,一个是通过版本号进行区分
    
        atomicMarkableReference.compareAndSet(期望值, 变更值, 期望标记, 更新标记);
    
        atomicStampedReference.compareAndSet(101, 100, 2, 3);

**守护线程**

        将一个线程设置为setDaemon(true); 所有线程结束守护线程也会退出.

**cas自旋锁**
    
        没什么好说的,太熟悉了.比较并交换,无锁并发,适用于轻量级并发

**CompletableFuture 异步编排**

    结论 cpu核心不够不要使用,高并发下用一个爆一个
    
        原因 默认多核( n-1 > 1)ForkJoinPool.commonPool(),单核ThreadPerTaskExecutor 单核一个任务new一个新线程
    
        此外还需要注意⚠️ 异步线程虽然是编排的,但是主线程不是
    
        会产生一个基础问题主线程在异步任务未完成的时候直接结束了,导致数据无法完整返回
    
        用join或者组合等待所有线程完成在进行返回
    
        异步编排适用于逻辑简单,吞吐量大的场景

    线程创建分析

        单个进程最多创建线程数在不同位数的操作系统下是不同的

        32位一个线程虚拟内存4g 用户分配3g 栈内存分配8m 加上其他占用大概10m 3000/10= 300 左右
    
        64位一线程虚拟内存128T 因此理论可以创建1000多万
    
        实际受到系统内核限制

        /proc/sys/kernel/threads-max，表示系统支持的最大线程数，默认值是 14553；
    
        /proc/sys/kernel/pid_max，表示系统全局的 PID 号数值的限制，每一个进程或线程都有 ID，ID 的值超过这个数，进程或线程就会创建失败，默认值是 32768；
    
        /proc/sys/vm/max_map_count，表示限制一个进程可以拥有的VMA(虚拟内存区域)的数量，具体什么意思我也没搞清楚，反正如果它的值很小，也会导致创建线程失败，默认值是 65530。

   **异步编排使用**

    一、创建异步任务

        1、Future.submit
        
            通常的线程池接口类ExecutorService，其中execute方法的返回值是void
        
            即无法获取异步任务的执行状态，3个重载的submit方法的返回值是Future，
        
            可以据此获取任务执行的状态和结果

        2、supplyAsync / runAsync

            supplyAsync表示创建带返回值的异步任务
        
            runAsync表示创建无返回值的异步任务 这两个都重载了线程池
        
          

    二、异步回调
    
        1、thenApply / thenApplyAsync
    
            thenApply 表示某个任务执行完成后执行的动作，即回调方法，会将该任务
        
            的执行结果即方法返回值作为入参传递到回调方法中 两者同一个线程执行
        
            thenApplyAsync 是线程池异步执行

        2、thenAccept / thenRun
    
            thenAccept 同 thenApply 接收上一个任务的返回值作为参数，
        
            但是无返回值；thenRun 的方法没有入参，也没有返回值
        
            同样有异步版本thenAcceptAsync/thenRunAsync
    
        3、 exceptionally
    
            exceptionally方法指定某个任务执行异常时执行的回调方法，会将抛出异常
        
            作为参数传递到回调方法中，如果该任务正常执行则会exceptionally方法返
        
            回的CompletionStage的result就是该任务正常执行的结果

        4、whenComplete
    
            whenComplete是当某个任务执行完成后执行的回调方法，会将执行结果
        
            或者执行期间抛出的异常传递给回调方法，如果是正常执行则异常为null，
        
            回调方法对应的CompletableFuture的result和该任务一致，如果该任务正常
        
            执行，则get方法返回执行结果，如果是执行异常，则get方法抛出异常

        5、handle
    
            跟whenComplete基本一致，区别在于handle的回调方法有返回值，
        
            且handle方法返回的CompletableFuture的result是回调方法的执行结果
        
            或者回调方法执行期间抛出的异常，与原始CompletableFuture的result无关

    三、组合处理

        1、thenCombine / thenAcceptBoth / runAfterBoth

            这三个方法都是将两个CompletableFuture组合起来，只有这两个都正常执行
        
            完了才会执行某个任务，区别在于，thenCombine会将两个任务的执行结果
        
            作为方法入参传递到指定方法中，且该方法有返回值；thenAcceptBoth同样
        
            将两个任务的执行结果作为方法入参，但是无返回值；runAfterBoth没有入参，也没有返回值。
        
            注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。
        
            `cf.thenCombine(cf1,(cf,cf1)->{ return cf+cf1;})`

        2、applyToEither / acceptEither / runAfterEither
    
            这三个方法都是将两个CompletableFuture组合起来，只要其中一个执行完了
        
            就会执行某个任务，谁先执行完成谁的结果作为参数,其区别
        
            applyToEither会将已经执行完成的任务的执行结果作为方法入参有返回值；
        
            acceptEither将已经执行完成的任务的执行结果作为方法入参，没有返回值；
        
            runAfterEither没有方法入参,也没有返回值。
        
            注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。

        3、thenCompose
    
            thenCompose方法会在某个任务执行完成后，将该任务的执行结果作为方法入参
        
            然后执行指定的方法，该方法会返回一个新的CompletableFuture实例，
        
            如果该CompletableFuture实例的result不为null，则返回一个基于该result的
        
            新的CompletableFuture实例；如果该CompletableFuture实例为null
            
        4、allOf / anyO
    
            fallOf返回的CompletableFuture是多个任务都执行完成后才会执行，
        
            只有有一个任务执行异常，则返回的CompletableFuture执行get方法时
        
            会抛出异常，如果都是正常执行，则get返回null

**线程中断**

    线程停止一般有三种状态 正常退出,暴力停止,interrupt异常停止

    暴力停止也就是stop() 方法因为数据不安全,已弃用

    interrupt()并不是真的去停止线程 它基于「一个线程不应该由其他线程来强制中断或停止，而是应该由线程自己自行停止。

    interrupt() 中断线程
    
        interrupt 操作不会打断所有阻塞，只有下面阻塞情况才在jvm的打断范围内，如处于锁阻塞的线程，不会受 interrupt 中断；

        阻塞情况下中断，抛出异常后线程恢复非中断状态，即 interrupted = false 也就是非中断

        interrupt 中断操作时，非自身打断需要先检测是否有中断权限，这由jvm的安全机制配置；

        如果线程处于sleep, wait, join 等状态，那么线程将立即退出被阻塞状态，并抛出一个InterruptedException异常；
        
        如果线程处于I/O阻塞状态，将会抛出ClosedByInterruptException（IOException的子类）异常；
        
        如果线程在Selector上被阻塞，select方法将立即返回；

        如果非以上情况，将直接标记 interrupt 状态；


    interrupted() 查看线程的中断状态,并且清除线程的标记位 先返回标记位然后在清除标记位

    isInterrupted() 判断当前线程是否被中断 不会清除标记位
    
锁机制
    
    可重入锁

        可重复可递归调用的锁 递归锁，在外层使用锁之后，在内层仍然可以使用并且不发生死锁 reent系列锁,synchronized 都说可重入锁

    锁粗化

        假如方法中首尾相接，前后相邻的都是同一个锁对象，那JIT编译器就会把这几个synchronized块合并成一个大块，
        加粗加大范围，一次申请锁使用即可，避免次次的申请和释放锁，提升了性能

    锁消除

        从JIT角度看相当于无视它，synchronized (o)不存在了,这个锁对象并没有被共用扩散到其它线程使用，
        因为没有其他线程使用到这个锁,实际上就当作单线程使用,极端的说就是根本没有加这个锁对象的底层机器码，消除了锁的使用
    
    锁降级

        遵循获取写锁→再获取读锁→再释放写锁的次序，写锁能够降级成为读锁。
        锁降级能提高性能,读锁是可以多线程同时持有的,写锁释放掉后转为读锁其他线程即可持有读锁

       
    主要三把锁 ReentrantLock ReentrantReadWriteLock StampedLock

    ReentrantLock
        
        ReentrantLock和synchronized 基本一致 区别ReentrantLock更灵活 需要手动解锁
        读读、读写、写写全部互斥是把完全互斥的锁效率不高,默认非公平锁
        
    ReentrantReadWriteLock
        ReentrantReadWriteLock 对ReentrantLock进行增强 读读共享 读写,写写互斥

    StampedLock
        
        StampedLock性能强 底层CLH自旋锁,但是不可重入,不能设置条件变量.同时也不可以进行线程中断.中断永久堵塞
    
        他有ReentrantReadWriteLock的读写锁同时支持乐观读,并发读的同时进行获取写锁 实测乐观读在没有写锁占用的时候,获取到的stamp状态不属于锁状态
        
        测试进行解锁会线程异常.同时需要验证stamp的写锁是否被占用,一旦被占用应当转为普通的读写锁,重新获取数据 实际使用中需要注意下