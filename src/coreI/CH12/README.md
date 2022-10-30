# Java并发编程整合

整合Notion笔记中的**Java多线程**和**线程池源码分析**笔记，以及OneDirve/Posts中的**Java并发**笔记。

其原因是，有段时间**Java并发**笔记丢失，于是又重新在Notion中写了一份，内容大体相似，又各有侧重，因此此处做一个总结，上传至`java-core`项目中。

leqing 于 2022.10.29

## 线程的基础概念

### 1. 线程与进程

![进程和线程关系图](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/进程和线程关系图.png)

在Java中，当我们启动main函数时，其实就启动了一个JVM的进程，而man函数所在的线程就是这个进程的一个线程，也成为主线程。

一个进程有多个线程，线程共享进程的堆和方法区资源，但每个线程有自己的程序计数器和栈区域。

> 程序计数器pc指向执行地址，如果执行的是native方法，则pc记录的是undefined地址

简单介绍下各个地方存放了啥：

- 堆：进程中最大的一块内存，进程创建时被分配，主要存放使用new操作创建的对象实例
- 方法区：存放JVM加载的类、常量及静态变量等信息
- 程序计数器：线程的目前的执行地址
- 栈：线程的局部变量、调用栈帧

### 2. 线程上下文切换

线程数一般都大于CPU个数，而单个CPU同一时间只能被一个线程使用，为了让用户感觉多个线程在同时运行，CPU资源分配采用时间片轮转的策略，给每个线程分配一个时间片。

该时间片结束后，该线程需要让出资源给其他线程，此时需要切换上下文。

切换时机如下：

- 当前线程的CPU时间片使用完处于就绪状态时
- 当前线程被其他线程中断时

### 3. 线程死锁

死锁的四个必要条件：

- 请求与保持
- 环路等待
- 互斥
- 不可剥夺

根据操作系统的知识，只有请求与保持、环路等待是可以被破坏的。

### 4. 守护线程与用户线程

Java线程分为两类：

- daemon线程（守护线程）
- user线程（用户线程）

当JVM启动时会调用main函数，main函数所在的线程是一个用户线程，其实在JVM内部同时还启动了好多守护线程，比如垃圾回收线程。而当main函数运行结束后，JVM会自动启动一个叫做DestroyJavaVM的线程，该线程会等待所有用户线程结束后终止JVM进程。

**二者的区别是，守护线程与JVM的退出无关，而只要存在一个未结束的用户线程，那么正常情况下JVM就不会退出！**

如何创建一个守护进程？设置线程的daemon参数为true即可。

```java
public class DaemonThreadTest  {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Thread daemonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // do somethings
            }
        });
        daemonThread.setDaemon(true); // 设置为守护进程
        daemonThread.start();
    }
}
```

### 5. 多线程并发编程

- 并发：多个任务在同一个时间段内同时执行，一个时间段包含多个时间单位。
- 并行：多个任务在同一个时间单位内同时执行。

由此可知，在单CPU时代，只有并发而无并行，而在多核时代，并行成为可能。

但是，在多线程编程实践中，线程的个数往往多于CPU的个数，所以一般称为多线程并发编程。

**为什么要多线程？**

因为多个CPU意味着每个线程可以使用自己的CPU运行，减少了上下文切换的开销，而对应用系统性能和吞吐量的要求的提高，使得多线程编程成为大趋势。

### 6. 锁的概述

根据对数据被外界修改的态度是否保守：

- 悲观锁：对数据被外界修改持保守态度，认为数据很容易就会被其他线程修改，因此在数据被处理之前对数据进行加锁，并且在整个 处理过程中，使数据处于锁定状态。
- 乐观锁：相对悲观锁而言，认为数据在一般情况下不会造成冲突，因此在访问记录前不会加排它锁，而是在进行数据提交更新时，才会正式对数据冲突与否进行检测。

根据线程获取锁的抢占机制是否公平：

- 公平锁：先到先得
- 非公平锁：先到未必先得，无公平性需求下尽量使用这个，公平锁有性能开销。

根据锁是否可以被多个线程共同持有：

- 独占锁：任何时候只能有一个线程得到锁，是一种悲观锁。
- 共享锁：是一种乐观锁。

根据一个线程是否可以再次获取它已经获取的锁：

- 可重入锁：synchronized内部锁是可重入锁

根据发现锁已经被其他线程占有，是否立即放弃CPU使用权，阻塞zi'ji

- 自旋锁：Java线程与操作系统线程一一对应，因此当线程获取锁失败时，会切换到内核状态而挂起，该操作开销大。自旋锁在获取锁失败时，不会马上阻塞自己，而是在不放弃CPU使用权的情况下，多次尝试获取(默认10）。即使用CPU时间获取线程阻塞与调度的开销，也可能得不偿失。

## Java 线程相关接口与类分析

Java 中多线程主要用到的类都在`java.util.concurrent`包下，其中常用的接口和类如下：

![RunnableFuture](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/RunnableFuture.png)

### Runable 接口

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}

```

### Callable 接口

为了弥补 Runnable 无法获取线程执行结果，也无法抛出异常的缺陷，引入了 Callable 接口

```java
@FunctionalInterface
public interface Callable<V> {
    /**
     * 计算结果，如果不能计算，则抛出异常。
     *
     * @return计算结果
     * 如果无法计算结果，则@抛出异常。
     */
    V call() throws Exception;
}
```

### Future 接口

Callable 有返回值，那么如何得到返回值等问题如何解决？答案就是使用 Future 接口

```java
boolean cancel(boolean mayInterruptIfRunning);
boolean isCancelled();
boolean isDone();
V get() throws InterruptedException, ExecutionException;
V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
```

### RunnableFuture 接口

```java
public interface RunnableFuture<V> extends Runnable, Future<V> {
    void run();
}
```

### FutureTask 类

继承了 RunnableFuture 接口，实现了其中的方法！

**构造函数：**

```java
public FutureTask(Callable<V> callable)
public FutureTask(Runnable runnable, V result)
```

### Object 类

该类是所有类的超类，除了 hashCode()、getClass()、equals()、toString()、clone()方法外，还有两类用于多线程的方法：

- wait

  ```java
  public final void wait() throws InterruptedException {
      wait(0L);
  }
  
  public final native void wait(long timeoutMillis) throws InterruptedException;
  
  public final void wait(long timeoutMillis, int nanos) throws InterruptedException {
      if (timeoutMillis < 0) {
          throw new IllegalArgumentException("timeoutMillis value is negative");
      }
  
      if (nanos < 0 || nanos > 999999) {
          throw new IllegalArgumentException(
              "nanosecond timeout value out of range");
      }
  
      if (nanos > 0 && timeoutMillis < Long.MAX_VALUE) {
          timeoutMillis++;
      }
  
      wait(timeoutMillis);
  }
  ```

- notify

  ```java
  @HotSpotIntrinsicCandidate
  public final native void notify();
  
  @HotSpotIntrinsicCandidate
  public final native void notifyAll();
  ```

### Thread 类

Thread 类下有很多方法，简介：

- run()用于创建线程时重写
- start()用于启动一个线程
- join()用于等待线程执行终止
- sleep()用于线程睡眠
- yield()用于让出 CPU 执行权
- 线程中断方法：
  - void interrupt()：中断线程
  - boolean isInterrupt()：检测线程是否中断，并返回结果
  - boolean interrupted()：检测线程是否中断，并返回结果，然后将中断状态清除(即设为 false)

## 线程的三种实现方法

### 1. 继承Thread类并重写run方法

缺点：Java只能单继承，因此该类无法再继承其他类了。

优点：方便传参，可以添加成员变量等，而其余方式只能使用主线程里被声明为final的变量。

```java
public class ThreadTest {
    public static class ThreadTask extends Thread{
        @Override
        public void run() {
            // do something
        }
    }

