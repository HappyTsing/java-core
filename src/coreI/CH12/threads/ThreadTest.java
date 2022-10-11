package coreI.CH12.threads;

/**
 * 1. 一个线程运行一个任务，将任务代码放在一个实现Runnable接口的类的run方法中！
 *              Runnable接口是一个函数式接口，因为它只有一个抽象方法run()，故而我们使用lambda表达式实现
 *              即：Runable r=() -> { run函数的具体内容 }
 * 2. 通过这个Runnable构造一个Thread对象：var t = new Thread(r);
 *
 * 3. 启动该线程：t.start();
 *
 * 定义线程还有另一个方法：通过建立Thread类的一个子类来定义线程，但是不推荐！
 * @author QingHappy
 */
public class ThreadTest {
    public static final int DELAY = 10;
    public static final int STEPS = 100;
    public static final double MAX_AMOUNT = 1000;

    public static void main(String[] args) {
        var bank = new Bank(4, 40000);
        Runnable task1=()->{
            try {
                for (int i = 0; i < STEPS; i++) {
                    double amount = MAX_AMOUNT * Math.random();
                    bank.transfer(0, 1, amount);
                    Thread.sleep((int) (DELAY * Math.random()));
                }
            } catch (InterruptedException e) {
            }
        };
        Runnable task2=()->{
            try {
                for (int i = 0; i < STEPS; i++) {
                    double amount = MAX_AMOUNT * Math.random();
                    bank.transfer(2, 3, amount);
                    Thread.sleep((int) (DELAY * Math.random()));
                }
            } catch (InterruptedException e) {
            }
        };
        new Thread(task1).start();
        new Thread(task2).start();

    }
}


