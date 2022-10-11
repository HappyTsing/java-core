package coreI.CH12.synchSynchronized;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Bank {

    private final double[] accounts;
    private Lock bankLock;
    private Condition sufficientFunds;

    /**
     * Constructs the bank.
     * @param n the number of accounts
     * @param initialBalance the initial balance for each account
     */
    public Bank(int n, double initialBalance)
    {
        accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
//        bankLock = new ReentrantLock();
//        sufficientFunds = bankLock.newCondition();
    }

    /**
     * 如果一个方法声明有synchronized关键字，那么对象的锁将保护整个方法
     * 但是内联对象锁只有一个关联条件，wait方法将一个线程增加到等待集中，notifyAll/notify方法可以解除等待线程的阻塞
     */
    public synchronized void transfer(int from, int to, double amount) throws InterruptedException {

//        bankLock.lock();
//        try {
            while (accounts[from] < amount)
                wait();
//                sufficientFunds.await();
            System.out.print(Thread.currentThread());
            accounts[from] -= amount;
            System.out.printf(" %10.2f from %d to %d", amount, from, to);
            accounts[to] += amount;
            System.out.printf(" Total Balance: %10.2f%n", getTotalBalance());
            notifyAll();
//            sufficientFunds.signalAll();
//        } finally {
//            bankLock.unlock();
//        }
    }


    public synchronized double getTotalBalance()
    {
//        bankLock.lock();
//        try
//        {
            double sum = 0;

            for (double a : accounts)
                sum += a;

            return sum;
//        }
//        finally
//        {
//            bankLock.unlock();
//        }
    }

    /**
     * Gets the number of accounts in the bank.
     * @return the number of accounts
     */
    public int size()
    {
        return accounts.length;
    }
}