    public static void main(String[] args) {
        Thread t =new ThreadTask();
        t.start();
    }
}
```

### 2. 实现Runable接口的run方法

优点：任务与代码分离，更灵活

缺点：无法获取线程任务返回值

```java
public class RunnableTest {
  
    // 普通方法首先实现Runnable接口
    public static class RunnableTask implements Runnable{
        @Override
        public void run() {
          	// do something
        }
    }
  
    public static void main(String[] args) {
        // 普通方法
      	Thread t0 = new Thread(new RunnableTask());
				
				// 匿名内部类
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // do something
            }
        });
				
				// lambda
				Thread t2 = new Thread(()->{
            // do something
        });
      
				t0.start();
        t1.start();
				t2.start();
    }
}
```

### 3. FutureTask&Callable

构建对象FutureTask时，需要传入一个实现了Callable接口的call()方法的类实例：

- 可以直接构建一个类实现Callable接口
- 也可以直接使用内部类创建
- 也可用lambda表达式。

优点：可以获取返回值

```java
public class CallableTest  {
  
    // 普通方法首先实现Callable接口
    public static class CallableTask implements Callable{
        @Override
        public String call() throws Exception {
            // do something
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 普通方法
      	FutureTask futureTask0 = new FutureTask<String>(new CallableTask());
      
        // 匿名内部类
        FutureTask<String> futureTask1 = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                //do something
                return "hello";
            }
        });

        Thread t0 = new Thread(futureTask0);
      	Thread t1 = new Thread(futureTask1);
          
        t0.start();
        t1.start();

				// 获取返回值
        String result = futureTask1.get();
        System.out.println(result);  // hello
    }

}
```

## 线程的六种状态

![Thread_state](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Thread_state.png)

### NEW

当线程被 NEW 创建出来还没有被调用 start()时候的状态。

### RUNNABLE

当线程被调用了 start()，且处于等待操作系统分配资源（如 CPU）、等待 IO 连接、正在运行状态，即表示 Running 状态和 Ready 状态。

注：不一定被调用了 start()立刻会改变状态，还有一些准备工作，这个时候的状态是不确定的

### BLOCKED

等待监视锁，这个时候线程被操作系统挂起。

在下面两种情况下会进入阻塞状态：

- 进入 synchronized 块/方法
- 在调用 wait()被唤醒/超时之后重新进入 synchronized 块/方法，锁被其它线程占有，这个时候也被操作系统挂起

阻塞状态的线程，即使调用 interrupt()方法也不会改变其状态

### WAITING

无条件等待：

当线程调用 wait()/join()/LockSupport.park()不加超时时间的方法之后所处的状态，如果没有被唤醒或等待的线程没有结束，那么将一直等待，当前状态的线程不会被分配 CPU 资源和持有锁。

### TIMED_WAITING

有条件的等待：

- sleep(睡眠时间)
- wait(等待时间)
- join(等待时间)
- LockSupport.parkNanos(等待时间)
- LockSupport.parkUntil(等待时间)

当线程调用上述方法之后所处的状态，在指定的时间没有被唤醒或者等待线程没有结束，会被系统自动唤醒，正常退出。

![线程状态转换](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/线程状态转换.png)

图中有一些问题，一个线程只有拿到锁，才能进入等待状态，若是没有拿到锁就调用 wait()方法，线程会抛出 IllegalMoniterStateException 异常，而且**一个线程被 notify()或者 notifyAll()唤醒后，需要获得锁才能进入等待状态，也就是说，WAITING 状态的线程被唤醒后类似于进入阻塞状态，必须竞争到锁才能进入运行状态！**

同理，超时等待后若得不到锁，也会进入阻塞状态。

## 线程状态处理

鉴于继承机制，Java把所有类都需要的方法放到了Object类中，如线程通知与等待系列函数：

- wait()
- wait(long timeout)
- wait(log timeout, int nanos)
- notify()
- notifyAll()

Thread类提供了很多线程处理的方法，比如：

- start()

- join()
- Thread.sleep(long millis)
- Thread.sleep(long millis, int nanos)
- Thread.yield()

### 1. wait()

参数

- timeout：超时参数，一个线程调用共享对象的该方法挂起后，如果没有在指定timeout毫秒时间内被其他线程唤醒，那么该函数还是会因为超时而返回。默认为0，即wait()=wait(0)
- nanos：内部调用wait(long timeout)，具体可看源码。

调用方法：共享变量.wait()

当一个线程调用一个共享变量的wait()方法时，该调用线程会被阻塞挂起，直到发生下述事件之一：

> 例如：假设一个线程获得了共享变量 queue 上的锁，使用 queue.wait()方法后该线程会被挂起，进入 WAITING 状态，并且会释放获取的 queue 上的锁。

- 其他线程调用了该共享对象的notify()或notifyAll()方法
- 其他线程调用了该线程的interrupt()方法(e.g. thread1.interrupt())，该线程抛出InterruptedException异常返回

只有获得了该共享变量的锁的线程才能使用对应共享变量的 wait 方法，如果调用wait()方法的线程没有事先获取该对象的 `监视器锁` ，则调用wait()方法后，该线程会抛出IllegalMonitorException异常。

那么，一个线程如何才能获取一个共享变量的监视器锁呢？

```java
// 方法1，执行synchronized同步代码块时，使用该共享变量作为参数
synchronized(共享变量){
		// do something
}

// 方法2，调用该共享变量的方法，并且该方法使用了synchronized修饰
synchronized void add(int a, int b){
		// do something
}
```

一个线程可以从挂起状态变为可运行状态，且没有发生上述的事件，称为 `虚假唤醒` ，这种情况很少发生，但不得不防，可以通过不停地去测试该进程被唤醒的条件是否满足，不满足则继续等待，即循环调用wait()方法：

```java
synchronized(obj){
		while(条件不满足){
				obj.wait();
		}
}
```

### 2. notify()/notifyAll()

调用方法：共享变量.notify()

一个线程调用共享对象的notify()方法后，notify()会随机唤醒**一个**在该共享变量上使用 wait()后进入 WAITING 或 TIMED_WAITING 状态的线程，而 notifyAll()会唤醒该共享变量上**所有**的因使用 wait()后进入 WAITING 或 TIMED_WAITING 状态的线程。

注意，一个共享变量上可能会有多个线程在等待，具体唤醒哪个等待的线程是随机的！

此外，被唤醒的线程不能马上从wait()方法返回并执行，它必须要获取了共享对象的 `监视器锁` 之后才可以返回！而它是不一定会获取到该共享对象的 `监视器锁` 的，因为还需要与其他线程一起竞争该锁。若没有竞争成功，则继续处于wait()阶段！

与wait()方法类似，如果调用notify()方法的线程没有事先获取该对象的 `监视器锁` ，则调用notify()方法后，该线程会抛出IllegalMonitorException异常。

### 3. start()

调用方法：线程名.start()

使用 start() 方法后进入 RUNNABLE 态，会自动调用 run 方法，切记不可以直接调用 run 方法，这样子做的话就是一个普通方法，而不是启动一个线程！

start()方法的源码中有如下一句：

```java
/* Notify the group that this thread is about to be started 通知group这个线程即将开始。
 * so that it can be added to the group's list of threads   这样该线程就会被添加到该组线程列表中。
 * and the group's unstarted count can be decremented.      可以减少该组的未启动的线程的数量*/
