计算机的CPU执行效率很高，大多情况都在等待，为了提高执行效率，可以多线程执行任务，物理硬件上多核多线程已经支持。

一个应用由一个或多个进程组成，一个进程由一个或多个线程组成，（虚拟线程只是虚拟的最终还是一个线程）。
进程：操作系统为每个进程分配独立的内存、文件句柄等资源。进程之间相互独立，通信复杂。一个进程默认提供一条线程。
线程是CPU调度的基本单位。线程之间共享进程的资源，如内存、文件句柄等。一个线程不能独立的存在，它必须是进程的一部分。

JVM底层会把Java线程都映射到一个操作系统的线程上。

1.8引入的CompletableFuture它不仅实现了Future接口，还实现了CompletionStage函数式编程、异步任务编排组合接口。

CompletableFuture提供了方法大约有50多个，单纯一个个记忆，是很麻烦的，因此将其划分为以下几类：
创建类

* completeFuture 可以用于创建默认返回值
* runAsync 异步执行，无返回值
* supplyAsync 异步执行，有返回值
* anyOf 任意一个执行完成，就可以进行下一步动作
* allOf 全部完成所有任务，才可以进行下一步任务

状态取值类

* join 合并结果，等待
* get 合并等待结果，可以增加超时时间;get和join区别，join只会抛出unchecked异常，get会返回具体的异常
* getNow 如果结果计算完成或者异常了，则返回结果或异常；否则，返回valueIfAbsent的值
* isCancelled
* isCompletedExceptionally
* isDone

控制类 用于主动控制CompletableFuture的完成行为

* complete
* completeExceptionally
* cancel

接续类 CompletableFuture 最重要的特性，没有这个的话，CompletableFuture就没意义了，用于注入回调行为。

* thenApply, thenApplyAsync 第一个任务执行完成后，执行第二个回调方法任务，会将该任务的执行结果，作为入参，传递到回调方法中，并且回调方法是有返回值的。
* thenAccept, thenAcceptAsync 第一个任务执行完成后，执行第二个回调方法任务，会将该任务的执行结果，作为入参，传递到回调方法中，但是回调方法是没有返回值的。
* thenRun, thenRunAsync 做完第一个任务后，再做第二个任务,这两个任务没有关联关系,第二个任务也没有返回值。
* thenCombine, thenCombineAsync 组合2个任务，都结束后才进行下阶段任务,两个任务的执行结果作为方法入参,有返回值
* thenAcceptBoth, thenAcceptBothAsync 组合2个任务，都结束后才进行下阶段任务,两个任务的执行结果作为方法入参,没有返回值
* runAfterBoth, runAfterBothAsync 组合2个任务，都结束后才进行下阶段任务,没有入参，没有返回值
* thenCompose, thenComposeAsync
* applyToEither, applyToEitherAsync 当任意一个CompletableFuture完成的时候，action这个消费者就会被执行,有返回值。
* acceptEither, acceptEitherAsync 当任意一个CompletableFuture完成的时候，action这个消费者就会被执行,没有返回值。
* runAfterEither, runAfterEitherAsync
* whenComplete, whenCompleteAsync 当某个任务执行完成后执行的回调方法，会将执行结果或者执行期间抛出的异常传递给回调方法
* handle, handleAsync当某个任务执行完成后执行的回调方法，会将执行结果或者执行期间抛出的异常传递给回调方法+返回值
* exceptionally

上面的方法很多，我们没必要死记硬背，按照如下规律，会方便很多，记忆规则：

* 以Async结尾的方法，都是异步方法，对应的没有Async则是同步方法，一般都是一个异步方法对应一个同步方法。
* 以Async后缀结尾的方法，都有两个重载的方法，一个是使用内容的forkjoin线程池，一种是使用自定义线程池
* 以run开头的方法，其入口参数一定是无参的，并且没有返回值，类似于执行Runnable方法。
* 以supply开头的方法，入口也是没有参数的，但是有返回值
* 以Accept开头或者结尾的方法，入口参数是有参数，但是没有返回值
* 以Apply开头或者结尾的方法，入口有参数，有返回值
* 带有either后缀的方法，表示谁先完成就消费谁