group.add(this);
```

### 4. join()

调用方法：线程名.join()

在当前线程，比如 main 函数线程中，使用了 threadOne.join()，那么 main 函数线程会在调用 threadOne.join()后被阻塞，进入 WAITING 状态，只有 threadOne 线程执行完毕后，主线程才会继续执行下面的语句。

如果在 A 线程调用 B 线程的 join 方法后，处于阻塞状态时，其他线程调用了 A 线程的 interrupt()方法，线程 A 抛出 InterruptedException 异常而返回。

```java
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne=new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ThreadOne over!");
        });
        threadOne.start();
        System.out.println("wait ThreadOne over");
        threadOne.join(); //main线程等待threadOne线程运行完毕，此时main线程被阻塞，threadOne运行完毕后main才继续运行！
        System.out.println("main over");
    }
}
```

输出如下：

```shell
wait ThreadOne over
ThreadOne over!
main over
```

### 5. sleep()

调用方法：Thread.sleep()

不同于 Thread 中的 start 和 join 方法，**sleep 和 yield 是 Thread 中的静态方法**

查看源码：

```java
    /**
     * 导致当前执行的线程在指定的毫秒数内休眠（暂时停止执行），这取决于系统计时器和调度器的精度和准确性。
     * 该线程不会失去任何监视器的所有权。
     *
     * @param millis
     * 以毫秒为单位的睡眠时间长度
     *
     * @throws IllegalArgumentException
     * 如果{@code millis}的值为负值
     *
     * @throws InterruptedException
     * 如果有任何线程中断了当前的线程。当前线程的<i>interrupted status</i>当该异常被抛出时将被清除。
     */
public static native void sleep(long millis) throws InterruptedException;
```

作用是导致**当前线程**进入阻塞状态，睡眠期间：

- 让出 CPU 指定时间的使用权，也就是说在这段时间内不参与 CPU 的调度（即不分配时间片）
- 但是不会让出监视器资源，比如获得的`监视器锁`不会让出，sleep(val)结束后，线程直接进入就绪状态，参与CPU的调度，获取时间片（即CPU资源）后即可运行。
- 如果在睡眠期间，线程A调用线程B的 interrupt()方法中断了线程B，则线程B会在调用spleep()的地方抛出 InterruptedException 异常而返回

> 静态方法：Thread.currentThread();
>
> 可以获取当前线程

使用 sleep 后进入 TIMED-WAITING 状态，但是不能被 notify 唤醒，只能超时返回。

### 6. yield()

调用方法：Thread.sleep()

查看源码

```java
  /**
     * 暗示调度器，当前线程愿意放弃当前对处理器的使用。调度器可以随意忽略这个提示
     *
     * <p> Yield是一种启发式尝试，旨在改善线程之间的相对进度，否则会过度利用CPU。它的使用应该与详细的剖析和基准测试相结合，以	 * 确保它确实具有预期的效果。
     *
     * <p> 使用这种方法很少合适。它可能对调试或测试目的有用，因为它可能有助于重现由于竞赛条件而产生的错误。在设计并发控制结构··	   * 时，它也可能是有用的，比如在设计并发控制结构时。
     * {@link java.util.concurrent.locks}包。
     */
public static native void yield();
```

使用 yield 方法后：

- 表示愿意让出 CPU 的使用权，每个线程会被分配一个时间片，使用 yield 后就表示告诉线程调度器，我占有的时间片还没用完，但我不想用了，就是说暗示线程调度器现在就可以进行下一轮的线程调度
- 线程立即进入 RUNNABLE 中的就绪状态，参与CPU调度，此时线程调度器会从就绪线程队列中找到一个线程优先级最高的线程使其处于运行状态！

> 在Hotspot虚拟机中，yield() = sleep(0)，但是其他JVM的实现可能有所不同！

### 7. 线程中断

Java中的线程中断是一种线程间的协作模式，通过设置线程的 `中断标志` 并不能直接终止该线程的执行，而是被中断的线程根据中断状态自行处理。

Thread类中提供了三个中断相关的方法：

- **void interrupt()方法**：中断线程，threadA运行时，threadB可以调用threadA的interrupt()方法来设置threadA的 `中断标志` 为true，并立即返回。注意，设置标志仅仅只是设置标志，threadA实际上并没有被中断，它会继续往下执行。如果threadA因为调用了wait系列函数、join方法、sleep方法而被阻塞挂起，这时候若threadB调用threadA的interrupt()方法，则threadA会在调用这些方法的地方抛出InterruptedException异常而返回。

- **boolean inInterrupted()方法**：检测当前线程是否被中断，返回true/false

- static boolean interrupted()

  ：检测当前线程是否被中断，有两个不同点：

  - 静态方法，通过Thread.interrupted()直接调用，因此，返回的是当前调用线程的中断标志。
  - 该方法若发现当前线程被中断，则会**清楚中断标志**！

## 关键字

### synchronized

synchronized块是Java提供的一种 `原子性内置锁` ，其和 ReentrantLock 一样都是**互斥锁**(一次只能有一个线程进入到临界区(被锁定的区域))

Java中的每个对象都可以当做一个同步锁来使用，这些Java内置的使用者看不到的锁被称为 `内部锁`，也叫做 `监视器锁`。

根据对象的不同分为：

- 类锁：.class、静态方法、静态变量
- 实例锁：普通对象、this、普通方法

> 某个对象 Student，其中的普通方法 getName()加了实例锁，然后我们在 main 方法中创建线程：
>
> ```java
> //线程一
> new Thread(()->{
>     Student st = new Student();
>     st.getName();
> }).start();
> 
> //线程二
> new Thread(()->{
>     Student st = new Student();
>     st.getName();
> }).start();
> ```
>
> 此时两个线程可以同时执行 st.getName()，因为实例对象是两个不同的 Student，也就是说此时它们都能拿到锁！
>
> 如果 getName 是静态方法，也就是我们加了类锁，那么会被阻塞，因为类文件在 JVM 的运行区中，仅有一份！

线程的执行代码在进入synchronized代码块前会自动获取监视器锁，这时候其他线程的执行代码在进入synchronized代码块时会被阻塞挂起。

释放监视器锁：

- 正常退出同步代码块
- 抛出异常
- 在同步代码块中抵达用了wait()系列方法

Java中的线程是与操作系统的原生线程一一对应的，所以当阻塞一个线程时，需要从用户态切换到内核态执行阻塞操作，这是很耗时的！

synchronized的内存语义：

- 进入synchronized块的内存语义：把synchronized块内使用到的变量从线程的工作内存中清楚，此时读取该变量只能从主内存中获取
- 退出synchronized块的内存语义：把synchronized块内对共享变量的修改刷新到主内存

synchronized可以解决的问题：

- 解决共享变量**内存可见性**问题
- 实现**原子性**操作

注意：synchronized关键字会引起线程上下文切换并带来线程调度开销。

### volatile

![volatile](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/volatile.png)

已知synchronized可以解决共享变量可见性问题，但太过笨重，因为它会带来线程上下文的切换开销。

而volatile关键字提供了一种弱形式的同步，也可用于**解决内存可见性问题**。

volatile可以确保对一个变量的更新对其他线程马上可见，当一个变量被声明为volatile后：

- 线程在写入变量时不会把值缓存在寄存器或者其他地方，而是会把值刷新回主内存
- 当其他线程在读取该共享变量时，会从主内存重新获取最新值，而不是使用当前线程工作内存中的值。

什么时候使用volatile：

- 写入变量值不依赖变量的当前值，因为如果依赖当前值，将是 `获取-计算-写入` 三步操作，这三步不是原子性的，而volatile不保证原子性。
- 读写变量值时没有加锁，因为加锁本身已经保证了内存可见性(e.g. synchronized)，此时无需再次声明为volatile。

此外，volatile还可以**禁止Java指令重排序**。

## 线程安全问题

线程A和线程B可以同时操作主内存中的共享变量，如果该变量是只读变量，则不会有问题，如果是可读写变量，那么就会出现脏读等问题，因此需要在线程访问共享变量时进行适当的同步，最常见的是使用关键字synchronized进行同步。

线程安全问题分为两类：

- 内存可见性
- 原子性

### 共享变量的内存可见性问题

`Java内存模型` 规定，将所有的变量都存放在主内存中，当线程使用变量时，会把主内存里面的变量**复制**到自己的工作空间(又称工作内存），线程读写变量时操作的是自己工作内存中的变量。

`Java内存模型` 是一个抽象的定义，实际实现时如下：

![Java内存模型实际实现](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Java内存模型实际实现.png)

图中所示是一个双核CPU系统架构，每个核有自己的控制器、运算器和一级缓存(L1 Cache)，在有些架构中还有一个**所有CPU共享的二级缓存(**L2 Cache)。

该架构会出现**内存不可见问题**：

- Thread1 首先获取共享变量1的值，L1 Cache、L2 Cache都没命中，从主内存中读取共享变量x，修改x=1，存入L1 Cache、L2 Cache、主内存中。
- Thread2 首先获取共享变量1的值，L1 Cache未命中，L2 Cache命中，得x=1，将其修改x=2，存入L1 Cache、L2 Cache、主内存中。
- Thread1 再次获取共享变量1的值，L1 Cache命中，x=1，但此时Thread2已经修改x=2，也就是说，Thread2写入的值对Thread1不可见，这就是内存不可见问题。

如何解决：使用Java中的volatile关键字。

### 原子性操作问题

原子性操作：执行一系列操作时，这些操作要么全部执行，要不全部不执行。

```java
public class ThreadNotSafeCount {
    private Long value;
    public Long getCount(){
        return value;
    }
    public void inc(){
        ++value;
    }
}
```

上述代码是线程不安全的，因为不能保证 `++value` 是原子性操作，使用 `javap -c` 查看汇编代码，如下所示：

```java
public void inc();
    Code:
       0: aload_0
       1: aload_0
       2: getfield      #2                  // Field value:Ljava/lang/Long;
       5: invokevirtual #3                  // Method java/lang/Long.longValue:()J
       8: lconst_1
       9: ladd
      10: invokestatic  #4                  // Method java/lang/Long.valueOf:(J)Ljava/lang/Long;
      13: putfield      #2                  // Field value:Ljava/lang/Long;
      16: return
```

简单的 `++value` 由2、5、8、9、10、13步组成，因此在汇编之后它就不再具有原子性了。

如何保证原子性？最简单的方法是使用synchronized关键字。

```java
public class ThreadNotSafeCount {
    private Long value;
    public synchronized Long getCount(){
        return value;
    }
    public synchronized void inc(){
        ++value;
    }
}
```

此处为什么 `getCount()` 方法也要加synchronized关键字？

加上synchronized关键字之后同一时间仅能有一个线程调用，大大降低了并发性，而且`getCount()`只是读操作并不存在线程安全问题。

原因是：为了实现value的内存可见性！

显然这种方法过于繁重，因此提供了CAS算法来解决内存可见性问题。

#### CAS算法

已知synchronized可以解决：

- 内存可见性
- 原子性

为了弥补锁带来的开销问题，**Java提供了非阻塞volatile关键字来解决内存可见性问题，但该关键字并不能解决原子性问题**。

CAS即Compare and Swap是JDK提供的非阻塞原子性操作，它通过硬件保证了比较-更新操作的原子性。

JDK里面的Unsafe类提供了一系列的compareAndSwap*方法。

CAS操作有个经典的 `ABA问题` ，具体如下：

- Thread1 读取X的值为A，然后使用CAS修改变量X的值为B，又使用CAS修改变量X的值为A
- Thread2 获取X的值，发现和变量预期值A相同，成功继续执行CAS。

但是，此时Thread2获取的X的值A，其实不是原来的A，是Thread1**环形转换**的结果，即 `A-B-A` 。

如果变量只能朝一个方向转换，那么就不会出现ABA问题，JDK中的AtomicStampedReference类给每个变量的状态值都配备了一个时间戳，从而避免了ABA问题的产生。

### 指令重排序问题

Java内存模型允许编译器和处理器对指令重排序以提高运行性能，并且只会对不存在 `数据依赖性` 指令重排序。

在单线程下重排序可以保证最终执行的结果与程序顺序执行的结果一致，但是在多线程下会出现问题。

```java
//Thread Main Init
ready=false;
num=0;

//Thread 1
if(ready){             ①
		sout(num+num);     ②
}

//Thread 2
num=2;                 ③
ready=true;            ④
```

上述代码变量未声明为volatile，也没有使用任何同步措施，因为volatile会避免重排序，所以会存在共享变量内存可见性问题，此处我们先不考虑内存可见性问题。

如上图伪代码，最后语句②可能输出0，也可能4，甚至可能不运行。

因为①②③④语句并无 `数据依赖性` ，因此可能会如下重排序：

- Thread2先运行语句④
- Thread1运行语句①②，输出0
- Thread2再运行语句③

也可能如下重排序：

- Thread2运行语句③④
- Thread1运行语句①②，输出4

甚至如下：

- Thread1先运行语句①，发现ready=false，直接不输出

解决办法：使用volatile修饰ready变量即可避免重排序和内存可见性问题。

### 伪共享问题

为了解决计算机系统中主内存与CPU之间运行速度差问题，会在CPU与主内存之间添加一级或多级高速缓冲存储器(Cache)，一般会集成到CPU内部，所以也称为CPU Cache。

在CPU Cache内部是按行存储的，每一行称为一个Cache行，行是Cache与主内存进行数据交换的单位，Cache行的大小一般为2的幂次方。

![Cache行](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Cache行.png)

由于Cache行是内存块而不是单纯变量，所以可能会把多个变量放在一个Cache行中。

当多个线程同时修改一个缓存行里面的不同变量时，由于缓存一致性协议，同时只能有一个线程操作缓存行，相比于将每个变量放到一个缓存行，性能会有所降低，这就是 `伪共享` 。

在单线程的情况下，由于局部性原理，Cache行的存在可以加速程序的运行，而在多线程并发下修改一个缓存行中的不同变量时就会竞争缓存行，从而降低程序运行性能。

**如何避免伪共享？**

- 字节填充。JDK8之前采用的方法，假设缓存行64 byte，变量占8 byte，我们填充56 byte的空字符，来实现一个Cache行只存放一个变量。
- sun.misc.Contended注解：JDK8提供，可解决伪共享问题

## 锁

- 乐观锁与悲观锁：数据库中的概念，悲观锁指对数据被外界修改保持保守态度，认为数据很容易就会被其他线程修改，因此在数据处理签先对数据进行加锁，并在整个处理过程中，使数据处于锁定状态。而乐观锁则认为数据一般不会冲突，在访问记录前不会加排它锁，而是在进行数据提交更新时，才会正式对数据冲突与否进行检测。

- 公平锁与非公平锁：根据线程获取锁的抢占机制划分，公平锁表示线程获取锁的顺序是按照线程请求锁的**时间早晚**决定，非公平锁则是先来未必先得。

  ReentrantLock 提供了公平锁和非公平锁的实现：

  - 公平锁：ReentrantLock pairLock = new ReentrantLock(true)
  - 非公平锁：ReentrantLock pairLock = new ReentrantLock(false)，如果构造函数不传递参数，则默认是非公平锁。

  例如，假设线程A已经持有了锁，这时候线程B请求该锁其将会被挂起。当线程A释放锁后，假设线程C也需要获取钙锁。如果采用非公平锁方式，则根据线程调度策略，线程B和线程C两者之一可能获取锁，无需任何干涉，如果使用公平锁，则需要把C挂起，让B获取当前锁。

  因此，在没有公平性的前提下尽量使用非公平锁，因为公平锁会带来性能开销。

- 独占锁与共享锁：根据锁只能被单个线程持有还是能被多个线程共同持有划分。

  ReentrantLock 就是独占锁。ReentrantReadWriteLock 是共享锁

  独占锁是一种悲观锁，共享锁则是乐观锁。

  - 可重入锁：当线程获取一个被其他线程持有的独占锁时，该线程会被阻塞，那么当一个线程再次获取它自己已经获取的锁时是否会被阻塞呢？如果不会，则是可重入锁。

- 自旋锁：Java中的线程与操作系统的线程一一对应，线程运行在用户态，当它运行到需要锁的代码，且获取锁失败时，线程会被挂起，需要切换到内核态，执行挂起相关代码，此时需要切上下文，这需要巨大的开销。

  自旋锁在当前线程获取锁失败时，不会马上阻塞自即，在不放弃CPU使用权的情况下，多次尝试获取锁，默认是10次。

  如果在尝试过程中获取了锁，则无需进行上下文切换了，但自旋锁也可能只是白白浪费CPU时间。

  因此，自旋锁是使用CPU时间换取线程阻塞与调度的开销。

### ReentrantLock

- 比 synchronized 更有伸缩性(灵活)
- 支持公平锁(是相对公平的，默认非公平锁)
- 使用时最标准用法是在 try 之前调用 lock 方法，在 finally 代码块释放锁
- 支持多个条件变量：condition

```java
class X {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cname = lock.newCondition();
    // ...

    public void m() {
        lock.lock();  // block until condition holds
        try {
            while(不满足condition条件)  、
                //进入等待集（wait set），只有其他线程调用该条件的signalAll()才会唤醒！类似于notify()
                cname.await();
            // ... method body
            cname.signalAll();
        } finally {
            lock.unlock()
        }
    }
}
```

### ReadWriteLock

ReentrantReadWriteLock 是一个**读写锁**：

- 在**读**取数据的时候，可以**多个线程同时进入到到临界区**(被锁定的区域)
- 在**写**数据的时候，无论是读线程还是写线程都是**互斥**的

**在读的时候可以共享，在写的时候是互斥的**

读写锁也有公平和非公平模式

![ReadWriteLock](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/ReadWriteLock.png)

## 线程池

### 线程池接口与类分析

![线程池接口与类](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/线程池接口与类.png)

上图是整个线程池的类图，简单介绍如下：

- Executor 接口：execute()方法在ThreadPoolExecutor实现，后续讲解！
- ExecutorService 接口：提供了线程池生命周期管理的方法(抽象接口）
  - 提交用于执行的任务
    - submit()：提交一个任务(Callable|Runnable)，并返回future对象
  
  - 执行给定的任务
    - invokeAll()：提交一个Callable对象集合中的所有对象，这个方法会阻塞，直到所有任务都完成，并返回表示所有任务运行结果的future对象列表
    - invokeAny()：提交一个Callable对象集合中的所有对象，并返回其中一项任务(已完成的任务)的结果(future)
  
  - 线程池生命周期管理方法
    - shutdown()
  
- AbstractExecutorService：提供ExecutorService的默认实现，也新增了几个方法。
- ScheduledExecutorService：提供了 `延迟`和 `定期执行` 的ExecutorService，提供了一些方法安排任务在给定的延时执行或者周期性执行
- **☆ThreadPoolExecutor**：实现了execute()方法，可构建线程池对象，使用最多的线程池类，后面详解！
- ScheduledThreadPoolExecutor：提供了 `延迟`和 `定期执行` 的ThreadPoolExecutor
- ForkJoinPool：JDK1.7加入

![线程池部分接口与类_详细方法](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/线程池部分接口与类_详细方法.png)

####  [Runnable]和[Callable、Future]

从上图的接口方法等，你会发现很多Future、Callable和Runnable，下面对此做简要介绍。

**Runnable**

Runnable封装一个异步运行的任务，可以把它想象成一个 `无参数无返回值` 的异步方法。

**Callable**

Callable和Runable类似，但是有返回值。Callable接口是一个参数化类型，类型参数是返回值的类型，例如：Callable<Integer>表示一个最终返回Integer对象的异步计算。

Callable的执行方法：

- FutureTask，它实现了Future和Runable接口，所以可以构造一个线程来运行这个任务。

```java
public class ThreadTest  {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                //do something
                return "hello";
            }
        });

        Thread t = new Thread(futureTask);
        t.start();

				// 获取返回值
        String result = futureTask.get();
        System.out.println(result);  // hello
    }

}
```

- 更常用的方法，将Callable传递到一个执行器。

**Future**

Future保存异步计算的结果，一般我们认为是Callable的返回值，但它其实代表的是任务的生命周期

Future\<V\>方法介绍：

- V get()：调用该方法会阻塞，直到任务完成；可传入超时参数，如果在计算完成之前超时，会抛出TimeoutException异常。
- boolean isDone()：顾名思义，是否完成，无论是正常完成、中途取消，还是发生异常，都返回true
- boolean cancel(boolean mayInterrupt)：尝试取消任务的运行，如果任务未开始，它会被取消且不再开始；如果任务已经开始，那么如果传入的mayInterrupt为true，它会被中断。如果成功执行了取消操作，则返回true
- boolean isCancelled()：如果任务在完成前被取消，则返回true。

![FutureTask](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/FutureTask.png)

#### ThreadPoolExecutor

查阅ThreadPoolExecutor的代码文档注释可知：

```java
/**
 @英文版
 To be useful across a wide range of contexts, this class provides many adjustable parameters and extensibility hooks.
 However, programmers are urged to use the more convenient
    {@link Executors} factory methods
 		{@link Executors#new CachedThreadPool} (unbounded thread pool, with automatic thread reclamation),
		{@link Executors#new FixedThreadPool} (fixed size thread pool) and
   	{@link Executors#new SingleThreadExecutor} (single background thread),
 that preconfigure settings for the most common usage scenarios. Otherwise, use the following guide when manually configuring and tuning this class:

  @中文版
  为了适应不同的应用场景，ThreadPoolExecutor类提供了许多可调整的parameters和可扩展性的hooks。
  然而，推荐使用更方便的 Executors 工厂方法：
 		Executors.newCachedThreadPool()       非绑定线程池，自动回收线程
		Executors.newFixedThreadPool()        固定大小的线程池
   	Executors.newSingleThreadExecutor()   单后台线程
  基于最常见的使用场景配置的ThreadPoolExecutor
  当手动配置和调整ThreadPoolExecutor时，请使用以下指南：
  ...
*/
```

由此可知，创建线程池的方法有两种：

- new ThreadPoolExecutor()
- 调用工具类 Executors 的静态工厂方法
  - FixedThreadPool：该方法返回一个固定线程数量的线程池，该线程池中的线程数量始终不变，当有一个新的任务提交时，线程池中若有空闲线程，则立即执行。若没有，则新的任务会被暂存在一个任务队列中，待有线程空闲时，便处理在任务队列中的任务。
  - SingleThreadExecutor：方法返回一个只有一个线程的线程池，若多余一个任务被提交到该线程池，任务会被保存在一个任务队列中，待线程空闲，按先入先出的顺序执行队列中的任务。
  - CachedThreadPool：该方法返回一个可根据实际情况调整线程数量的线程池，线程池的线程数量不确定，若有空闲线程可以复用，则会优先使用可复用的线程。若所有线程均在工作，又有新的任务提交，则会创建新的线程处理任务。所有线程在当前任务执行完毕后将放回线程池进行复用。

查阅Executors源码可知，其本质上就是new ThreadPoolExecutor()，只是传入了特定的参数罢了。

---

##### Executors解析

执行器（Executors）类中定义了许多静态工厂，用来构造线程池，汇总如下：

```java
/**
	* corePoolSize:    0                 
	* maximumPoolSize: Integer.MAX_VALUE
	* keepAliveTime:   60
	* unit:            TimeUnit.SECONDS
	* WorkQueue:       SynchronousQueue
	* 核心线程为0，最大线程无穷大，队列为SynchronousQueue，说明排队策略为Direct handoffs，即无队列。说明有任务时直接创建新线程，且无限创建。
	* (线程数量超过corePoolSize时）空闲线程保留时间为：60s
	*/
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
}

/**
	* corePoolSize:    nThreads             
	* maximumPoolSize: nThreads
	* keepAliveTime:   0
	* unit:            TimeUnit.MILLISECONDS
	* WorkQueue:       LinkedBlockingQueue
	* 核心线程 == 最大线程，因此空闲线程会一直被保留，队列为LinkedBlockingQueue，说明排队策略为Unbounded queues，即队列长度无限。
	* 永远不会创建新线程(线程数等于corePoolSize），新任务会在队列中排队。
	* 空闲线程保留时间为：无意义，设为0ms
	*/
public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
}

/**
	* corePoolSize:    1           
	* maximumPoolSize: 1
	* keepAliveTime:   0
	* unit:            TimeUnit.MILLISECONDS
	* WorkQueue:       LinkedBlockingQueue
	* 核心线程 == 最大线程 == 1，队列为LinkedBlockingQueue，说明排队策略为Unbounded queues，即队列长度无限。
	* 永远不会创建新线程(线程数等于1），新任务会在队列中排队。
	* 空闲线程保留时间为：无意义，设为0ms
	*/
public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
}
```

---

如果想要自定义ThreadPoolExecutor，必须首先了解下述概念：

- **Core and maximum pool sizes**

  - 如果运行的线程数大于corePoolSize，则创建新线程来处理请求，即使存在空闲线程
  - 如果运行的线程数大于corePoolSize，但是小于maximumPoolSize，则仅当队列满时才创建新线程
  - 如果设置的 `corePoolSize = maximumPoolSize` ，则 `创建了固定大小的线程池` 。
  - 通过将maximumPoolSize设置为一个基本无界的数(e.g. Integer.MAX_VALUE)，可以允许线程池容纳任意数量的并发任务

- **On-demand construction**：按需构建，默认情况下，即便是核心线程也只有在新任务到达时才初始化和启动，可以动态覆盖。

- **Creating new threads**：创建新线程，默认由Executors.defaultThreadFactory创建，都属于同一个线程组，相同的优先级(NORM_PRIORITY)，非守护线程状态。通过提供不同的ThreadFactory，可以更改线程的name, thread group, priority, daemon status等

- **Keep-alive times**：如果当前线程池中的线程数大于corePoolSize，则多余的线程的空闲时间如果超过 keepAliveTime，将被终止。这提供了一种在未积极使用线程池时减少资源消耗的方法。可以使用setKeepAliveTime(long, TimeUnit)方法动态更改此参数。

- **Queuing**：队列的使用与线程池的大小交互：

  - 如果正在运行的线程数小于corePoolSize，则Executor总是添加新线程而不是排队
  - 如果正在运行的线程数大于corePoolSize，则Executor总是请求排队而不是添加新线程
  - 如果队列满了不能入队：
    - 运行的线程数大于corePoolSize，但是小于maximumPoolSize，创建新线程。
    - 运行的线程数等于maximumPoolSize，任务将被拒绝。

  排队的一般策略有如下三种：

  1. Direct handoffs. 一个很好的默认选择是SynchronousQueue，它将任务移交给线程而不用其他方式保留它们，即无队列！一般用于线程池是无界限的情况。
  2. Unbounded queues. 无界队列，队列长度无限(e.g. 没有预定义容量的LinkedBlockingQueue)，**线程数永远不会超过corePoolSize**！即maximumPoolSize的值没有任何效果。
  3. Bounded queues. 有界队列(e.g. ArrayBlockingQueue)，与有限的maximumPoolSizes一起使用时有助于防止资源耗尽，但较难调整和控制，队列大小和最大池大小可以相互权衡：
     1. 大队列，小线程池：最大限度地减少CPU使用率、操作系统资源和上下文切换开销，但会导致人为地降低吞吐量。
     2. 小队列：通常需要更大的线程池，这会使CPU更忙，但可能会遇到不可接受的调度开销(线程多，切换线程开销大)，也会降低吞吐量。

- **Rejected tasks**：使用方法execute(Runnable)提交的新任务将被拒绝，在如下两种情况：

  - Executor 已经被 shut down
  - Executor 设置了maximumPoolSize，并且使用Bounded queues，且队列已饱和。

  四种拒绝任务策略：

  - ThreadPoolExecutor.AbortPolicy：默认策略，拒绝任务时直接抛出RejectedExecutionException异常
  - ThreadPoolExecutor.CallerRunsPolicy：用调用者自己的线程来执行任务，这提供了一个简单的反馈控制机制，可以减缓提交新任务的速度。如果是main线程提交的任务，那么会使用main线程来执行这个任务。
  - ThreadPoolExecutor.DiscardPolicy：直接丢弃任务
  - ThreadPoolExecutor.DiscardOldestPolicy：如果执行器没有关闭，工作队列头部的任务会被丢弃(最旧的任务)，然后重试执行。可能会再次失败，导致重复执行。

- **Hook methods**：提供protected overridable methods，即可重写命令(beforeExecute, afterExecute)，在每个任务执行之前和之后调用，可用于操作执行环境，例如重新初始化ThreadLocals、收集统计信息或添加日志条目等；此外，可以重写terminated方法，在Executor完全终止后执行一些特殊处理。

  ```java
  protected void beforeExecute(Thread t, Runnable r) { }
  protected void afterExecute(Runnable r, Throwable t) { }
  protected void terminated() { }
  ```

此外，有必要了解**线程池的状态**：

```java
public class ThreadPoolExecutor extends AbstractExecutorService {
		private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int COUNT_MASK = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;
}
```

- RUNNING：线程池**能够接受新任务**，以及对新添加的任务进行处理。
- SHUTDOWN：线程池**不可以接受新任务**，但是可以对已添加的任务进行处理。
- STOP：线程池**不接收新任务，不处理已添加的任务，并且会中断正在处理的任务**。
- TIDYING：当**所有的任务已终止**，ctl 记录的"任务数量"为 0，线程池会变为 TIDYING 状态。当线程池变为 TIDYING 状态时，会执行hook函数 terminated()。terminated()在 ThreadPoolExecutor 类中是空的，若用户想在线程池变为 TIDYING 时，进行相应的处理；可以通过重载 terminated()函数来实现。
- TERMINATED：线程池**彻底终止的状态**。

查阅ThreadPoolExecutor最长的**构造函数**如下：

```java
public class ThreadPoolExecutor extends AbstractExecutorService {
    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters.
     *
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,               // 最大线程数
                          		long keepAliveTime,                // 空闲线程存活时间
                          		TimeUnit unit,                     // 时间单位, 例如TimeUnit.SECONDS
                          		BlockingQueue<Runnable> workQueue, /* 线程队列:
                              																			SynchronousQueue
                              																			ArrayBlockingQueue
                              																			LinkedBlockingQueue
                              																			*/
                          		ThreadFactory threadFactory,       // 线程工厂
                         			RejectedExecutionHandler handler)  /* 任务拒绝处理器:
                         																						ThreadPoolExecutor.AbortPolicy 
                         																						ThreadPoolExecutor.CallerRunsPolicy
                         																						ThreadPoolExecutor.DiscardPolicy | 
                         																						ThreadPoolExecutor.DiscardOldestPolicy
                         																						*/
		{
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
}
```

查阅使用：

- 默认的线程工厂ThreadFactory
- 默认的任务拒绝处理器RejectedExecutionHandler

ThreadPoolExecutor的构造函数，其源码为：

```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue) {
  this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
       Executors.defaultThreadFactory(), defaultHandler);
}

// 默认RejectedExecutionHandler为AbortPolicy，拒绝任务时直接抛出RejectedExecutionException异常
private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();
public static class AbortPolicy implements RejectedExecutionHandler {
  public AbortPolicy() { }
  public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    throw new RejectedExecutionException("Task " + r.toString() +
                                         " rejected from " +
                                         e.toString());
  }
}
// 默认的线程工厂
public static ThreadFactory defaultThreadFactory() {
  return new DefaultThreadFactory();
}
```

查阅默认线程工厂的源码：

```java
private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :        // 线程组 thread group
                                  Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +                            // 线程名
                          poolNumber.getAndIncrement() +
                         "-thread-";
        }
				
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,     // 创建一个新线程！
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())         //非守护线程
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)  // 优先级设为Thread.NORM_PRIORITY
                t.setPriority(Thread.NORM_PRIORITY);  
            return t;
        }
}
```

可以看到其继承了ThreadFactory接口，其源码为：

```java
public interface ThreadFactory {

    /**
     * Constructs a new Thread.  Implementations may also initialize
     * priority, name, daemon status, ThreadGroup, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or null if the request to create a thread is rejected     
     */
    Thread newThread(Runnable r);
}
```

该接口最简单的实现为：

```java
class SimpleThreadFactory implements ThreadFactory {
   public Thread newThread(Runnable r) {
     return new Thread(r);
   }
 }
```

此时就是返回了最简单的线程，而默认的线程工厂中还为线程设置了线程名、优先级等！

#### execute()方法探究

最后，讨论**execute()方法将在何时被调用？**

该方法是接口Executor定义的唯一的方法，用于执行任务。

根据继承关系：

```
Executor -> ExecutorService -> AbstractExecutorService -> ThreadPoolExecutor
```

接口Executor声明了execute方法，接口ExecutorService声明了submit、invokeAll、invokeAny等运行任务的方法和shutdown等池状态管理方法。

抽象类AbstractExecutorService实现了接口ExecutorService中的方法，**类ThreadPoolExecutor实现了接口Executor中的方法(i.e. execute())**，其源码为：

```java
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
         /*
         * 分三步走：
         *
         * 如果运行的线程少于corePoolSize线程，尝试用给定的命令启动一个新的线程作为它的第一个任务。 
         * 调用addWorker原子化地检查runState和workerCount，
         * 因此通过返回false来防止错误报警，以免在不应该增加线程的时候增加线程。
         *
         * 2.如果一个任务可以成功排队，那么我们仍然需要仔细检查是否应该增加一个线程：
         *	 因为现有的线程在上次检查后就死了
         *	 或者线池在进入这个方法后就关闭了。
         *   所以我们要重新检查状态，如果停止了，必要时回滚enqueuing，如果没有，则启动一个新的线程
         *
         * 3.如果我们不能排队任务，那么我们就尝试添加一个新的线程。 如果失败，我们知道我们已经关闭或饱和，所以拒绝任务。
         */

        int c = ctl.get();
        //如果线程池中运行的线程数量<corePoolSize，则通过addWordker创建一个新线程来处理请求，即使其他辅助线程是空闲的。
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;

            c = ctl.get();
        }

	/*如果线程池中运行的线程数量>=corePoolSize,且线程池处于RUNNING状态，且把提交的任务成功放入阻塞队列中
       就再次检查线程池的状态:
           1.如果线程池不是RUNNING状态，且成功从阻塞队列中删除任务，则该任务由当前 RejectedExecutionHandler 处理。
           2.否则如果线程池中运行的线程数量为0，则通过addWorker(null, false)尝试新建一个线程，新建线程对应的任务为null*/
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }

         /* 如果以上两种case不成立，即没能将任务成功放入阻塞队列中，且addWoker新建线程失败，
        	则该任务由当前 RejectedExecutionHandler 处理。*/
        else if (!addWorker(command, false))
            reject(command);
    }
```



同时，根据类ThreadPoolExecutor的注释，一个ExecutorService使用线程池执行每个提交的任务，而**ThreadPoolExecutor正是一种线程池的实现！**

```java
import java.util.concurrent.*;
public class ThreadPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();  //①
        Future<String> f1 = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello";
            }
        });
        System.out.println(f1.get());
        executor.shutdown();
    }
}
```

如上，我们在提交一个Callable任务对象时，会调用submit()方法。

分析**语句①**源码可知，我们其实构建的对象是 `new ThreadPoolExecutor` ，然后在Executors工具类的newSingleThreadExecutor方法的返回值中将其声明为ExecutorService，由于Java的多态特性，ThreadPoolExecutor类中没有重写submit等方法，因此我们**最终会运行AbstractExecutorService中的submit()方法**，查看源码如下：

```java
public <T> Future<T> submit(Callable<T> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task);
        execute(ftask);
        return ftask;
}

protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask<T>(callable);
}
```

此处我们先回忆，在非线程池的情况下，线程有三种实现方法，其中一种是FutureTask&Callable，[参见](#3. FutureTask&Callable)。

再看submit的实现，其最终也是构建了一个FutureTask类，该类接受了一个Callable对象，然后将ftask传入execute()中执行！

同理，invokeAll、invokeAny最终也会调用execute()方法。

此外，值得注意的是，submit()还有其他几种实现，用于适应线程的Runnable接口实现方式：

```java
public Future<?> submit(Runnable task) {
  if (task == null) throw new NullPointerException();
  RunnableFuture<Void> ftask = newTaskFor(task, null);
  execute(ftask);
  return ftask;
}

public <T> Future<T> submit(Runnable task, T result) {
  if (task == null) throw new NullPointerException();
  RunnableFuture<T> ftask = newTaskFor(task, result);
  execute(ftask);
  return ftask;
}
```

#### execute()方法和 submit()方法的区别

在此总结一下**执行 execute()方法和 submit()方法的区别？**

- execute()方法用于**提交不需要返回值的任务**，所以**无法判断任务是否被线程池执行成功**

  execute()方法是在 Executor 接口中定义，在 ThreadPoolExecutor 类中实现。

- submit()方法用于**提交需要返回值的任务**，线程池会返回以恶搞 Future 类型的对象，通过这个对象可以判断任务是否执行成功，并且可以通过 Future 的 get()方法来获取返回值。

  submit()方法是在 ExecutorService 接口中定义，在 AbstractExecutorService 抽象类中实现。

  submit()最终还是通过调用execute()来执行！

### 线程池示例测试

```java
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {
    private static final int CORE_POOL_SIZE =2;    //核心线程
    private static final int MAX_POOL_SIZE =3;     //最大线程
    private static final long KEEP_ALIVE_TIME =1L;
    private static final int QUEUE_CAPACITY =2;    //阻塞队列容量

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY)
//                ,new ThreadPoolExecutor.CallerRunsPolicy() //使用调用者的线程来处理
//                ,new ThreadPoolExecutor.DiscardPolicy()    //直接丢弃
                ,new ThreadPoolExecutor.AbortPolicy()        //直接抛出异常
        );


        for (int i = 0; i < 10; i++) {
            executor.execute(()->{
                System.out.println(Thread.currentThread().getName()+" start time = "+new Date());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" End time = "+new Date());
            });

        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finish all threads");
    }


}
```

#### 测试一

核心线程：2

最大线程：3

阻塞队列容量：2

拒绝策略： new ThreadPoolExecutor.DiscardPolicy() ，直接丢弃

运行结果：

```java
pool-1-thread-3 start time = Sun Dec 06 19:21:00 CST 2020
pool-1-thread-1 start time = Sun Dec 06 19:21:00 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:21:00 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:21:02 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:21:02 CST 2020
pool-1-thread-1 start time = Sun Dec 06 19:21:02 CST 2020
pool-1-thread-3 start time = Sun Dec 06 19:21:02 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:21:02 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:21:04 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:21:04 CST 2020
Finish all threads

Process finished with exit code 0
```

解释：由于我们有 10 个任务，首先创建两个核心线程，然后第三个放入阻塞队列，然后将第四个放入阻塞队列，此时阻塞队列中存放的任务数达到队列容量，当前可同时运行的线程数变为最大线程数 3。

于是 start 了三个线程：1，2，3

此时第五个任务提交，此时有 3 个线程在运行，阻塞队列中有 1 个任务，于是将第五个任务放入阻塞队列

第六个任务提交，此时有 3 个线程在运行，阻塞队列中有 2 个任务，此时同时运行的线程数量达到最大线程数量，并且队列也已经被放满了任务，就运行拒绝策略。

此时的拒绝策略是 DiscardPolicy() ，直接丢弃，因此后面的任务都被丢弃，从结果中可以看到，我们一个 start 了五个任务。

#### 测试二

核心线程：2

最大线程：3

阻塞队列容量：2

拒绝策略： new ThreadPoolExecutor.AbortPolicy()，直接抛出异常

运行结果：

```java
Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task CH12.面试复习.线程池.ThreadPoolExecutorTest$$Lambda$16/0x0000000800b95040@3941a79c rejected from java.util.concurrent.ThreadPoolExecutor@506e1b77[Running, pool size = 3, active threads = 3, queued tasks = 2, completed tasks = 0]
	at java.base/java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2057)
	at java.base/java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:827)
	at java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1357)
	at CH12.面试复习.线程池.ThreadPoolExecutorTest.main(ThreadPoolExecutorTest.java:29)
pool-1-thread-3 start time = Sun Dec 06 19:18:51 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:18:51 CST 2020
pool-1-thread-1 start time = Sun Dec 06 19:18:51 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:18:53 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:18:53 CST 2020
pool-1-thread-3 start time = Sun Dec 06 19:18:53 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:18:53 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:18:53 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:18:55 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:18:55 CST 2020
```

解释：类似于测试一，只不过拒绝策略变成了直接抛出异常！

#### 测试三

核心线程：2

最大线程：3

阻塞队列容量：2

拒绝策略：new ThreadPoolExecutor.CallerRunsPolicy() ，使用调用者的线程来处理

运行结果：

```java
pool-1-thread-1 start time = Sun Dec 06 19:20:25 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:20:25 CST 2020
pool-1-thread-3 start time = Sun Dec 06 19:20:25 CST 2020
main start time = Sun Dec 06 19:20:25 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:20:27 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:20:27 CST 2020
main End time = Sun Dec 06 19:20:27 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:20:27 CST 2020
main start time = Sun Dec 06 19:20:27 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:20:27 CST 2020
pool-1-thread-1 start time = Sun Dec 06 19:20:27 CST 2020
pool-1-thread-3 start time = Sun Dec 06 19:20:27 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:20:29 CST 2020
main End time = Sun Dec 06 19:20:29 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:20:29 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:20:29 CST 2020
pool-1-thread-1 start time = Sun Dec 06 19:20:29 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:20:29 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:20:31 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:20:31 CST 2020
Finish all threads

Process finished with exit code 0
```

解释：前面同测试一，不过拒绝策略是使用调用者的线程来处理，于是在第六个任务来临时，也就是阻塞队列已满，且当前运行的线程达到最大线程数时，会使用调用者的线程来处理：`main start time = Sun Dec 06 19:20:25 CST 2020`，可以看到一共 start 了十个线程！

#### 测试四

核心线程：5

最大线程：7

阻塞队列容量：10

拒绝策略：new ThreadPoolExecutor.CallerRunsPolicy() ，使用调用者的线程来处理

运行结果：

```java
pool-1-thread-1 start time = Sun Dec 06 19:24:25 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:24:25 CST 2020
pool-1-thread-4 start time = Sun Dec 06 19:24:25 CST 2020
pool-1-thread-3 start time = Sun Dec 06 19:24:25 CST 2020
pool-1-thread-5 start time = Sun Dec 06 19:24:25 CST 2020
pool-1-thread-4 End time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-2 start time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-3 start time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-4 start time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-5 End time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-5 start time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-1 start time = Sun Dec 06 19:24:27 CST 2020
pool-1-thread-2 End time = Sun Dec 06 19:24:29 CST 2020
pool-1-thread-3 End time = Sun Dec 06 19:24:29 CST 2020
pool-1-thread-1 End time = Sun Dec 06 19:24:29 CST 2020
pool-1-thread-5 End time = Sun Dec 06 19:24:29 CST 2020
pool-1-thread-4 End time = Sun Dec 06 19:24:29 CST 2020
Finish all threads

Process finished with exit code 0
```

解释：由于没有阻塞队列不曾装满，因此可运行线程数量是等于核心线程数，也就是 5，同样也不会运行拒绝策略。

![线程池流程图](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E6%B5%81%E7%A8%8B%E5%9B%BE.png)

## ScheduledThreadPoolExecutor 类

相当于提供了延时执行和周期性执行的 ThreadPoolExecutor 类

```java
public class ScheduledThreadPoolExecutor
        extends ThreadPoolExecutor
        implements ScheduledExecutorService
```

## ForkJoinPool 类

JDK1.7 中新增的一个线程池，与 ThreadPoolExecutor 一样，同样继承了 AbstractExecutorService。ForkJoinPool 是 Fork/Join 框架的两大核心类之一。与其它类型的 ExecutorService 相比，**其主要的不同在于采用了工作窃取算法(work-stealing)**：所有池中线程会尝试找到并执行已被提交到池中的或由其他线程创建的任务。这样很少有线程会处于空闲状态，非常高效。这使得能够有效地处理以下情景：**大多数由任务产生大量子任务的情况**；从外部客户端大量提交小任务到池中的情况。

# TODO

- Java并发编程之美：高级篇，包含并发包中的原子操作类、并发List、锁原理、队列原理等，还有实战篇！
- 锁Lock(参考java 核心卷）：可能要完善下？
- Unsafe类